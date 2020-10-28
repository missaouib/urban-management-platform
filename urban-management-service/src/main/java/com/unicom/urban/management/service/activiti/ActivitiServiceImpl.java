package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.dao.event.EventButtonRepository;
import com.unicom.urban.management.pojo.entity.EventButton;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 封装activiti功能
 *
 * @author liukai
 */
@Slf4j
@Service
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private EventButtonRepository eventButtonRepository;

    private final static String EVENT_KEY = "event";

    private ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
    }


    private ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    @Override
    public ProcessInstance reportEvent(String eventId, List<String> userList, String eventSource) {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("shouliyuanList", userList);
        variables.put("eventSource", eventSource);
        ProcessInstance processInstance = startProcessInstanceByKey(EVENT_KEY, eventId, variables);
        log.debug("-------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("-------------受理员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("-------------事件来源 eventSource:{}-----------------------------------------------", eventSource);
        return processInstance;
    }

    @Override
    public Task getTaskByProcessInstanceId(String processInstanceId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (CollectionUtils.isEmpty(taskList)) {
            log.error("task集合不应该为空");
            throw new BusinessException("任务流转出现异常");
        }
        return taskList.get(0);
    }

    @Override
    public void xxx(String userId, Pageable pageable) {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(userId)
                .processDefinitionKey("hotline")
                .orderByTaskCreateTime().desc()
                .listPage(pageable.getPageNumber(), pageable.getPageSize());

        for (Task task : taskList) {
            String processInstanceId = task.getProcessInstanceId();
//            String businessKey = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceId).singleResult().getBusinessKey();
        }

    }

    @Override
    public void complete(String taskId) {
        taskService.complete(taskId);
    }


    //    @Override
    public void asdasd(String taskId) {
//        taskService.complete();
    }

    @Override
    public List<EventButton> queryButton(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<EventButton> eventButtonList = eventButtonRepository.findByTaskName(task.getName());
        return eventButtonList;
    }

    public List<Map<String, String>> getButton(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        log.debug(task.getName());

//        taskService.complete();


        return null;

    }

    // 根据taskId查找当前任务出口
//    @Override
    public List<String> getOutGoingTransNames(String taskId) {
        // 获取流程定义
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        // 获取流程实例
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        // 根据流程实例查找当前活动id
        String activityId = pi.getActivityId();
        // 根据活动id查找当前活动对象
        ActivityImpl activity = pd.findActivity(activityId);
        // 根据活动对象查找所有出口
        List<PvmTransition> outgoingTransitions = activity.getOutgoingTransitions();
        // 将所有出口封装成集合
        List<String> transNames = new ArrayList<String>();
        for (PvmTransition trans : outgoingTransitions) {
            String transName = (String) trans.getProperty("name");
            if (transName != null) {
                // 将出口名字加入到集合
                transNames.add(transName);
            }
        }
        // 如果该节点没有出口，默认设置按钮为提交
        if (transNames.size() == 0) {
            transNames.add("提交");
        }
        return transNames;
    }

}
