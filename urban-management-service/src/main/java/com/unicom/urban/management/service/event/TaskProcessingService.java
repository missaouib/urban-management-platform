package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * 任务处理
 *
 * @author 顾志杰
 * @date 2020/11/4-10:24
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TaskProcessingService {

    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private WorkService workService;


    /**
     *值班长 立案 回退
     */
    public void shiftLeader(String eventId, List<String> userId, String buttonId){
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }

    /**
     * 派遣员-派遣 给专业部门
     */
    public void dispatcher(String eventId, List<String> userId, String buttonId){
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }

    /**
     * 专业部门 转核查 申请回退
     */
    public void professionalAgenc(String eventId, List<String> userId, String buttonId){
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }

}
