package com.unicom.urban.management.web.framework.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ActivitiTest {


    private ProcessEngine processEngine;

    private RepositoryService repositoryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://192.168.23.123:32722/urban_management_platform?useUnicode=true&autoReconnect=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("Root@123")
                .setDbIdentityUsed(false)
                .buildProcessEngine();

        repositoryService = processEngine.getRepositoryService();
    }

    @Test
    public void newModel() {

        String name = "上报事件流程";
//        String description = "上报事件流程description";

//        ObjectNode metaInfo = objectMapper.createObjectNode();
//        metaInfo.put(ModelDataJsonConstants.MODEL_NAME, name);
//        metaInfo.put(ModelDataJsonConstants.MODEL_REVISION, 1);
//        metaInfo.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);


        Model model = repositoryService.newModel();
        model.setName(name);
//        model.setMetaInfo(metaInfo.toString());

        repositoryService.saveModel(model);


        ObjectNode editorNode = objectMapper.createObjectNode();
        ObjectNode stencilSetNode = objectMapper.createObjectNode();

        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");


        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        editorNode.set("stencilset", stencilSetNode);
        System.out.println("editorNode = " + editorNode);
        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));


        System.out.println("model.getId() =============================================== " + model.getId());

        //        repositoryService.deleteModel(model.getId());

//        repositoryService.createModelQuery().de


    }

}
