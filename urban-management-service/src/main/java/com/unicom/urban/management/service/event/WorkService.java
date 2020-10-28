package com.unicom.urban.management.service.event;

import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.service.activiti.ActivitiService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用工作流
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class WorkService {

    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private EventService eventService;

    public void reportEvent(String eventId) {
        /*TODO 查询所有有受理员角色的人*/
        List<String> userList = new ArrayList<>();
        userList.add("1");
        /*TODO 查询所有有监督员角色的人*/
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.reportEvent(eventId, userList, event.getEventSource().getId());

        Task task = activitiService.getTaskByProcessInstanceId(processInstance.getId());
        event.setTaskId(task.getId());
        eventService.update(event);
    }

}
