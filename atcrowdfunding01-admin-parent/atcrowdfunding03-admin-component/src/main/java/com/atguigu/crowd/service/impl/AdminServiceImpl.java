package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录对象查询admin对象
        AdminExample adminExample = new AdminExample();

        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(loginAcct);

        List<Admin> adminList = adminMapper.selectByExample(adminExample);

        // 2.判断admin对象是否为null
        if (adminList == null || adminList.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (adminList.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        // 3.如果admin对象为null则抛出异常
        Admin admin = adminList.get(0);

        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果admin对象不为null将数据库密码从admin对象中取出
        String userPswdDb = admin.getUserPswd();

        // 5.将表单提交的密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        // 6.将密码进行对比
        if (!Objects.equals(userPswdDb, userPswdForm)) {
            // 7.密码不匹配
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8.密码匹配则返回对象
        return admin;
    }

    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPswd = admin.getUserPswd();

        //userPswd = CrowdUtil.md5(userPswd);

        userPswd = passwordEncoder.encode(userPswd);

        admin.setUserPswd(userPswd);

        // 2.生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        // 执行保存， 如果账户被占用会抛出异常
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {

            /**
             * 异常未能抛出，改在Handler中抛出
             */

            e.printStackTrace();
            // 检测当前捕获的异常对象，如果是 DuplicateKeyException 类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException) {
                // 抛出自定义的 LoginAcctAlreadyInUseException 异常
                throw new
                        LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            // 为了不掩盖问题，如果当前捕获到的不是 DuplicateKeyException 类型的异常，则把当前捕获到的异常对象继续向上抛出
            throw e;
        }

        logger.info(admin.toString() + " 已注册。");

        //throw new RuntimeException();
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize) {
        // 1.开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 2.查询 Admin 数据
        List<Admin> adminList = adminMapper.selectAdminListByKeyword(keyword);

        // ※辅助代码： 打印 adminList 的全类名
        logger.debug("adminList 的全类名是： " + adminList.getClass().getName());

        // 3.为了方便页面使用将 adminList 封装为 PageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);

        return pageInfo;
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);

        logger.info("id是[" + adminId + "]的admin对象已删除");
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        // "Selective"表示有选择的更新，对于null值的字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            // 检测当前捕获的异常对象，如果是 DuplicateKeyException 类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException) {
                // 抛出自定义的 LoginAcctAlreadyInUseException 异常
                throw new
                        LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            // 为了不掩盖问题，如果当前捕获到的不是 DuplicateKeyException 类型的异常，则把当前捕获到的异常对象继续向上抛出
            throw e;
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 1.根据aminId删除旧的关联数据
        adminMapper.deleteOldRelationship(adminId);

        // 2.根据roleIdList和adminId保存新的关系
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> list = adminMapper.selectByExample(example);

        Admin admin = list.get(0);

        return admin;
    }

    @Override
    public void removeAdminArray(List<String> loginAcctArray) {
        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAcctIn(loginAcctArray);

        adminMapper.deleteByExample(example);
    }
}
