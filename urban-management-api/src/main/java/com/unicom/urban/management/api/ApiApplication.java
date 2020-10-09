package com.unicom.urban.management.api;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 接口启动类
 *
 * @author liukai
 */
@ComponentScan(value = {"com.unicom.urban.management"})
@EntityScan(basePackages = "com.unicom.urban.management.pojo.entity")
@EnableJpaRepositories(basePackages = "com.unicom.urban.management.dao")
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
