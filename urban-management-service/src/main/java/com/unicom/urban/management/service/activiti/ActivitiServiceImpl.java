package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.common.constant.EventSourceConstant;
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

    @Override
    public void complete(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if ("核实反馈".equals(task.getName())) {

        }

        taskService.complete(taskId);
    }


    @Override
    public List<EventButton> queryButton(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return queryButtonList(task.getName());

    }

    private List<EventButton> queryButtonList(String taskName) {

//        List<EventButton> eventButtonList = eventButtonRepository.findByTaskName(task.getName());
        List<EventButton> eventButtonList = new ArrayList<>();
        if ("核实反馈".equals(taskName)) {
            EventButton button1 = new EventButton();
            button1.setId("1");
            button1.setButtonValue("受理");

            EventButton button2 = new EventButton();
            button2.setId("2");
            button2.setButtonValue("不予受理");

            eventButtonList.add(button1);
            eventButtonList.add(button2);
        }
        if ("值班长-立案".equals(taskName)) {
            EventButton button1 = new EventButton();
            button1.setId("3");
            button1.setButtonValue("回退");

            EventButton button2 = new EventButton();
            button2.setId("4");
            button2.setButtonValue("立案");

            eventButtonList.add(button1);
            eventButtonList.add(button2);
        }

        if ("信息核实".equals(taskName)) {
            EventButton button1 = new EventButton();
            button1.setId("5");
            button1.setButtonValue("完成任务");
            eventButtonList.add(button1);
        }

        return eventButtonList;
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
