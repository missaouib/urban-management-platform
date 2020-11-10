package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.pojo.entity.ProcessTimeLimit;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private StatisticsService statisticsService;

    public List<String> queryTaskByAssignee() {
        return activitiService.queryTaskByAssignee(SecurityUtil.getUserId());
    }

    public List<String> queryTaskByAssigneeAndTaskName(List<String> taskName) {
        return activitiService.queryTaskByAssigneeAndTaskName(SecurityUtil.getUserId(), taskName);
    }

    /* ---------------------------------------------------------------------- */

    public void supervisor(String eventId, List<String> userId, String buttonId) {
        //todo eventId userList buttonId
        activitiService.complete(eventId, userId, buttonId);
        this.testFinish(eventId);
        this.initStatistics(eventId);
    }

    /**
     * 领取 -> 派遣
     *
     * @param eventId 事件id
     * @param userid
     */
    public void eventHandle(String eventId, List<String> userid, String buttonId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        activitiService.complete(statistics.getTaskId(), userid, buttonId);
        this.testFinish(eventId);
        this.initStatistics(eventId);
    }

    /**
     * 案件受理-核实
     *
     * @param eventId 事件id
     * @param userId  监督员
     */
    public void caseAcceptanceByReceive(String eventId, String userId) {
        this.acceptanceReportingByReceptionist(eventId);
        this.claimByReceptionist(eventId);
        this.completeByReceptionist(eventId, userId, "11");
    }

    /**
     * 案件受理-保存
     *
     * @param eventId 事件id
     */
    public void caseAcceptanceByDispatch(String eventId) {
        this.acceptanceReportingByReceptionist(eventId);
        this.claimByReceptionist(eventId);
    }

    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核实
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByReceptionist(String eventId, String userId, String button) {
        Statistics s = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        if ("13".equals(button) || "16".equals(button)) {
            activitiService.claim(s.getTaskId(), SecurityUtil.getUserId());
        }
        Map<String, Object> map = judgeOverTimeIsOrNot(s.getStartTime(), s.getEndTime(), s.getProcessTimeLimit().getTimeLimit());
        if ("14".equals(button)) {
            s.setSendCheckHuman(1);
            s.setInTimeCheck((Integer) map.get("time"));
            s.setSts(map.get("sts").toString());
        }
        if ("11".equals(button)) {
            /* 发核实 */
            s.setSendVerify(1);
            /* 按时发核实 */
            s.setInTimeSendVerify((Integer) map.get("time"));
            s.setSts(map.get("sts").toString());
            s.setSendVerifyHumanName(SecurityUtil.getUser().castToUser());
            s.setSendVerifyHuman(SecurityUtil.getUser().castToUser());
            /*TODO 获取当前登陆人角色*/
            s.setUser(SecurityUtil.getUser().castToUser());
        }
        s.setEndTime(LocalDateTime.now());
        statisticsService.update(s);
        activitiService.complete(s.getTaskId(), Collections.singletonList(userId), button);
        Statistics statistics = initStatistics(eventId);
        if ("14".equals(button)) {
            statistics.setNeedCheck(1);
        }
        if ("11".equals(button)) {
            /* 应核实 */
            statistics.setNeedVerify(1);
        }
        statisticsService.update(statistics);
        activitiService.claim(statistics.getTaskId(), userId);

    }

    /**
     * 监督员-信息核实
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByVerificationist(String eventId, String userId, String button) {
        String s = testFinish(eventId);
        activitiService.complete(s, Collections.singletonList("1"), button);
        initStatistics(eventId);
    }

    /**
     * 监督员-案件核查
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByInspect(String eventId, String userId, String button) {
        String s = testFinish(eventId);
        activitiService.complete(s, Collections.singletonList("1"), button);
        initStatistics(eventId);
    }

    /**
     * 受理员领取任务
     *
     * @param eventId 事件id
     */
    public void claimByReceptionist(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setOperateHuman(SecurityUtil.getUser().castToUser());
        statistics.setOperateHumanName(SecurityUtil.getUser().castToUser());
        /*TODO 获取当前登陆人的角色*/
        statistics.setUser(SecurityUtil.getUser().castToUser());
        statisticsService.update(statistics);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
    }

    /**
     * 受理员 核实反馈
     * 受理开启值班长流程
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByReceptionistForDo(String eventId, String button) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setEndTime(LocalDateTime.now());
        /* 受理数 */
        statistics.setOperate(1);
        Map<String, Object> map = judgeOverTimeIsOrNot(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeLimit());
        /* 按时受理 */
        statistics.setInTimeOperate((Integer) map.get("time"));
        statistics.setSts(map.get("sts").toString());
        /* 待受理 */
        statistics.setToOperate(0);
        statisticsService.update(statistics);
        /*TODO 查询所有值班长角色的人*/
        activitiService.complete(statistics.getTaskId(), Collections.singletonList("1"), button);
        Statistics statistics1 = this.initStatistics(eventId);
        if ("10".equals(button)) {
            /* 待结案 */
            statistics1.setToClose(1);
        }
        if ("3".equals(button)) {
            /* 待立案数 */
            statistics1.setToInst(1);
        }
        if ("17".equals(button)) {
            /* 带派遣 */
            statistics1.setToDispatch(1);
            /* 应派遣 */
            statistics1.setNeedDispatch(1);
        }
        statisticsService.update(statistics1);
    }

    /**
     * 受理员 核实反馈
     * 不予受理直接结束
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByReceptionistForNotDo(String eventId, String button) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setEndTime(LocalDateTime.now());
        /* 待受理 */
        statistics.setToOperate(0);
        /* 不予受理 */
        statistics.setNotOperate(1);
        Map<String, Object> map = judgeOverTimeIsOrNot(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeLimit());
        /* 按时受理 */
        statistics.setInTimeOperate((Integer) map.get("time"));
        statistics.setSts(map.get("sts").toString());
        statisticsService.update(statistics);
        activitiService.complete(statistics.getTaskId(), null, button);
    }

    /* -------------------------------------------------------------私有方法- */

    /**
     * 受理员上报
     *
     * @param eventId 上报的事件
     */
    private void acceptanceReportingByReceptionist(String eventId) {
        /*TODO 查询所有有受理员角色的人*/
        List<String> userList = new ArrayList<>();
        userList.add("1");
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.acceptanceReporting(eventId, userList);
        event.setProcessInstanceId(processInstance.getId());
        eventService.update(event);
        Statistics statistics = this.initStatistics(event.getId());
        /* 公众举报 */
        statistics.setPublicReport(1);
        /* 上报数 */
        statistics.setReport(1);
        /* 应发核实 */
        statistics.setNeedSendVerify(1);
        statisticsService.update(statistics);
    }

    /**
     * 测试期间 直接完结statistics表
     *
     * @param eventId
     * @return
     */
    public String testFinish(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setEndTime(LocalDateTime.now());
        statisticsService.update(statistics);
        return statistics.getTaskId();
    }

    /**
     * 初始化实例
     *
     * @param eventId 事件id
     * @return 流转统计
     */
    public Statistics initStatistics(String eventId) {
        Event event = eventService.findOne(eventId);
        Statistics statistics = new Statistics();
        statistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        statistics.setTaskId(task.getId());
        statistics.setTaskName(task.getName());
        statistics.setStartTime(LocalDateTime.now());
        statistics.setDeptTimeLimit(event.getTimeLimit());
        /*ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId(task.getName(), event.getTimeLimit().getId());*/
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId("值班长-立案", "28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        statistics.setProcessTimeLimit(processTimeLimit);
        return statisticsService.save(statistics);
    }

    /**
     * 为每条实例增加 意见 和 附件
     *
     * @param statistics    要修改的实例
     * @param opinions      意见
     * @param eventFileList 附件
     */
    private void setOpinionsAndEventFileList(Statistics statistics, String opinions, List<EventFile> eventFileList) {
        statistics.setOpinions(opinions);
        statistics.setEventFileList(eventFileList);
    }

    private Map<String, Object> judgeOverTimeIsOrNot(LocalDateTime starTime, LocalDateTime endTime, int processTimeLimit) {
        /* 计算出的实际完成任务时长 */
        Duration duration = Duration.between(starTime, endTime);
        /* 规定任务时限 processTimeLimit*/
        int two = 2;
        Map<String, Object> map = new HashMap<>(two);
        /* 超时 红灯 */
        if (duration.toDays() > processTimeLimit) {
            map.put("time", 0);
            map.put("sts", "0");
        } else {
            /* 超过80% */
            double v = processTimeLimit * 0.8;
            if (duration.toDays() > v) {
                /* 黄灯 */
                map.put("time", 1);
                map.put("sts", "1");
            } else {
                /* 按时 绿灯 */
                map.put("time", 1);
                map.put("sts", "2");
            }
        }
        return map;
    }

}
