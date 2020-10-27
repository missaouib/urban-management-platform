package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.pojo.entity.EventButton;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivitiService {


    /**
     * 上报事件
     *
     * @param eventId  事件ID
     * @param userList 受理员的用户ID
     * @return 流程实例ID
     */
    ProcessInstance reportEvent(String eventId, List<String> userList);


    void xxx(String userId, Pageable pageable);

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
