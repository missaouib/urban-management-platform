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
     * @param eventSource 事件来源
     * @return 流程实例ID
     */
    ProcessInstance reportEvent(String eventId, List<String> userList, String eventSource);

    /**
     * 根据流程实例ID获取任务Task
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    Task getTaskByProcessInstanceId(String processInstanceId);

    /**
     * 查询待办任务
     *
     * @return eventId
     */
    List<String> queryMyTask(String userId, Pageable pageable);

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
    void complete(String taskId);


}
