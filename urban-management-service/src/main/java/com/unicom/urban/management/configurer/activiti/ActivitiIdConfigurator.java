package com.unicom.urban.management.configurer.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.cfg.AbstractProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.springframework.stereotype.Component;

/**
 * 配置Activiti id生成器
 *
 * @author liukai
 */
@Slf4j
@Component(value = "activitiIdConfigurator")
public class ActivitiIdConfigurator extends AbstractProcessEngineConfigurator {

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void beforeInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        StrongUuidGenerator uuidGenerator = new StrongUuidGenerator();
        processEngineConfiguration.setIdGenerator(uuidGenerator);
    }

    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {

    }
}
