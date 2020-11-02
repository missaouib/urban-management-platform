package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 初始化实例
     *
     * @param event 事件
     * @return 流转统计
     */
    public Statistics initStatistics(Event event) {
        Statistics statistics = new Statistics();
        statistics.setEvent(event);
        /* 获取当前环节 */
        Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
        statistics.setTaskId(task.getId());
        statistics.setTaskName(task.getName());
        statistics.setStartTime(LocalDateTime.now());
        statistics.setDeptTimeLimit(event.getTimeLimit());
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskName(task.getName());
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
     * 监督员上报
     *
     * @param statistics 初始化出来的实例
     */
    private void supervisorReportForStatistics(Statistics statistics) {
        statistics.setPatrolReport(1);
        statistics.setReport(1);
        statistics.setEndTime(statistics.getStartTime());
        User user = new User();
        user.setId(SecurityUtil.getUserId());
        statistics.setReportPatrol(user);
        statistics.setReportPatrolName(user);
        /*TODO 获取当前登陆人的角色*/
        Role role = new Role();
        statistics.setReportPatrolRole(role);
        statistics.setUser(user);
    }

    /**
     * 受理员转批值班长 待受理
     *
     * @param statistics 初始化出来的实例
     */
    private void initStatisticsByReceptionistReport(Statistics statistics) {
        statistics.setStartTime(LocalDateTime.now());
        /* 待受理 */
        statistics.setToOperate(1);
        /*TODO 调用定时器 判断是否超时*/
    }

    /**
     * 受理员转批值班长/受理员上报 领取任务
     *
     * @param statistics 待受理的实例
     */
    private void checkStatisticsByReceptionistReport(Statistics statistics) {
        User user = new User();
        user.setId(SecurityUtil.getUserId());
        statistics.setOperateHuman(user);
        statistics.setOperateHumanName(user);
        /*TODO 获取当前登陆人的角色*/
        Role role = new Role();
        statistics.setOperateRole(role);
        statistics.setUser(user);
        /*TODO 调用工作流领取任务*/
    }

    /**
     * 受理/不予受理
     *
     * @param statistics 指定受理员接受的实例
     * @param buttonName 点击的按钮名称
     */
    private void judgeStatisticsByReceptionistReport(Statistics statistics, String buttonName, String opinions, List<EventFile> eventFileList) {
        setOpinionsAndEventFileList(statistics, opinions, eventFileList);
        statistics.setToOperate(0);
        statistics.setReport(1);
        statistics.setEndTime(LocalDateTime.now());
        if ("受理Id".equals(buttonName)) {
            statistics.setOperate(1);
        } else if ("不予受理Id".equals(buttonName)) {
            statistics.setNotOperate(1);
        } else {
            throw new DataValidException("请检查工作流流程");
        }
        Map<String, Object> map = judgeOverTimeIsOrNot(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeLimit());
        statistics.setInTimeOperate((Integer) map.get("time"));
        statistics.setSts(map.get("sts").toString());
    }

    /**
     * 受理员上报
     *
     * @param statistics 初始化的实例
     */
    private void receptionistReportForStatistics(Statistics statistics) {
        statistics.setPublicReport(1);
        statistics.setReport(1);
        statistics.setNeedSendVerify(1);
    }

    /**
     * 受理员上报 派核实
     *
     * @param statistics 待核实实例
     */
    private User receptionistSendVerify(Statistics statistics, User user, String opinions, List<EventFile> eventFileList) {
        setOpinionsAndEventFileList(statistics, opinions, eventFileList);
        statistics.setSendVerify(1);
        statistics.setNeedSendVerify(1);
        statistics.setEndTime(LocalDateTime.now());
        Map<String, Object> map = judgeOverTimeIsOrNot(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeLimit());
        statistics.setInTimeSendVerify((Integer) map.get("time"));
        statistics.setSts(map.get("sts").toString());
        return user;
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

    /**
     * 监督员核实反馈 初始化
     *
     * @param statistics 初始化的实例
     * @param user       被受理员派核实选中的监督员
     */
    private void initSupervisorVerification(Statistics statistics, User user) {
        statistics.setStartTime(LocalDateTime.now());
        statistics.setUser(user);
        statistics.setVerifyPatrol(user);
        statistics.setVerifyPatrolName(user);
        /*TODO 获取传入参数user的角色*/
        Role role = new Role();
        statistics.setVerifyPatrolRole(role);
        statistics.setNeedVerify(1);
    }

    /**
     * 监督员核实反馈 核实按钮
     *
     * @param statistics 初始化过，增加了受理员指定监督的实例
     */
    private void supervisorVerification(Statistics statistics, String opinions, List<EventFile> eventFileList) {
        setOpinionsAndEventFileList(statistics, opinions, eventFileList);
        statistics.setVerify(1);
        statistics.setNeedVerify(0);
        Map<String, Object> map = judgeOverTimeIsOrNot(statistics.getStartTime(), statistics.getEndTime(), statistics.getProcessTimeLimit().getTimeLimit());
        statistics.setInTimeVerify((Integer) map.get("time"));
        statistics.setSts(map.get("sts").toString());
    }

}
