package com.unicom.urban.management.service.event;

import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.ProcessTimeLimit;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    @Autowired
    private ProcessTimeLimitService processTimeLimitService;

    public void reportEvent(String eventId) {
        /*TODO 查询所有有受理员角色的人*/
        List<String> userList = new ArrayList<>();
        userList.add("1");
        /*TODO 查询所有有监督员角色的人*/
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.reportEvent(eventId, userList, event.getEventSource().getId());
        event.setProcessInstanceId(processInstance.getId());


        eventService.update(event);
    }

    public Statistics initStatistics(Event event) {
        Statistics statistics = new Statistics();
        statistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        statistics.setTaskId(task.getId());
        statistics.setStarTime(LocalDateTime.now());
        statistics.setDeptTimeLimit(event.getTimeLimit());
        ProcessTimeLimit byTaskName = processTimeLimitService.findByTaskName(task.getName());
        statistics.setProcessTimeLimit(byTaskName);
        return statistics;
    }

}
