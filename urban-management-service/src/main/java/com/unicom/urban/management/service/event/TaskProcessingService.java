package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.dao.event.EventButtonRepository;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.eventfile.EventFileService;
import com.unicom.urban.management.service.processtimelimit.ProcessTimeLimitService;
import com.unicom.urban.management.service.role.RoleService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务处理
 *
 * @author 顾志杰
 * @date 2020/11/4-10:24
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class TaskProcessingService {

    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ProcessTimeLimitService processTimeLimitService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EventButtonRepository eventButtonRepository;
    @Autowired
    private EventFileService eventFileService;

    public List<String> findTaskNames() {
        List<EventButton> all = eventButtonRepository.findAll();
        List<String> buttonText = new ArrayList<>();
        all.forEach(b -> buttonText.add(b.getTaskName()));
        return buttonText.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 任务处理
     */
    public void handle(String eventId, String buttonId, StatisticsDTO statisticsDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        EventButton eventButton = eventButtonRepository.findById(buttonId).orElse(new EventButton());
        if ("值班长-立案".equals(statistics.getTaskName())) {
            List<String> users;
            if ("立案".equals(eventButton.getButtonText())) {
                users = this.getUsers(KvConstant.DISPATCHER_ROLE);
            } else {
                users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
            }

            this.shiftLeader(eventId, users, eventButton, statisticsDTO);
        } else if ("派遣员-派遣".equals(statistics.getTaskName())) {
            if ("申请作废".equals(eventButton.getButtonText())) {
                this.backOff(eventId, eventButton, statisticsDTO);
            } else {
                List<String> users = this.getUsers(statisticsDTO.getDeptId());
                this.dispatcher(eventId, users, buttonId, statisticsDTO);
            }

        } else if ("专业部门".equals(statistics.getTaskName())) {

            this.professionalAgenc(eventId, buttonId, statisticsDTO);
        } else if ("值班长-结案".equals(statistics.getTaskName())) {
            this.closeTask(eventId, statisticsDTO);
        } else if ("派遣员-延时审批".equals(statistics.getTaskName())) {
            this.delayedApproval(eventId, buttonId, statisticsDTO);
        } else if ("派遣员-回退审批".equals(statistics.getTaskName())) {
            this.backOff(eventId, eventButton, statisticsDTO);
        } else if ("值班长-作废审批".equals(statistics.getTaskName())) {
            this.toVoid(eventId, eventButton, statisticsDTO);
        } else if ("派遣员-挂账审批".equals(statistics.getTaskName())) {
            this.onAccount(eventId, eventButton, statisticsDTO);
        } else if ("挂账恢复".equals(statistics.getTaskName())) {
            this.recovery(eventId, eventButton, statisticsDTO);
        } else if ("自处理案件结案".equals(statistics.getTaskName())) {
            this.meCloseTask(eventId, eventButton, statisticsDTO);
        }

    }

    /**
     * 值班长 立案 回退
     */
    private void shiftLeader(String eventId, List<String> userId, EventButton buttonId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, userId, buttonId.getId());
        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setInst(1);
        int[] ints = this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit(), 0);
        statistics.setInTimeInst(ints[0]);
        statistics.setSts(String.valueOf(ints[1]));
        statistics.setToInst(0);
        statistics.setInstHuman(SecurityUtil.getUser().castToUser());
        statistics.setInstHumanName(SecurityUtil.getUser().castToUser());
        statistics.setInstRole(roleService.findOne(KvConstant.SHIFT_LEADER_ROLE));


        Event event = eventService.findOne(eventId);
        Statistics newStatistics = this.initStatistics(event);
        if ("立案".equals(buttonId.getButtonText())) {
            if (KvConstant.SUPERVISOR.equals(statistics.getEvent().getEventSource().getId())) {
                statistics.setValidPatrolReport(1);
            } else {
                statistics.setValidPublicReport(1);
            }
            statistics.setValidReport(1);
            statisticsService.update(statistics);
            newStatistics.setSort(statistics.getSort() + 1);
            newStatistics.setToDispatch(1);
            newStatistics.setNeedDispatch(1);
        } else {
            //回退 受理员
            newStatistics.setToOperate(1);
            newStatistics.setBackOff(1);
            newStatistics.setBackOffDate(LocalDateTime.now());
            newStatistics.setSort(statistics.getSort() + 1);

        }
        statisticsService.update(statistics);
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
        List<Statistics> total = statisticsService.findByEventIdToList(eventId).stream().filter(s -> "专业部门".equals(s.getTaskName())).collect(Collectors.toList());
        List<String> unitSize = total.stream().map(t -> t.getDisposeUnitName().getId()).collect(Collectors.toList());
        if (total.size() != unitSize.stream().distinct().count()) {
            statistics.setRework(1);
        }
        statistics.setClose(1);
        int[] ints = this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit(), 0);
        statistics.setInTimeClose(ints[0]);
        statistics.setSts(String.valueOf(ints[1]));
        statistics.setToClose(0);
        statistics.setCloseHuman(SecurityUtil.getUser().castToUser());
        statistics.setCloseHumanName(SecurityUtil.getUser().castToUser());
        statistics.setCloseRole(roleService.findOne(KvConstant.SHIFT_LEADER_ROLE));
        statisticsService.update(statistics);

    }

    /**
     * 自处理案件结案
     */
    private void meCloseTask(String eventId, EventButton eventButton, StatisticsDTO statisticsDTO) {

        if ("结案存档".equals(eventButton.getButtonText())) {
            this.avtivitiHandle(eventId, null, eventButton.getId());
            Event one = eventService.findOne(eventId);
            KV kv = new KV();
            kv.setId(KvConstant.CLOS_ETESK);
            one.setEventSate(kv);
            eventService.update(one);
            Statistics statistics = this.updateStatistics(statisticsDTO);
            statistics.setClose(1);
            statistics.setClosingFiling(1);
            int[] ints = this.betWeenTime(statistics.getStartTime(),
                    statistics.getEndTime(),
                    statistics.getProcessTimeLimit().getTimeType().getId(),
                    statistics.getProcessTimeLimit().getTimeLimit(), 0);
            statistics.setInTimeClose(ints[0]);
            statistics.setSts(String.valueOf(ints[1]));
            statistics.setToClose(0);
            statistics.setCloseHuman(SecurityUtil.getUser().castToUser());
            statistics.setCloseHumanName(SecurityUtil.getUser().castToUser());
            statistics.setCloseRole(roleService.findOne(KvConstant.SHIFT_LEADER_ROLE));
            statisticsService.update(statistics);
        } else {
            List<String> users = this.getUsers(KvConstant.RECEPTIONIST_ROLE);
            this.avtivitiHandle(eventId, users, eventButton.getId());
            Statistics statistics = this.updateStatistics(statisticsDTO);
            statisticsService.update(statistics);
            Event event = eventService.findOne(eventId);
            Statistics newStatistics = this.initStatistics(event);
            statisticsService.save(newStatistics);
        }


    }

    /**
     * 派遣员-派遣 给专业部门角色
     */
    private void dispatcher(String eventId, List<String> userId, String buttonId, StatisticsDTO statisticsDTO) {
        this.avtivitiHandle(eventId, userId, buttonId);
        Statistics statistics = this.updateStatistics(statisticsDTO);
        statistics.setDispatch(1);
        statistics.setToDispatch(0);
        int[] ints = this.betWeenTime(statistics.getStartTime(),
                statistics.getEndTime(),
                statistics.getProcessTimeLimit().getTimeType().getId(),
                statistics.getProcessTimeLimit().getTimeLimit(), 0);
        statistics.setInTimeDispatch(ints[0]);
        statistics.setSts(String.valueOf(ints[1]));
        statistics.setDispatchHuman(SecurityUtil.getUser().castToUser());
        statistics.setDispatchHumanName(SecurityUtil.getUser().castToUser());
        statistics.setDispatchHumanRole(roleService.findOne(KvConstant.DISPATCHER_ROLE));
        statisticsService.update(statistics);


        Event event = eventService.findOne(eventId);
        Statistics newStatistics = this.initStatistics(event);
        newStatistics.setNeedDispose(1);
        newStatistics.setToDispose(1);
        Role one = roleService.findOne(statisticsDTO.getDeptId());
        Dept dept = one.getDept();
        newStatistics.setDisposeUnit(dept);
        newStatistics.setDisposeUnitName(dept);
        newStatistics.setSort(statistics.getSort() + 1);
        statisticsService.save(newStatistics);
    }

    /**
     * 专业部门 转核查 申请回退
     */
    private void professionalAgenc(String eventId, String buttonId, StatisticsDTO statisticsDTO) {

        Statistics statistics;
        EventButton eventButton = eventButtonRepository.findById(buttonId).orElse(new EventButton());
        Event event = eventService.findOne(eventId);
        List<String> userId = new ArrayList<>();
        Statistics newStatistics;
        switch (eventButton.getButtonText()) {
            case "申请延时":
                if (statisticsDTO.getDelayedTime() == null) {
                    throw new DataValidException("延时时间不能为空");
                }
                userId.addAll(this.getUsers(KvConstant.DISPATCHER_ROLE));
                this.avtivitiHandle(eventId, userId, buttonId);
                newStatistics = this.initStatistics(event);
                statistics = this.updateStatistics(statisticsDTO);
                statistics.setNeedDispose(0);
                statistics.setToDispose(0);
                statistics.setDelayedHours(statisticsDTO.getDelayedTime());
                newStatistics.setSort(statistics.getSort() + 1);
                statisticsService.update(statistics);
                statisticsService.save(newStatistics);
                break;
            case "申请回退":
                userId.addAll(this.getUsers(KvConstant.DISPATCHER_ROLE));
                this.avtivitiHandle(eventId, userId, buttonId);
                statistics = this.updateStatistics(statisticsDTO);
                newStatistics = this.initStatistics(event);
                newStatistics.setBackOff(1);
                newStatistics.setBackOffDate(LocalDateTime.now());
                newStatistics.setSort(statistics.getSort() + 1);
                statisticsService.update(statistics);
                statisticsService.save(newStatistics);
                break;
            case "申请挂账":
                userId.addAll(this.getUsers(KvConstant.DISPATCHER_ROLE));
                this.avtivitiHandle(eventId, userId, buttonId);
                statistics = this.updateStatistics(statisticsDTO);
                newStatistics = this.initStatistics(event);
                newStatistics.setHang(1);
                newStatistics.setHangDate(LocalDateTime.now());
                newStatistics.setSort(statistics.getSort() + 1);
                statisticsService.update(statistics);
                statisticsService.save(newStatistics);
                break;
            case "转核查":
                userId.addAll(this.getUsers(KvConstant.RECEPTIONIST_ROLE));
                this.avtivitiHandle(eventId, userId, buttonId);
                newStatistics = this.initStatistics(event);
                statistics = this.updateStatistics(statisticsDTO);
                statistics.setDispose(1);
                statistics.setToDispose(0);
                int hangDuration = 0;
                if (statistics.getHangDuration() != null) {
                    hangDuration += statistics.getHangDuration() * 60 * 60 * 1000;
                }
                if (statistics.getDelayedHours() != 0) {
                    hangDuration += statistics.getDelayedHours() * 60 * 60 * 1000;
                }
                int[] num = this.betWeenTime(statistics.getStartTime(),
                        statistics.getEndTime(),
                        statistics.getDeptTimeLimit().getTimeType().getId(),
                        statistics.getDeptTimeLimit().getTimeLimit(), hangDuration);
                statistics.setInTimeDispose(num[0] == 1 ? 1 : 0);
                statistics.setOvertimeDispose(num[0] == 1 ? 0 : 1);
                statistics.setSts(String.valueOf(num[1]));
                newStatistics.setSort(statistics.getSort() + 1);
                newStatistics.setNeedSendCheck(1);
                statisticsService.update(statistics);
                statisticsService.save(newStatistics);
                break;
            default:
                throw new DataValidException("Read time out");
        }


    }

    /**
     * 挂账恢复
     */
    private void recovery(String eventId, EventButton buttonId, StatisticsDTO statisticsDTO) {
        Event one = eventService.findOne(eventId);
        List<Statistics> statisticsList = statisticsService.findByEventIdToList(eventId);
        List<Statistics> collect = statisticsList.stream().filter(s -> "专业部门".equals(s.getTaskName())).collect(Collectors.toList());
        if (collect.size() > 0) {
            Dept dept = collect.get(0).getDisposeUnit();
            List<String> users = this.getUsers(dept);
            this.avtivitiHandle(eventId, users, buttonId.getId());

            Statistics statistics = this.updateStatistics(statisticsDTO);
            statistics.setSpecialEndTime(LocalDateTime.now());
            statisticsService.update(statistics);

            Statistics newStatistics = this.copy(collect.get(0));
            Task task = activitiService.getTaskByProcessInstanceId(one.getProcessInstanceId());
            newStatistics.setTaskId(task.getId());
            newStatistics.setSpecialStartTime(statistics.getSpecialStartTime());
            newStatistics.setSpecialEndTime(statistics.getSpecialEndTime());
            newStatistics.setSort(statistics.getSort() + 1);
            Duration duration = Duration.between(statistics.getStartTime(), statistics.getEndTime());
            newStatistics.setHangDuration((int) duration.toHours());
            statisticsService.save(newStatistics);
        }
    }

    /**
     * 派遣员-延时审批
     */
    private void delayedApproval(String eventId, String buttonId, StatisticsDTO statisticsDTO) {
        List<Statistics> statisticsList = statisticsService.findByEventIdToList(eventId);
        List<Statistics> collect = statisticsList.stream().filter(s -> s.getDelayedHours() != null).filter(s -> "专业部门".equals(s.getTaskName())).collect(Collectors.toList());
        if (collect.size() > 0) {
            Dept dept = collect.get(0).getDisposeUnit();
            List<String> users = this.getUsers(dept);
            this.avtivitiHandle(eventId, users, buttonId);
            Statistics statistics = this.updateStatistics(statisticsDTO);
            Event event = eventService.findOne(eventId);
            Statistics newStatistics = this.initStatistics(event);
            EventButton eventButton = eventButtonRepository.findById(buttonId).orElse(new EventButton());
            statistics.setDispatchHuman(SecurityUtil.getUser().castToUser());
            statistics.setDispatchHumanName(SecurityUtil.getUser().castToUser());
            statistics.setDispatchHumanRole(roleService.findOne(KvConstant.DISPATCHER_ROLE));

            newStatistics.setNeedDispose(1);
            newStatistics.setToDispose(1);
            newStatistics.setDisposeUnit(dept);
            newStatistics.setDisposeUnitName(dept);
            newStatistics.setStartTime(collect.get(0).getStartTime());
            if ("通过".equals(eventButton.getButtonText())) {
                LocalDateTime now = LocalDateTime.now();
                statistics.setDelayedState(1);
                statistics.setDelayedDate(now);
                newStatistics.setDelayedHours(collect.get(0).getDelayedHours());
            }
            newStatistics.setSort(statistics.getSort() + 1);
            statisticsService.update(statistics);
            statisticsService.save(newStatistics);
        }

    }

    /**
     * 派遣员-挂账审批
     */
    private void onAccount(String eventId, EventButton buttonId, StatisticsDTO statisticsDTO) {
        List<Statistics> statisticsList = statisticsService.findByEventIdToList(eventId);
        List<Statistics> collect = statisticsList.stream().filter(s -> "专业部门".equals(s.getTaskName())).collect(Collectors.toList());
        if (collect.size() > 0) {
            Statistics oldStatistics = collect.get(0);
            Dept dept = collect.get(0).getDisposeUnit();
            List<String> users = this.getUsers(dept);
            this.avtivitiHandle(eventId, users, buttonId.getId());

            Statistics statistics = this.updateStatistics(statisticsDTO);
            statisticsService.update(statistics);

            Statistics newStatistics;
            Event event = eventService.findOne(eventId);
            if ("通过".equals(buttonId.getButtonText())) {
                newStatistics = this.initStatistics(event);
                newStatistics.setHang(1);
                newStatistics.setHangDate(LocalDateTime.now());
                newStatistics.setSpecialStartTime(LocalDateTime.now());
            } else {
                newStatistics = this.copy(oldStatistics);
                Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
                newStatistics.setTaskId(task.getId());
            }
            newStatistics.setSort(statistics.getSort() + 1);
            statisticsService.save(newStatistics);
        }

    }

    private Statistics copy(Statistics oldStatistics) {
        Statistics newStatistics = new Statistics();
        newStatistics.setNeedDispose(oldStatistics.getNeedDispose());
        newStatistics.setToDispose(oldStatistics.getToDispose());
        newStatistics.setDisposeUnit(oldStatistics.getDisposeUnit());
        newStatistics.setDisposeUnitName(oldStatistics.getDisposeUnitName());
        newStatistics.setEvent(oldStatistics.getEvent());
        newStatistics.setTaskName(oldStatistics.getTaskName());
        newStatistics.setStartTime(oldStatistics.getStartTime());
        newStatistics.setDeptTimeLimit(oldStatistics.getDeptTimeLimit());
        newStatistics.setProcessTimeLimit(oldStatistics.getProcessTimeLimit());
        return newStatistics;
    }

    /**
     * 派遣员-回退审批
     */
    private void backOff(String eventId, EventButton buttonId, StatisticsDTO statisticsDTO) {
        Event event = eventService.findOne(eventId);
        if ("通过".equals(buttonId.getButtonText())) {
            List<String> users = this.getUsers(KvConstant.DISPATCHER_ROLE);
            this.avtivitiHandle(eventId, users, buttonId.getId());
            Statistics statistics = this.updateStatistics(statisticsDTO);
            statistics.setBackOff(1);
            statistics.setBackOffDate(LocalDateTime.now());
            statisticsService.update(statistics);
            Statistics newStatistics = this.initStatistics(event);
            newStatistics.setToDispatch(1);
            newStatistics.setNeedDispatch(1);
            newStatistics.setSort(statistics.getSort() + 1);
            statisticsService.save(newStatistics);
        } else if ("不通过".equals(buttonId.getButtonText())) {

            List<Statistics> statisticsList = statisticsService.findByEventIdToList(eventId);
            List<Statistics> collect = statisticsList.stream().filter(s -> "专业部门".equals(s.getTaskName())).collect(Collectors.toList());
            if (collect.size() > 0) {

                Statistics oldStatistics = collect.get(0);
                List<String> users = this.getUsers(oldStatistics.getDisposeUnit());
                this.avtivitiHandle(eventId, users, buttonId.getId());


                Statistics statistics = this.updateStatistics(statisticsDTO);
                statisticsService.update(statistics);

                Statistics newStatistics = this.copy(oldStatistics);
                Task task = activitiService.getTaskByProcessInstanceId(event.getProcessInstanceId());
                newStatistics.setTaskId(task.getId());
                newStatistics.setSort(statistics.getSort() + 1);
                statisticsService.save(newStatistics);
            }
        } else {
            List<String> users = this.getUsers(KvConstant.SHIFT_LEADER_ROLE);
            this.avtivitiHandle(eventId, users, buttonId.getId());
            Statistics statistics = this.updateStatistics(statisticsDTO);
            statisticsService.update(statistics);
            Statistics newStatistics = this.initStatistics(event);
            newStatistics.setSort(statistics.getSort() + 1);
            statisticsService.save(newStatistics);
        }
    }

    /**
     * 值班长-作废审批
     */
    private void toVoid(String eventId, EventButton buttonId, StatisticsDTO statisticsDTO) {
        Event one = eventService.findOne(eventId);
        if ("通过".equals(buttonId.getButtonText())) {
            this.avtivitiHandle(eventId, null, buttonId.getId());
            KV kv = new KV();
            kv.setId(KvConstant.TO_VOID);
            one.setEventSate(kv);
            eventService.update(one);

            Statistics statistics = this.updateStatistics(statisticsDTO);
            statistics.setCancel(1);
            statistics.setCancelDate(LocalDateTime.now());
            statisticsService.update(statistics);
        } else {
            List<String> users = this.getUsers(KvConstant.DISPATCHER_ROLE);
            this.avtivitiHandle(eventId, users, buttonId.getId());
            Statistics statistics = this.updateStatistics(statisticsDTO);
            statisticsService.update(statistics);

            Statistics newStatistics = this.initStatistics(one);
            newStatistics.setBackOff(1);
            newStatistics.setBackOffDate(LocalDateTime.now());
            newStatistics.setSort(statistics.getSort() + 1);
            statisticsService.save(newStatistics);
        }
    }

    /**
     * 专业部门获取人员id列表
     */
    private List<String> getUsers(Dept dept) {
        List<String> users = new ArrayList<>();
        for (User user : dept.getUserList()) {
            for (Role role : user.getRoleList()) {
                if (KvConstant.PROFESSIONAL_DEPARTMENTS_ROLE.equals(role.getId())) {
                    users.add(user.getId());
                    break;
                }
            }
        }
        return users;
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
        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId(task.getName(), event.getTimeLimit().getLevel().getId());
//        ProcessTimeLimit processTimeLimit = processTimeLimitService.findByTaskNameAndLevelId("值班长-立案", "28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        newStatistics.setProcessTimeLimit(processTimeLimit);
        return newStatistics;
    }

    private Statistics updateStatistics(StatisticsDTO statisticsDTO) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(statisticsDTO.getEventId());

        if (statisticsDTO.getImageUrlList() != null) {
            List<EventFile> eventFileList = new ArrayList<>();
            List<EventFile> eventFileListImage = eventFileService.joinEventFileListToObjet(statisticsDTO.getImageUrlList(), EventFile.FileType.IMAGE);
            List<EventFile> eventFileListVideo = eventFileService.joinEventFileListToObjet(statisticsDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
            List<EventFile> eventFileListMusic = eventFileService.joinEventFileListToObjet(statisticsDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
            eventFileList.addAll(eventFileListImage);
            eventFileList.addAll(eventFileListVideo);
            eventFileList.addAll(eventFileListMusic);
            statistics.setEventFileList(eventFileList);
        }
        statistics.setUser(SecurityUtil.getUser().castToUser());
        statistics.setOpinions(statisticsDTO.getOpinions());
        LocalDateTime endTime = LocalDateTime.now();
        statistics.setEndTime(endTime);
        return statistics;
    }

    public int[] betWeenTime(LocalDateTime startTime, LocalDateTime endTime, String timeType, int timeLimit, int hangDuAction) {
        Long between1 = activitiService.intervalMinutes(startTime, endTime);
        long millis = between1 * 60 * 1000;
//        Duration between = Duration.between(startTime, endTime);
//        long millis = between.toMillis();
        switch (timeType) {
            case KvConstant.TASK_DAY:
                timeLimit = (timeLimit * 24 * 60 * 60 * 1000) + hangDuAction;
                break;
            case KvConstant.TASK_HOUR:
                timeLimit = (timeLimit * 60 * 60 * 1000) + hangDuAction;
                break;
            case KvConstant.TASK_MINUTE:
                timeLimit = (timeLimit * 60 * 1000) + hangDuAction;
                break;
            case KvConstant.DAY:
                timeLimit = (timeLimit * 24 * 60 * 60 * 1000) + hangDuAction;
                break;
            case KvConstant.HOUR:
                timeLimit = (timeLimit * 60 * 60 * 1000) + hangDuAction;
                break;
            case "工作日":
            case "天":
                timeLimit = (timeLimit * 24 * 60 * 60 * 1000) + hangDuAction;
                break;
            case "工作时":
            case "小时":
                timeLimit = (timeLimit * 60 * 60 * 1000) + hangDuAction;
                break;
            case "分钟":
                timeLimit = (timeLimit * 60 * 1000) + hangDuAction;
                break;
            default:
                return new int[]{0, 0};
        }
        int[] i = new int[2];
        i[0] = millis <= timeLimit ? 1 : 0;
        i[1] = millis <= timeLimit ? millis <= (timeLimit * 0.8) ? 2 : 1 : 0;
        return i;
    }


    public List<String> getUsers(String roleId) {
        Role one = roleService.findOne(roleId);
        if (one == null) {
            throw new DataValidException("角色不存在，请选择角色");
        }
        List<String> user = new ArrayList<>();
        one.getUserList().forEach(u -> user.add(u.getId()));
        if (user.size() == 0) {
            throw new DataValidException("角色下没有人员");
        }
        return user;
    }

}
