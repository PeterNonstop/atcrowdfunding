package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.CustomException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctUpdateException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    private final Logger logger = LoggerFactory.getLogger(AdminHandler.class);


    @RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword
    ) {
        // 生成修改时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        /**
         * 因为在AdminServiceImpl中异常没抛出，所以在handler抛出。
         */

        try {
            adminService.update(admin);
        } catch (Exception e) {
            e.printStackTrace();
            // 检测当前捕获的异常对象，如果是 DuplicateKeyException 类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException) {
                // 抛出自定义的 LoginAcctUpdateException 异常
                throw new
                        LoginAcctUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_UPDATE);
            }
            // 为了不掩盖问题，如果当前捕获到的不是 DuplicateKeyException 类型的异常，则把当前捕获到的异常对象继续向上抛出
            throw e;
        }


        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }


    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {
        // 1.根据adminId查询Admin对象
        Admin admin = adminService.getAdminById(adminId);

        // 2.将admin存入模型
        modelMap.addAttribute("admin", admin);

        return "admin-edit";
    }

    /**
     * spring security接管后，这里不再执行。
     * 没有设置session导致remove admin有bug
     *
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ) {

        // 调用 Service 方法执行登录检查
        // 这个方法如果能够返回 admin 对象说明登录成功， 如果账号、 密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        // 将登录成功返回的 admin 对象存入 Session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        logger.info("admin:" + admin);

        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/to/login/page.html")
    public String toLoginPage() {
        return "admin-login";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(

            // 使用@RequestParam注解的defaultValue属性，指定默认值，在请求中没有携带对应参数时使用默认值
            // keyword默认值使用空字符串，和SQL语句配合实现两种情况适配
            @RequestParam(value = "keyword", defaultValue = "") String keyword,

            // pageNum默认值使用1
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,

            // pageSize默认值使用5
            @RequestParam(value = "pageSize", defaultValue = "9") Integer pageSize,

            ModelMap modelMap

    ) {

        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getAdminPage(keyword, pageNum, pageSize);

        // 将PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin-page";
    }


    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword,
            HttpSession session
    ) throws Exception {
        /**
         * 不能删除当前登录用户
         */

        // 从session中获取当前admin
        // 使用spring security后已失效
        //Admin currAdmin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        Admin currAdmin = new Admin();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currAdmin = adminService.getAdminByLoginAcct(currentUserName);
        } else {
            throw new CustomException(CrowdConstant.MESSAGE_LOGIN_ACCT_NO_EXIST);
        }

        // 通过adminId查询出将要被删除的admin，或者不用查数据库直接用页面传过来的adminId
        // Admin removedAdmin = adminService.getAdminById(adminId);

        if (!Objects.equals(currAdmin.getId(), adminId)) {
            // 执行删除
            adminService.remove(adminId);
            return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
        } else {

            throw new CustomException(CrowdConstant.MESSAGE_CURR_LOGIN_ACCT_NO_DEL);

        }
        // 页面跳转：回到分页页面

        // 尝试方案1：直接转发到admin-page.jsp会无法显示分页数据
        // return "admin-page";

        // 尝试方案2：转发到/admin/get/page.html地址，一旦刷新页面会重复执行删除浪费性能
        // return "forward:/admin/get/page.html";

        // 尝试方案3：重定向到/admin/get/page.html地址
        // 同时为了保持原本所在的页面和查询关键词再附加pageNum和keyword两个请求参数
        //return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    // 批量删除
    @ResponseBody
    @RequestMapping("/admin/remove/by/loginAcct/array.json")
    public ResultEntity<String> removeByAdminArray(@RequestBody List<String> loginAcctArray){

        adminService.removeAdminArray(loginAcctArray);

        logger.info(loginAcctArray + "已经被删除~");

        return ResultEntity.successWithoutData();
    }


    // 用户保存的权限
    @PreAuthorize("hasAnyAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {

        /**
         * 因为在AdminServiceImpl中异常没抛出，所以在handler抛出。
         */

        try {
            adminService.saveAdmin(admin);
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

        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

}
