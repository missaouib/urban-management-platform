package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.entity.ProcessTimeLimit;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
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
    @Autowired
    private ProcessTimeLimitService processTimeLimitService;


    /**
     * 任务处理
     */
    public void handle(String eventId, String roleId, String buttonId, StatisticsDTO statisticsDTO) {
        //todo 根据角色获取人员id
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        if ("值班长-立案".equals(statistics.getTaskName())) {
            this.shiftLeader(eventId, Collections.singletonList("1"), buttonId, statisticsDTO);
        } else if ("派遣员-派遣".equals(statistics.getTaskName())) {
            this.dispatcher(eventId, Collections.singletonList("1"), buttonId, statisticsDTO);
        } else if ("专业部门".equals(statistics.getTaskName())) {
            this.professionalAgenc(eventId, Collections.singletonList("1"), buttonId, statisticsDTO);
        } else if ("值班长-结案".equals(statistics.getTaskName())) {
            this.closeTask(eventId, statisticsDTO);
        }

    }

    /**
     * 值班长 立案 回退
     */
    private void shiftLeader(String eventId, List<String> userId, String buttonId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, userId, buttonId);
        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setEventFileList(statisticsDTO.getEventFileList());
        statistics.setInst(1);
        statistics.setInTimeInst(this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit()));
        statistics.setToInst(0);
        statistics.setInstHuman(SecurityUtil.getUser().castToUser());
        statistics.setInstHumanName(SecurityUtil.getUser().castToUser());
        statistics.setInstRole(null);
        if (KvConstant.SUPERVISOR.equals(statistics.getEvent().getEventSource().getId())) {
            statistics.setValidPatrolReport(1);
        } else {
            statistics.setValidPublicReport(1);
        }
        statistics.setValidReport(1);
        statisticsService.update(statistics);

        Event event = eventService.findOne(eventId);
        Statistics newStatistics = this.initStatistics(event);
        newStatistics.setToDispatch(1);
        newStatistics.setNeedDispatch(1);
        statisticsService.save(newStatistics);
    }


    /**
     * 值班长 结案
     */
    private void closeTask(String eventId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, null, null);

        Event one = eventService.findOne(eventId);
        KV kv = new KV();
        kv.setId(KvConstant.CLOS_ETESK);
        one.setEventSate(kv);
        eventService.update(one);

        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setClose(1);
        statistics.setInTimeClose(this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit()));
        statistics.setToClose(0);
        statistics.setCloseHuman(SecurityUtil.getUser().castToUser());
        statistics.setCloseHumanName(SecurityUtil.getUser().castToUser());
        statistics.setCloseRole(null);
        statisticsService.update(statistics);

    }

    /**
     * 派遣员-派遣 给专业部门
     */
    private void dispatcher(String eventId, List<String> userId, String buttonId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, userId, buttonId);
        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setDispatch(1);
        statistics.setToDispatch(0);
        statistics.setInTimeDispatch(this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit()));
        statistics.setDispatchHuman(SecurityUtil.getUser().castToUser());
        statistics.setDispatchHumanName(SecurityUtil.getUser().castToUser());
        statistics.setCloseRole(null);
        statisticsService.update(statistics);


        Event event = eventService.findOne(eventId);
        Statistics newStatistics = this.initStatistics(event);
        newStatistics.setNeedDispose(1);
        newStatistics.setToDispose(1);
        //todo 部门标识
        statisticsService.save(newStatistics);
    }

    /**
     * 专业部门 转核查 申请回退
     */
    private void professionalAgenc(String eventId, List<String> userId, String buttonId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, userId, buttonId);
        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setDispose(1);
        statistics.setToDispose(0);
        int num = this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit());
        statistics.setInTimeDispose(num == 1 ? 1 : 0);
        statistics.setOvertimeDispose(num == 1 ? 0 : 1);
        statisticsService.update(statistics);


        Event event = eventService.findOne(eventId);
        Statistics newStatistics = this.initStatistics(event);
        statisticsService.save(newStatistics);
    }

    /**
     * 工作流处理 领取 -> 派发
     */
    private void avtivitiHandle(String eventId, List<String> userId, String buttonId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userId, buttonId);
    }


    private Statistics initStatistics(Event event) {
        Statistics newStatistics = new Statistics();
        newStatistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        newStatistics.setTaskId(task.getId());
        newStatistics.setTaskName(task.getName());
        newStatistics.setStartTime(LocalDateTime.now());
        newStatistics.setDeptTimeLimit(event.getTimeLimit());
        /* 此处是目前数据库数据尚未完善 所以用一条假数据暂替 ——姜文 ——2020/11/10 */
        /*ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId(task.getName(), event.getTimeLimit().getId());*/
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId("值班长-立案", "28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        newStatistics.setProcessTimeLimit(processTimeLimit);
        return newStatistics;
    }

    private Statistics updateStatistics(StatisticsDTO statisticsDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(statisticsDTO.getEventId());
        statistics.setOpinions(statisticsDTO.getOpinions());
        LocalDateTime endTime = LocalDateTime.now();
        statistics.setEndTime(endTime);
        return statistics;
    }

    private int betWeenTime(LocalDateTime startTime, LocalDateTime endTime, String timeType, int timeLimit) {
        Duration between = Duration.between(startTime, endTime);
        long millis = between.toMillis();
        switch (timeType) {
            case KvConstant.TASK_DAY:
                timeLimit = timeLimit * 24 * 60 * 60 * 1000;
                break;
            case KvConstant.TASK_HOUR:
                timeLimit = timeLimit * 60 * 60 * 1000;
                break;
            case KvConstant.TASK_MINUTE:
                timeLimit = timeLimit * 60 * 1000;
                break;
        }
        return millis <= timeLimit ? 1 : 0;
    }


}
