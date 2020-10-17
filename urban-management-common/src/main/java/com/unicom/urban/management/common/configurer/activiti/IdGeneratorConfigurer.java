package com.unicom.urban.management.common.configurer.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置activiti主键生成策略
 *
 * @author liukai
 */
@Configuration
public class IdGeneratorConfigurer {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        StrongUuidGenerator uuidGenerator = new StrongUuidGenerator();
        processEngineConfiguration.setIdGenerator(uuidGenerator);
        processEngineConfiguration.getDbSqlSessionFactory().setIdGenerator(uuidGenerator);
        return processEngineConfiguration;
    }

}
