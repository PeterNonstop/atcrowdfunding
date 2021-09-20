package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 改成以spring-security.xml方式引入WebAppSecurityConfig bean
//@Configuration

// 启动web环境下权限控制功能
@EnableWebSecurity

// 启用全局方法权限控制功能，并且设置prePostEnabled = true。保证@PreAuthority、@PostAuthority、@PreFilter、@PostFilter生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        // 临时登录
        //builder.inMemoryAuthentication().withUser("admin").password("123123").roles("ADMIN");

        // 连接数据登录
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security.authorizeRequests()
                .antMatchers("/index.jsp", "/admin/to/login/page.html", "/admin/to/main/page.html").permitAll()
                .antMatchers("/bootstrap/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**", " /layer/**", "/script/**", "/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")//.hasRole("经理")
                .access("hasRole('经理') or hasAuthority('user:get')")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {

                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response,
                                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
                    }
                })
                .and()
                .formLogin()         // 开启表格登录
                .loginPage("/admin/to/login/page.html")      // 指定登录页面
                .loginProcessingUrl("/admin/do/login.html")  // 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html")  // 指定登录成功后前往的地址
                .usernameParameter("loginAcct") // 账号的请求参数名称
                .passwordParameter("userPswd")  // 密码的请求参数名称
                .and()
                .logout()                       // 开启退出登录
                .logoutUrl("/admin/do/logout.html")                      // 指定退出登录功能
                .logoutSuccessUrl("/admin/to/login/page.html")           // 指定退出成功以后往前的地址
                .permitAll()
                .and()
                .csrf().disable();

        //security.csrf().disable();


    }
}
