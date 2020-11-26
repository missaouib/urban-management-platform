package com.unicom.urban.management.web;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author liukai
 */
@ComponentScan(value = {"com.unicom.urban.management", "org.activiti.rest.diagram.services"})
@EntityScan(basePackages = "com.unicom.urban.management.pojo.entity")
@EnableJpaRepositories(basePackages = "com.unicom.urban.management.dao")
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WebApplication {

    public static void main(String[] args) {
//        SpringApplication.run(WebApplication.class, args);
        LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
    }

}
