package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | Editor | File and Code Templates.
 * @author Peter
 * @Date: 2021/9/12 0:42
 * @Description:
 *
 */
// 启用Feign客户端功能
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CrowdMainProjectProvider {

    public static void main(String[] args) {
        SpringApplication.run(CrowdMainProjectProvider.class, args);
    }
}
