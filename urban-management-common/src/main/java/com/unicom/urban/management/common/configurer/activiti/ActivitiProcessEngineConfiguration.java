package com.unicom.urban.management.common.configurer.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Activiti流程引擎配置类
 *
 * @author liukai
 */
@Slf4j
@Configuration
public class ActivitiProcessEngineConfiguration extends AbstractProcessEngineAutoConfiguration {


    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource,
                                                                             EntityManagerFactory entityManagerFactory,
                                                                             PlatformTransactionManager transactionManager,
                                                                             SpringAsyncExecutor springAsyncExecutor,
                                                                             ProcessEngineConfigurator activitiFontConfigurator,
                                                                             ProcessEngineConfigurator activitiIdConfigurator) throws IOException {

        SpringProcessEngineConfiguration config = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        config.setJpaEntityManagerFactory(entityManagerFactory);
        config.setTransactionManager(transactionManager);
        config.setJpaHandleTransaction(false);
        config.setJpaCloseEntityManager(false);
        config.addConfigurator(activitiFontConfigurator).addConfigurator(activitiIdConfigurator);


        return config;
    }
}
