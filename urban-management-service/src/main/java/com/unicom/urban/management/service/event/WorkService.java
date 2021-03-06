package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import com.unicom.urban.management.service.role.RoleService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private TaskProcessingService taskProcessingService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptTimeLimitService deptTimeLimitService;

    public List<String> queryTaskByAssignee() {
        return activitiService.queryTask(SecurityUtil.getUserId());
    }

    public List<String> queryTaskByAssigneeAndTaskName(String userId,List<String> taskName) {
        return activitiService.queryTaskByAssigneeAndTaskName(userId, taskName);
    }
    public List<String> queryTaskByTaskName(String taskName) {
        return activitiService.queryTaskByTaskName(taskName);
    }

    /* ---------------------------------------------------------------------- */

    public void supervisor(String eventId, List<String> userId, String buttonId) {
        //todo eventId userList buttonId
        activitiService.complete(eventId, userId, buttonId);
        this.testFinish(eventId);
        this.initStatistics(eventId, 0);
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
        this.initStatistics(eventId, 0);
    }

    /**
     * 案件受理-核实
     *
     * @param eventDTO 事件
     */
    public void caseAcceptanceByReceive(EventDTO eventDTO) {
        this.acceptanceReportingByReceptionist(eventDTO.getId());
        eventDTO.setButton("11");
        this.completeByReceptionist(eventDTO);
    }

    /**
     * 案件受理-保存
     *
     * @param eventId 事件id
     */
    public void caseAcceptanceByDispatch(String eventId) {
        this.acceptanceReportingByReceptionist(eventId);
    }

    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核实
     *
     * @param eventDTO 事件
     */
    public void completeByReceptionist(EventDTO eventDTO) {
        Statistics statistics1 = statisticsService.findByEventIdAndEndTimeIsNull(eventDTO.getId());
        activitiService.claim(statistics1.getTaskId(), SecurityUtil.getUserId());
        statistics1.setEndTime(LocalDateTime.now());
        int[] map = taskProcessingService.betWeenTime(statistics1.getStartTime(), statistics1.getEndTime(), statistics1.getProcessTimeLimit().getTimeType().getId(), statistics1.getProcessTimeLimit().getTimeLimit(), 0);
        if ("16".equals(eventDTO.getButton())) {
            /* 按时发核查 */
            statistics1.setInTimeSendCheck(map[0]);
            statistics1.setSts(String.valueOf(map[1]));
            /* 标识人 */
            statistics1.setSendCheckHuman(SecurityUtil.getUser().castToUser());
            statistics1.setSendCheckHumanName(SecurityUtil.getUser().castToUser());
            statistics1.setUser(SecurityUtil.getUser().castToUser());
            Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
            statistics1.setSendCheckHumanRole(role);
            /* 附件 */
            setOpinionsAndEventFileList(statistics1, eventDTO.getRepresent(), eventDTO.getEventFileList());
        }
        if ("13".equals(eventDTO.getButton())) {
            /* 按时发核实 */
            statistics1.setInTimeSendVerify(map[0]);
            statistics1.setSts(String.valueOf(map[1]));
            /* 标识人 */
            statistics1.setSendVerifyHumanName(SecurityUtil.getUser().castToUser());
            statistics1.setSendVerifyHuman(SecurityUtil.getUser().castToUser());
            statistics1.setUser(SecurityUtil.getUser().castToUser());
            Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
            statistics1.setSendVerifyHumanRole(role);
            /* 附件 */
            setOpinionsAndEventFileList(statistics1, eventDTO.getRepresent(), eventDTO.getEventFileList());
        }
        if ("14".equals(eventDTO.getButton())) {
            /* 发核查 */
            statistics1.setSendCheckNum(1);
            /* 按时发核查 */
            statistics1.setInTimeSendCheck(map[0]);
            statistics1.setSts(String.valueOf(map[1]));
            /* 标识人 */
            statistics1.setSendCheckHuman(SecurityUtil.getUser().castToUser());
            statistics1.setSendCheckHumanName(SecurityUtil.getUser().castToUser());
            statistics1.setUser(SecurityUtil.getUser().castToUser());
            Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
            statistics1.setSendCheckHumanRole(role);
            /* 附件 */
            setOpinionsAndEventFileList(statistics1, eventDTO.getRepresent(), eventDTO.getEventFileList());
        }
        if ("11".equals(eventDTO.getButton())) {
            /* 发核实 */
            statistics1.setSendVerify(1);
            /* 按时发核实 */
            statistics1.setInTimeSendVerify(map[0]);
            statistics1.setSts(String.valueOf(map[1]));
            /* 标识人 */
            statistics1.setSendVerifyHumanName(SecurityUtil.getUser().castToUser());
            statistics1.setSendVerifyHuman(SecurityUtil.getUser().castToUser());
            statistics1.setUser(SecurityUtil.getUser().castToUser());
            Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
            statistics1.setSendVerifyHumanRole(role);
            /* 附件 */
            setOpinionsAndEventFileList(statistics1, null, eventDTO.getEventFileList());
        } else {
            setOpinionsAndEventFileList(statistics1, eventDTO.getRepresent(), eventDTO.getEventFileList());
        }
        statisticsService.update(statistics1);
        activitiService.complete(statistics1.getTaskId(), Collections.singletonList(eventDTO.getUserId()), eventDTO.getButton());
        Statistics statistics = initStatistics(eventDTO.getId(), statistics1.getSort());
        statistics.setStartTime(statistics1.getEndTime());
        statistics.setSort(statistics1.getSort() + 1);
        User user = new User(eventDTO.getUserId());
        if ("14".equals(eventDTO.getButton()) || "16".equals(eventDTO.getButton())) {
            /* 应核查 */
            statistics.setNeedCheck(1);
            /* 标识人 */
            statistics.setCheckPatrolName(user);
            statistics.setUser(user);
            Role role = new Role(KvConstant.SUPERVISOR_ROLE);
            statistics.setCheckPatrolId(role);
        }
        if ("11".equals(eventDTO.getButton()) || "13".equals(eventDTO.getButton())) {
            /* 应核实 */
            statistics.setNeedVerify(1);
            /* 标识人 */
            statistics.setVerifyPatrol(user);
            statistics.setVerifyPatrolName(user);
            statistics.setUser(user);
            Role role = new Role(KvConstant.SUPERVISOR_ROLE);
            statistics.setVerifyPatrolRole(role);
        }
        statisticsService.update(statistics);
    }

    /**
     * 监督员-信息核实
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByVerificationist(String eventId, String button) {
        Statistics saved = statisticsFinish(eventId);
        List<String> users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
        activitiService.complete(saved.getTaskId(), users, button);
        Event event = eventService.findOne(eventId);
        Statistics statistics = createStatistics(event);
        statistics.setSort(saved.getSort() + 1);
        statistics.setVerify(1);
        statistics.setInTimeVerify(1);
        statistics.setVerifyPatrolName(SecurityUtil.getUser().castToUser());
        statistics.setToOperate(1);
        statisticsService.save(statistics);
    }

    /**
     * 监督员-案件核查
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByInspect(String eventId, String button) {
        Statistics saved = statisticsFinish(eventId);
        List<String> users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
        activitiService.complete(saved.getTaskId(), users, button);
        Event event = eventService.findOne(eventId);
        Statistics statistics = createStatistics(event);
        statistics.setSort(saved.getSort() + 1);
        statistics.setCheckNum(1);
        statistics.setInTimeCheck(1);
        statistics.setCheckPatrolName(SecurityUtil.getUser().castToUser());
        statistics.setToCloseEvent(1);
        statisticsService.save(statistics);
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
        statistics.setUser(SecurityUtil.getUser().castToUser());
        Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
        statistics.setOperateRole(role);
        statisticsService.update(statistics);
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
    }

    /**
     * 受理员 核实反馈
     * 受理开启值班长流程
     *
     * @param eventDTO 事件
     */
    public void completeByReceptionistForDo(EventDTO eventDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventDTO.getId());
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setOperateHuman(SecurityUtil.getUser().castToUser());
        statistics.setOperateHumanName(SecurityUtil.getUser().castToUser());
        Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
        statistics.setOperateRole(role);
        statistics.setUser(SecurityUtil.getUser().castToUser());
        statistics.setEndTime(LocalDateTime.now());
        /* 受理数 */
        statistics.setOperate(1);
        int[] map = taskProcessingService.betWeenTime(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeType().getId(), statistics.getProcessTimeLimit().getTimeLimit(), 0);
        /* 按时受理 */
        statistics.setInTimeOperate(map[0]);
        statistics.setSts(String.valueOf(map[1]));
        /* 待受理 */
        statistics.setToOperate(0);
        setOpinionsAndEventFileList(statistics, eventDTO.getRepresent(), eventDTO.getEventFileList());
        statisticsService.update(statistics);
        List<String> userList;
        if ("17".equals(eventDTO.getButton())) {
            userList = taskProcessingService.getUsers(KvConstant.DISPATCHER_ROLE);
        } else if ("3".equals(eventDTO.getButton()) || "10".equals(eventDTO.getButton())) {
            userList = taskProcessingService.getUsers(KvConstant.SHIFT_LEADER_ROLE);
        } else {
            throw new DataValidException("此处步骤不合理");
        }
        activitiService.complete(statistics.getTaskId(), userList, eventDTO.getButton());
        Statistics statistics1 = this.initStatistics(eventDTO.getId(), statistics.getSort());
        if ("10".equals(eventDTO.getButton())) {
            /* 待结案 */
            statistics1.setCloseOperate(1);
            statistics1.setCloseInTimeOperate(1);
            statistics1.setToCloseEvent(0);
            statistics1.setToClose(1);
        }
        if ("3".equals(eventDTO.getButton())) {
            /* 待立案数 */
            statistics1.setToInst(1);
        }
        if ("17".equals(eventDTO.getButton())) {
            /* 待派遣 */
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
     * @param eventDTO 事件
     */
    public void completeByReceptionistForNotDo(EventDTO eventDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventDTO.getId());
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setEndTime(LocalDateTime.now());
        statistics.setOperateHuman(SecurityUtil.getUser().castToUser());
        statistics.setOperateHumanName(SecurityUtil.getUser().castToUser());
        statistics.setUser(SecurityUtil.getUser().castToUser());
        Role role = new Role(KvConstant.RECEPTIONIST_ROLE);
        statistics.setOperateRole(role);
        /* 待受理 */
        statistics.setToOperate(0);
        /* 不予受理 */
        statistics.setNotOperate(1);
        int[] map = taskProcessingService.betWeenTime(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeType().getId(), statistics.getProcessTimeLimit().getTimeLimit(), 0);
        /* 按时受理 */
        statistics.setInTimeOperate(map[0]);
        statistics.setSts(String.valueOf(map[1]));
        setOpinionsAndEventFileList(statistics, eventDTO.getRepresent(), eventDTO.getEventFileList());
        statisticsService.update(statistics);
        activitiService.complete(statistics.getTaskId(), null, eventDTO.getButton());
    }

    /**
     * 无效案件
     *
     * @param eventDTO 数据
     */
    public void completeForInvalidCases(EventDTO eventDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventDTO.getId());
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setEndTime(LocalDateTime.now());
        statistics.setInvalidEvent(1);
        int[] map = taskProcessingService.betWeenTime(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeType().getId(), statistics.getProcessTimeLimit().getTimeLimit(), 0);
        statistics.setSts(String.valueOf(map[1]));
        setOpinionsAndEventFileList(statistics, eventDTO.getRepresent(), eventDTO.getEventFileList());
        statisticsService.update(statistics);
        activitiService.complete(statistics.getTaskId(), null, eventDTO.getButton());
    }

    /**
     * 结案存档
     *
     * @param eventDTO 数据
     */
    public void completeForClosingAndFiling(EventDTO eventDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventDTO.getId());
        activitiService.claim(statistics.getTaskId(), SecurityUtil.getUserId());
        statistics.setEndTime(LocalDateTime.now());
        int[] map = taskProcessingService.betWeenTime(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeType().getId(), statistics.getProcessTimeLimit().getTimeLimit(), 0);
        statistics.setSts(String.valueOf(map[1]));
        setOpinionsAndEventFileList(statistics, eventDTO.getRepresent(), eventDTO.getEventFileList());
        statistics.setUser(SecurityUtil.getUser().castToUser());
        statisticsService.update(statistics);
        List<String> userList = taskProcessingService.getUsers(KvConstant.SHIFT_LEADER_ROLE);
        activitiService.complete(statistics.getTaskId(), userList, eventDTO.getButton());
        Statistics statistics1 = this.initStatistics(eventDTO.getId(), statistics.getSort());
    }


    /* -------------------------------------------------------------私有方法- */

    /**
     * 受理员上报
     *
     * @param eventId 上报的事件
     */
    private void acceptanceReportingByReceptionist(String eventId) {
        List<String> userList = taskProcessingService.getUsers(KvConstant.RECEPTIONIST_ROLE);
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.acceptanceReporting(eventId, userList);
        event.setProcessInstanceId(processInstance.getId());
        eventService.update(event);
        Statistics statistics = this.initStatistics(event.getId(), 0);
        /* 公众举报 */
        statistics.setPublicReport(1);
        /* 上报数 */
        statistics.setReport(1);
        /* 应发核实 */
        statistics.setNeedSendVerify(1);
        statisticsService.save(statistics);
    }

    /**
     * 测试期间 直接完结statistics表
     *
     * @param eventId 主键
     * @return 任务id
     */
    public String testFinish(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setEndTime(LocalDateTime.now());
        statisticsService.update(statistics);
        return statistics.getTaskId();
    }

    /**
     * 测试期间 直接完结statistics表
     *
     * @param eventId 主键
     * @return 环节
     */
    public Statistics statisticsFinish(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setEndTime(LocalDateTime.now());
        statisticsService.update(statistics);
        return statistics;
    }

    /**
     * 初始化实例
     *
     * @param eventId 事件id
     * @return 流转统计
     */
    public Statistics initStatistics(String eventId, int sort) {
        Event event = eventService.findOne(eventId);
        Statistics statistics = new Statistics();
        statistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        statistics.setTaskId(task.getId());
        statistics.setTaskName(task.getName());
        statistics.setStartTime(LocalDateTime.now());
        statistics.setDeptTimeLimit(event.getTimeLimit());
        DeptTimeLimit timeLimit = event.getTimeLimit();
        DeptTimeLimit one = deptTimeLimitService.findOne(timeLimit.getId());
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId(task.getName(), one.getLevel().getId());
        statistics.setProcessTimeLimit(processTimeLimit);
        statistics.setSort(sort + 1);
        return statisticsService.save(statistics);
    }

    /**
     * 初始化实例
     *
     * @param event 事件id
     * @return 流转统计
     */
    private Statistics createStatistics(Event event) {
        Statistics statistics = new Statistics();
        statistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        statistics.setTaskId(task.getId());
        statistics.setTaskName(task.getName());
        statistics.setStartTime(LocalDateTime.now());
        statistics.setDeptTimeLimit(event.getTimeLimit());
        /* 此处是目前数据库数据尚未完善 所以用一条假数据暂替 ——姜文 ——2020/11/10 */
        DeptTimeLimit one = deptTimeLimitService.findOne(event.getTimeLimit().getId());
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId(task.getName(), one.getLevel().getId());
        statistics.setProcessTimeLimit(processTimeLimit);
        return statistics;
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

    /**
     * 上报自处理事件
     *
     * @param eventId 主键
     */
    public void saveAutoReport(String eventId) {
        List<String> users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.reportAutoEvent(eventId, users);
        event.setProcessInstanceId(processInstance.getId());
        eventService.update(event);

        Statistics statistics = this.createStatistics(event);
        statistics.setNeedSendVerify(1);
        statistics.setReport(1);
        statistics.setPatrolReport(1);
        statistics.setToOperate(1);
        statistics.setSort(1);
        statistics.setReportPatrolName(SecurityUtil.getUser().castToUser());
        statisticsService.save(statistics);
    }

    /**
     * 上报事件
     *
     * @param eventId 主键
     */
    public void superviseReporting(String eventId) {
        List<String> users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
        Event event = eventService.findOne(eventId);
        ProcessInstance processInstance = activitiService.superviseReporting(eventId, users);
        event.setProcessInstanceId(processInstance.getId());
        eventService.update(event);
        Statistics statistics = this.createStatistics(event);
        statistics.setNeedSendVerify(1);
        statistics.setReport(1);
        statistics.setPatrolReport(1);
        statistics.setToOperate(1);
        statistics.setSort(1);
        statistics.setReportPatrolName(SecurityUtil.getUser().castToUser());
        statisticsService.save(statistics);
    }

    public List<String> getUsers(String roleId) {
        Role one = roleService.findOne(roleId);
        List<String> user = new ArrayList<>();
        one.getUserList().forEach(u -> user.add(u.getId()));
        return user;
    }
}
