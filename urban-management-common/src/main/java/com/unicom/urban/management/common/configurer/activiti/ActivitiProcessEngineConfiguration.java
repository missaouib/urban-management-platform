package com.unicom.urban.management.common.configurer.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Activiti流程引擎配置类
 *
 * @author liukai
 */
@Configuration
public class ActivitiProcessEngineConfiguration {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        StrongUuidGenerator uuidGenerator = new StrongUuidGenerator();
        processEngineConfiguration.setIdGenerator(uuidGenerator);
        processEngineConfiguration.getDbSqlSessionFactory().setIdGenerator(uuidGenerator);
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setActivityFontName("宋体");
        return processEngineConfiguration;
    }

}
