package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * \* Created with IntelliJ IDEA.
 * \* To change this template use File | Settings | Editor | File and Code Templates.
 * \* @author Peter
 * \* @Date: 2021/9/12 0:25
 * \* @Description:
 * \
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // view-controller是在project-consumer内部定义的，所以这里是一个不经过Zuul访问的地址，
        // 所以这个路径前面不加路由规则中定义的前缀：“/project”
        registry.addViewController("/agree/protocol/page").setViewName("project-agree");
        registry.addViewController("/launch/project/page").setViewName("project-launch");
        registry.addViewController("/return/info/page").setViewName("project-return");
        registry.addViewController("/create/confirm/page").setViewName("project-confirm");
        registry.addViewController("/create/success").setViewName("project-success");
        registry.addViewController("/show/detail").setViewName("project-detail");

    }
}
