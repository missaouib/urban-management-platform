package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
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
    @Autowired
    private EventService eventService;


    /**
     * 任务处理
     */
    public void handle(String eventId, String roleId, String buttonId, StatisticsDTO statisticsDTO) {
        //todo 根据角色获取人员id
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        if ("值班长-立案".equals(statistics.getTaskName())) {
            this.shiftLeader(eventId, Collections.singletonList("1"), buttonId);
        } else if ("派遣员-派遣".equals(statistics.getTaskName())) {
            this.dispatcher(eventId, Collections.singletonList("1"), buttonId);
        } else if ("专业部门".equals(statistics.getTaskName())) {
            this.professionalAgenc(eventId, Collections.singletonList("1"), buttonId);
        }else if("值班长-结案".equals(statistics.getTaskName())){
            this.closeTask(eventId);
        }else {
            this.shiftLeader(eventId, Collections.singletonList("1"), buttonId);
        }


    }

    /**
     * 值班长 立案 回退
     */
    private void shiftLeader(String eventId, List<String> userId, String buttonId) {
        this.avtivitiHandle(eventId, userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }
    /**
     * 值班长 结案
     */
    private void closeTask(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), null, null);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
        Event one = eventService.findOne(eventId);
        KV kv = new KV();
        kv.setId(KvConstant.CLOS_ETESK);
        one.setEventSate(kv);
        eventService.update(one);
    }

    /**
     * 派遣员-派遣 给专业部门
     */
    private void dispatcher(String eventId, List<String> userId, String buttonId) {
        this.avtivitiHandle(eventId, userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }

    /**
     * 专业部门 转核查 申请回退
     */
    private void professionalAgenc(String eventId, List<String> userId, String buttonId) {
        this.avtivitiHandle(eventId, userId, buttonId);
        workService.testFinish(eventId);
        statisticsService.save(workService.initStatistics(eventId));
    }

    /**
     * 工作流处理 领取 -> 派发
     */
    private void avtivitiHandle(String eventId, List<String> userId, String buttonId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userId, buttonId);
    }


}
