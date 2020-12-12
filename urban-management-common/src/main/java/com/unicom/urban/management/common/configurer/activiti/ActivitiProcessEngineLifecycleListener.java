package com.unicom.urban.management.common.configurer.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * 流程引擎生命周期监听器
 *
 * @author liukai
 */
@Slf4j
@Component
public class ActivitiProcessEngineLifecycleListener implements ProcessEngineLifecycleListener {
    @Override
    public void onProcessEngineBuilt(ProcessEngine processEngine) {
        log.debug("processEngines built success");
    }

    @Override
    public void onProcessEngineClosed(ProcessEngine processEngine) {
        log.debug("processEngines close success");
    }
}
