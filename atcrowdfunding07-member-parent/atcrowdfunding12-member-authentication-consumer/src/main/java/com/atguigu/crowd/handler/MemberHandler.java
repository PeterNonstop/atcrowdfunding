package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    /**
     * No qualifying bean of type 'com.atguigu.crowd.api.RedisRemoteService' available
     * Consider defining a bean of type 'com.atguigu.crowd.api.RedisRemoteService' in your configuration.
     * <p>
     * 不能注入，在启动类上添加
     *
     * @EnableFeignClients
     * @EnableDiscoveryClient
     */
    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService sqlRemoteService;

    private final Logger logger = LoggerFactory.getLogger(MemberHandler.class);

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/logout")
    public String doLogout(HttpSession session) {

        session.invalidate();

        // 退出到auth-provider工程
        // return "redirect:/";

        // 退出到zuul工程
        return CrowdConstant.REDIRECT_ZUUL;
    }

    /**
     * @param loginacct
     * @param userpswd
     * @param modelMap
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String doLogin(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
    ) {
        // 调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = sqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();

        if (memberPO == null) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        // 比较密码
        String userpswdDataBase = memberPO.getUserpswd();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

        if (!matcheResult) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        // 创建MemberLoginVO对象存入session
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        //return "redirect:/auth/member/to/center/page";

        // 所有重定向所从Zuul开始
        return CrowdConstant.REDIRECT_ZUUL + "/auth/member/to/center/page";
    }


    /**
     * 注册
     *
     * @param memberVO
     * @param modelMap
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String doRegistry(MemberVO memberVO, ModelMap modelMap) {

        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼Redis中存储验证码的Key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.从Redis中读取对应的value
        ResultEntity<String> queryRedisResultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);

        // 4.检查查询操作是否有效
        String result = queryRedisResultEntity.getResult();

        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, queryRedisResultEntity.getMessage());

            return "member-reg";
        }

        // 5.如果从Redis中能够查询到Value时则比较表单验证码和Redis验证码
        String redisCode = queryRedisResultEntity.getData();
        if (redisCode == null) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);

            return "member-reg";
        }

        String formCode = memberVO.getCode();
        if (!Objects.equals(formCode, redisCode)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);

            return "member-reg";
        }

        // 6.如果验证码一致，则从Redis中删除
        ResultEntity<String> removeRedisKeyResultEntity = redisRemoteService.removeRedisKeyRemote(key);
        // 如果删除失败，记录日志
        if (ResultEntity.FAILED.equals(removeRedisKeyResultEntity.getResult())) {
            logger.info("redis key=" + key + ", 删除失败，请注意！");
        }

        // 7.执行密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userpswdForm = memberVO.getUserpswd();

        String encode = bCryptPasswordEncoder.encode(userpswdForm);
        memberVO.setUserpswd(encode);

        // 8.执行保存
        // 创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        // 复制属性
        BeanUtils.copyProperties(memberVO, memberPO);

        // 调用远程方法
        ResultEntity<String> saveMemberResultEntity = sqlRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册
        // return "redirect:/auth/member/to/login/page";

        return CrowdConstant.REDIRECT_ZUUL + "/auth/member/to/login/page";
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        logger.info("phoneNum=" + phoneNum);

        // 1.发送验证码到phoneNum手机
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(), phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());

        // 2.判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            // 3.如果发送成功，则将验证码存入Redis
            // ①从上一步操作的结果中获取随机生成的验证码
            String code = sendMessageResultEntity.getData();

            logger.info("验证码 code=" + code);

            // ②拼接一个用于在Redis中存储数据的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            // ③调用远程接口存入Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            // ④判断结果
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {

                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }

    }

}
