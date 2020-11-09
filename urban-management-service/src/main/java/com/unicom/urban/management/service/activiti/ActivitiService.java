package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.pojo.entity.EventButton;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivitiService {


    /**
     * 上报事件
     *
     * @param eventId     事件ID
     * @param userList    用户ID
     * @param eventSource 事件来源 {@link com.unicom.urban.management.common.constant.EventSourceConstant}
     * @return 流程实例ID
     */
    ProcessInstance reportEvent(String eventId, List<String> userList, String eventSource);

    /**
     * 监督员上报流程开启
     *
     * @param eventId  事件ID
     * @param userList 受理员用户ID
     * @return 流程实例
     */
    ProcessInstance superviseReporting(String eventId, List<String> userList);

    /**
     * 受理员上报流程开启
     *
     * @param eventId  事件ID
     * @param userList 监督员用户ID
     * @return 流程实例
     */
    ProcessInstance acceptanceReporting(String eventId, List<String> userList);

    /**
     * 上报自处理事件
     *
     * @param eventId  事件ID
     * @param userList TODO
     * @return 流程实例
     */
    ProcessInstance reportAutoEvent(String eventId, List<String> userList);

    /**
     * 根据流程实例ID获取任务Task
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    Task getTaskByProcessInstanceId(String processInstanceId);


    /**
     * 查询组任务 (没有被领取的任务)
     */
    List<String> queryGroupEvent(String userId, String taskId, Pageable pageable);


    /**
     * 领取任务
     *
     * @param taskId taskId
     * @param userId 领取任务的这个人的userId
     */
    void claim(String taskId, String userId);

    /**
     * 根据taskId获取task
     *
     * @param taskId taskId
     * @return Task
     */
    Task getTaskById(String taskId);

    /**
     * 查询待办任务 带分页
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return eventId 集合
     */
    List<String> queryTaskByAssignee(String userId, Pageable pageable);

    /**
     * 查询待办任务
     *
     * @param userId 用户ID
     * @return eventId
     */
    List<String> queryTaskByAssignee(String userId);

    /**
     * 查询指定环节的待办任务 带分页
     *
     * @param userId   用户ID
     * @param taskName 环节名称 可查询多个环节
     * @param pageable 分页信息
     * @return eventId 集合
     */
    List<String> queryTaskByAssigneeAndTaskName(String userId, List<String> taskName, Pageable pageable);


    /**
     * 查询指定环节的待办任务 不带分页
     *
     * @param userId   用户ID
     * @param taskName 环节名称 可查询多个环节
     * @return eventId 集合
     */
    List<String> queryTaskByAssigneeAndTaskName(String userId, List<String> taskName);

    /**
     * 查询当前任务 都有什么按钮
     *
     * @param taskId 当前任务ID
     * @return 按钮集合
     */
    List<EventButton> queryButton(String taskId);


    /**
     * 完成任务
     */
//    void complete(String taskId);

    /**
     * 完成任务
     *
     * @param taskId   任务ID
     * @param userList 下一步处理人
     * @param buttonId 页面点击的哪个按钮ID
     */
    void complete(String taskId, List<String> userList, String buttonId);


}
