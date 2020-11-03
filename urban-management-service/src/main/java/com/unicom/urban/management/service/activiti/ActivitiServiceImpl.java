package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.common.constant.EventSourceConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.dao.event.EventButtonRepository;
import com.unicom.urban.management.pojo.entity.EventButton;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        // 监督员上报
        if (EventSourceConstant.SUPERVISE_REPORTING.equals(eventSource)) {
            return superviseReporting(eventId, userList);
        }
        // 受理员上报
        if (EventSourceConstant.ACCEPTANCE_REPORTING.equals(eventSource)) {
            return acceptanceReporting(eventId, userList);
        }
        throw new BusinessException("非法事件来源: " + eventSource);
    }

    @Override
    public ProcessInstance superviseReporting(String eventId, List<String> userList) {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("userId", userList);
        variables.put("eventSource", EventSourceConstant.SUPERVISE_REPORTING);
        ProcessInstance processInstance = startProcessInstanceByKey(EVENT_KEY, eventId, variables);
        log.debug("----------------------监督员上报事件开始--------------------------------------");
        log.debug("----------------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("----------------------监督员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("----------------------监督员上报事件结束--------------------------------------");
        return processInstance;
    }

    @Override
    public ProcessInstance acceptanceReporting(String eventId, List<String> userList) {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("userId", userList);
        variables.put("eventSource", EventSourceConstant.ACCEPTANCE_REPORTING);
        ProcessInstance processInstance = startProcessInstanceByKey(EVENT_KEY, eventId, variables);
        log.debug("----------------------受理员上报事件开始--------------------------------------");
        log.debug("----------------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("----------------------受理员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("----------------------受理员上报事件结束--------------------------------------");
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
    public List<String> queryGroupEvent(String userId, String taskId, Pageable pageable) {

        TaskQuery taskQuery = taskService.createTaskQuery();

        Task task = taskQuery.taskId(taskId).singleResult();

        List<Task> taskList = taskQuery.taskCandidateUser(userId).taskName(task.getName()).listPage(pageable.getPageNumber(), pageable.getPageSize());

        Set<String> taskIds = taskList.parallelStream().map(Task::getProcessDefinitionId).collect(Collectors.toSet());

        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(taskIds).list();

        return processInstanceList.parallelStream().map(ProcessInstance::getBusinessKey).collect(Collectors.toList());

    }

    @Override
    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public List<String> queryMyTask(String userId, Pageable pageable) {

        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).listPage(pageable.getPageNumber(), pageable.getPageSize());

        Set<String> taskIds = taskList.parallelStream().map(Task::getProcessDefinitionId).collect(Collectors.toSet());

        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(taskIds).list();

        return processInstanceList.parallelStream().map(ProcessInstance::getBusinessKey).collect(Collectors.toList());

    }

    @Override
    public void claim(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

//    @Override
//    public void complete(String taskId) {
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        Map<String, Object> variables = new HashMap<>(3);
//
//        if ("核实反馈".equals(task.getName())) {
//            variables.put("", "");
//        }
//
//        if ("".equals(task.getName())) {
//            variables.put("", "");
//        }
//
//
//        taskService.complete(taskId, variables);
//
//    }

    @Override
    public String complete(String taskId, List<String> userList, String buttonId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        Map<String, Object> variables = new HashMap<>(3);

        if ("核实反馈".equals(task.getName())) {
            variables.put("", "");
        }

        if ("核实反馈".equals(task.getName())) {
            variables.put("shouliyuanType", buttonId);
            variables.put("userId", userList);
        }


        taskService.complete(taskId, variables);

        //TODO 应该返回taskId
        return null;
    }


    @Override
    public List<EventButton> queryButton(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return queryButtonList(task.getName());
    }

    private List<EventButton> queryButtonList(String taskName) {
        return eventButtonRepository.findByTaskNameOrderBySort(taskName);
    }

}
