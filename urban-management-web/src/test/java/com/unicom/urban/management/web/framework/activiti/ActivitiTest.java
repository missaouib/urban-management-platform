package com.unicom.urban.management.web.framework.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.util.List;

@Slf4j
public class ActivitiTest {


    @Test
    public void testProcessEngines() {
        ProcessEngine processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://192.168.23.123:32722/urban_management_platform?useUnicode=true&autoReconnect=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("Root@123")
                .buildProcessEngine();

        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();


        String deploymentId = "22504";
        List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames(deploymentId);

//        for (String deploymentResourceName : deploymentResourceNames) {
//            System.out.println(deploymentResourceName);
//
//            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).list();
//        }


        List<Deployment> list = repositoryService.createDeploymentQuery().deploymentId(deploymentId).list();
        System.out.println("list.size() = " + list.size());

    }


}
