package com.unicom.urban.management.web.framework.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ActivitiTest {


    private ProcessEngine processEngine;

    private RepositoryService repositoryService;

    private RuntimeService runtimeService;

    private TaskService taskService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://192.168.23.123:32722/urban_management_platform?useUnicode=true&autoReconnect=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("Root@123")
                .setDbIdentityUsed(false)
                .setHistoryLevel(HistoryLevel.FULL)
                .setDbHistoryUsed(true)
                .buildProcessEngine();

        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
    }

    @Test
    public void testEvent() {
        String eventId = "1";

        List<String> shouliyuanList = Arrays.asList("zhangsan", "lisi", "wangwu");

        Map<String, Object> map = new HashMap<>();

        map.put("reportUserId", "liukai");
        map.put("shouliyuanList", shouliyuanList);


        ProcessInstance event = runtimeService.startProcessInstanceByKey("event", eventId, map);

        System.out.println(event.getProcessInstanceId());


//        Task zhangsanTask = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();

//        System.out.println(zhangsanTaskList.size());


//        taskService.complete(zhangsanTask.getId());


//        for (String s : shouliyuanList) {
//            Task task = taskService.createTaskQuery().taskAssignee(s).singleResult();
//            System.out.println(task);
//        }
//
//        Task zhangsan = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
//
//        taskService.complete(zhangsan.getId());
//
//        for (String s : shouliyuanList) {
//            Task task = taskService.createTaskQuery().taskAssignee(s).singleResult();
//            System.out.println(task);
//        }


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
