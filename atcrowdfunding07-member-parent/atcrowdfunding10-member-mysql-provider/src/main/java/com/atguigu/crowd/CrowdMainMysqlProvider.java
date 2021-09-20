package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @MapperScan 扫描 MyBatis 的 Mapper 接口所在的包
 */
@MapperScan("com.atguigu.crowd.mapper")
@SpringBootApplication
public class CrowdMainMysqlProvider {

    public static void main(String[] args) {

        SpringApplication.run(CrowdMainMysqlProvider.class, args);

    }
}
