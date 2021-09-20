package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启用Feign客户端功能
 * @EnableFeignClients
 * @EnableDiscoveryClient
 *
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CrowdMainAuthProvider {

    public static void main(String[] args) {

        SpringApplication.run(CrowdMainAuthProvider.class, args);

    }
}
