package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * To change it use File | Settings | Editor | File and Code Templates.
 *
 * @author Peter
 * @date 2021/9/15 11:52
 * @description: TODO
 */
@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService sqlRemoteService;

    private final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {

        // 1.执行地址信息的保存
        ResultEntity<String> resultEntity = sqlRemoteService.saveAddressRemote(addressVO);

        logger.debug("地址保存处理结果：" + resultEntity.getResult());

        // 2.从session域获取orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_DATA);

        // 3.从orderProjectVO对象中获取returnCount
        Integer returnCount = orderProjectVO.getReturnCount();

        // 4.重定向到指定地址，重新进入确认订单页面
        return CrowdConstant.REDIRECT_ZUUL + "/order/confirm/order/" + returnCount;
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session
    ) {
        // 1.把接收到的returnCount合并到Session域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_DATA);

        orderProjectVO.setReturnCount(returnCount);

        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_DATA, orderProjectVO);

        // 2.获取当前用户的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        ResultEntity<List<AddressVO>> resultEntity = sqlRemoteService.getAddressVORemote(memberId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> addressVOList = resultEntity.getData();

            session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_DATA, addressVOList);
        }

        return "confirm-order";
    }


    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity =
                sqlRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO orderProjectVO = resultEntity.getData();

            // 为了能够在后续操作中保持 orderProjectVO 数据， 存入 Session 域
            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_DATA, orderProjectVO);
        }

        return "confirm-return";
    }


}
