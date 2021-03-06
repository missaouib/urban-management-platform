package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.common.constant.EventSourceConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.LocalDateTimeUtil;
import com.unicom.urban.management.dao.event.EventButtonRepository;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.entity.EventButton;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 封装activiti功能
 *
 * @author liukai
 */
@Slf4j
@Service
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private EventButtonRepository eventButtonRepository;

    @Autowired
    private TimePlanRepository timePlanRepository;

    @Autowired
    private DayRepository dayRepository;

    private final static String EVENT_KEY = "event";

    private ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
    }


    private ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    @Override
    public ProcessInstance reportEvent(String eventId, List<String> userList, String eventSource) {
        // 监督员上报
        if (EventSourceConstant.SUPERVISE_REPORTING.equals(eventSource)) {
            return superviseReporting(eventId, userList);
        }
        // 受理员上报
        if (EventSourceConstant.ACCEPTANCE_REPORTING.equals(eventSource)) {
            return acceptanceReporting(eventId, userList);
        }
        throw new BusinessException("非法事件来源: " + eventSource);
    }

    @Override
    public ProcessInstance superviseReporting(String eventId, List<String> userList) {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("userId", StringUtils.collectionToCommaDelimitedString(userList));
        variables.put("eventSource", EventSourceConstant.SUPERVISE_REPORTING);
        ProcessInstance processInstance = startProcessInstanceByKey(EVENT_KEY, eventId, variables);
        log.debug("----------------------监督员上报事件开始--------------------------------------");
        log.debug("----------------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("----------------------受理员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("----------------------监督员上报事件结束--------------------------------------");
        return processInstance;
    }

    @Override
    public ProcessInstance acceptanceReporting(String eventId, List<String> userList) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("userId", StringUtils.collectionToCommaDelimitedString(userList));
        variables.put("eventSource", EventSourceConstant.ACCEPTANCE_REPORTING);
        ProcessInstance processInstance = startProcessInstanceByKey(EVENT_KEY, eventId, variables);
        long endTime = System.currentTimeMillis();
        log.debug("----------------------受理员上报事件开始--------------------------------------");
        log.debug("----------------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("----------------------受理员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("----------------------受理员上报事件结束 activiti花费时间 {}:--------------------------------------", endTime - startTime);
        return processInstance;
    }

    @Override
    public ProcessInstance reportAutoEvent(String eventId, List<String> userList) {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("userId", StringUtils.collectionToCommaDelimitedString(userList));
        ProcessInstance processInstance = startProcessInstanceByKey("auto_event", eventId, variables);
        log.debug("----------------------自处理事件上报开始--------------------------------------");
        log.debug("----------------------上报事件 开启流程实例 eventId:{}---------------------", eventId);
        log.debug("----------------------受理员 userId:{}---------------------", Arrays.toString(userList.toArray()));
        log.debug("----------------------自处理事件上报结束--------------------------------------");
        return processInstance;
    }

    @Override
    public Task getTaskByProcessInstanceId(String processInstanceId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (CollectionUtils.isEmpty(taskList)) {
            log.error("task集合不应该为空");
            throw new BusinessException("任务流转出现异常");
        }
        return taskList.get(0);
    }

    @Override
    public List<String> queryGroupEvent(String userId, String taskId, Pageable pageable) {

        TaskQuery taskQuery = taskService.createTaskQuery();

        Task task = taskQuery.taskId(taskId).singleResult();

        List<Task> taskList = taskQuery.taskCandidateUser(userId).taskName(task.getName()).listPage(pageable.getPageNumber(), pageable.getPageSize());

        Set<String> taskIds = taskList.parallelStream().map(Task::getProcessDefinitionId).collect(Collectors.toSet());

        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(taskIds).list();

        return processInstanceList.parallelStream().map(ProcessInstance::getBusinessKey).collect(Collectors.toList());

    }

    @Override
    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

//    @Override
//    public List<String> queryTaskByAssignee(String userId, Pageable pageable) {
//        long startTime = System.currentTimeMillis();
//        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).listPage(pageable.getPageNumber(), pageable.getPageSize());
//
//        List<String> eventIdList = queryTask(taskList);
//        log.debug("----------------------activiti查询花费时间: {}ms--------------------------------------", System.currentTimeMillis() - startTime);
//        return eventIdList;
//
//    }

    @Override
    public List<String> queryTask(String userId) {
        long startTime = System.currentTimeMillis();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        List<String> eventIdList = queryTask(taskList);
        log.debug("----------------------activiti查询花费时间: {}ms--------------------------------------", System.currentTimeMillis() - startTime);
        return eventIdList;
    }

    @Override
    public List<String> queryTaskByTaskName(String taskName) {
        long startTime = System.currentTimeMillis();
        List<Task> list = taskService.createTaskQuery().taskName(taskName).list();
        List<String> eventIdList = queryTask(list);
        log.debug("----------------------activiti查询花费时间: {}ms--------------------------------------", System.currentTimeMillis() - startTime);
        return eventIdList;
    }

    @Override
    public List<String> queryTaskByAssigneeAndTaskName(String userId, List<String> taskName, Pageable pageable) {
        long startTime = System.currentTimeMillis();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).taskNameIn(taskName).listPage(pageable.getPageNumber(), pageable.getPageSize());
        List<String> eventIdList = queryTask(taskList);
        log.debug("----------------------activiti查询花费时间: {}ms--------------------------------------", System.currentTimeMillis() - startTime);
        return eventIdList;
    }

    @Override
    public List<String> queryTaskByAssigneeAndTaskName(String userId, List<String> taskName) {
        long startTime = System.currentTimeMillis();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userId).taskNameIn(taskName).list();
        List<String> eventIdList = queryTask(taskList);
        log.debug("----------------------activiti查询花费时间: {}ms--------------------------------------", System.currentTimeMillis() - startTime);
        return eventIdList;

    }

    private List<String> queryTask(List<Task> taskList) {
        if (CollectionUtils.isEmpty(taskList)) {
            return new ArrayList<>();
        }

        Set<String> taskIds = taskList.parallelStream().map(Task::getProcessInstanceId).collect(Collectors.toSet());

        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(taskIds).list();

        return processInstanceList.parallelStream().map(ProcessInstance::getBusinessKey).collect(Collectors.toList());
    }


    @Override
    public void claim(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (!StringUtils.isEmpty(task.getAssignee())) {
            throw new DataValidException("此事件已被他人领取");
        }
        taskService.claim(taskId, userId);
    }

    @Override
    public void unClaim(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        if (StringUtils) {
//
//        }
        taskService.unclaim(taskId);
    }

//    @Override
//    public void complete(String taskId) {
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        Map<String, Object> variables = new HashMap<>(3);
//
//        if ("核实反馈".equals(task.getName())) {
//            variables.put("", "");
//        }
//
//        if ("".equals(task.getName())) {
//            variables.put("", "");
//        }
//
//
//        taskService.complete(taskId, variables);
//
//    }

    @Override
    public void complete(String taskId, List<String> userList, String buttonId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new DataValidException("此事件已被他人领取");
        }
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("buttonId", buttonId);
        variables.put("userId", StringUtils.collectionToCommaDelimitedString(userList));
        taskService.complete(taskId, variables);
    }


    @Override
    public List<EventButton> queryButton(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return queryButtonList(task.getName());
    }

    private List<EventButton> queryButtonList(String taskName) {
        return eventButtonRepository.findByTaskNameOrderBySort(taskName);
    }

    @Override
    public Long intervalMinutes(LocalDateTime startTime, LocalDateTime endTime) {

        if (endTime.isBefore(startTime)) {
            throw new BusinessException("结束时间不可在开始时间之前 startTime: " + startTime + " endTime: " + endTime);
        }
        // 总共差了多少分钟
        long totalMinute = Duration.between(startTime, endTime).toMinutes();

        Optional<TimePlan> optionalTimePlan = timePlanRepository.getBySts(TimePlan.Status.ENABLE);
        if (optionalTimePlan.isPresent()) {
            TimePlan timePlan = optionalTimePlan.get();

            List<TimeScheme> timeSchemeList = timePlan.getTimeSchemeList();


            if (CollectionUtils.isEmpty(timeSchemeList)) {
                throw new DataValidException("未设置时间");
            }

            List<TempTime> tempTimeList = buildTempTimeList(timeSchemeList);
            // 总时间-休息时间
            return totalMinute - getMinute(startTime, endTime, tempTimeList, timePlan);
        } else {
            return totalMinute - defaultMethod(startTime, endTime);
        }


    }

    @Override
    public LocalDateTime addTime(LocalDateTime startDateTime, long minutes, boolean flag) {
        if (flag) {
            return startDateTime.plusMinutes(minutes);
        } else {
            Optional<TimePlan> optionalTimePlan = timePlanRepository.getBySts(TimePlan.Status.ENABLE);
            long seconds = minutes * 60;
            LocalTime localTime = null;
            LocalDateTime plusDays = startDateTime;
            // 系统中设置了可用的计时方案
            if (optionalTimePlan.isPresent()) {

                TimePlan timePlan = optionalTimePlan.get();

                List<TimeScheme> timeSchemeList = timePlan.getTimeSchemeList();

                List<Day> dayList = timePlan.getDayList();

                if (CollectionUtils.isEmpty(timeSchemeList)) {
                    throw new DataValidException("未设置时间");
                }

                List<TempTime> tempTimeList = buildTempTimeList(timeSchemeList);
                int index = -1;

                // 查询在哪个区间 index为区间下标
                for (int i = tempTimeList.size() - 1; i >= 0; i--) {
                    if ((startDateTime.toLocalTime().isAfter(tempTimeList.get(i).getStartTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getStartTime())) && (startDateTime.toLocalTime().isBefore(tempTimeList.get(i).getEndTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getEndTime()))) {
                        index = i;
                        break;
                    }
                }


                // 开始时间那天是上班的
                if (isWorkDay(startDateTime, dayList)) {
                    // 如果上班时间
                    if (tempTimeList.get(index).isFlag()) {
                        LocalTime endTime = tempTimeList.get(index).getEndTime();
                        long betweenSeconds = Duration.between(startDateTime.toLocalTime(), endTime).getSeconds();
                        if (betweenSeconds >= seconds) {
                            return startDateTime.plusSeconds(seconds);
                        } else {
                            seconds = seconds - betweenSeconds;
                            for (int i = index + 1; i < tempTimeList.size(); i++) {
                                if (tempTimeList.get(i).isFlag()) {
                                    if (seconds >= tempTimeList.get(i).getTotalMinute() * 60) {
                                        seconds = seconds - tempTimeList.get(i).getTotalMinute() * 60;
                                    } else {
                                        localTime = tempTimeList.get(i).getStartTime().plusSeconds(seconds);
                                        break;
                                    }
                                }
                                // 该下一天了
                                if (i == tempTimeList.size() - 1) {
                                    i = -1;
                                    do {
                                        plusDays = plusDays.plusDays(1);
                                    } while (isWeekDay(plusDays, dayList));

                                }
                            }
                            return LocalDateTime.of(plusDays.toLocalDate(), localTime);
                        }
                    } else {
                        for (int i = index + 1; i < tempTimeList.size(); i++) {
                            if (tempTimeList.get(i).isFlag()) {
                                if (seconds >= tempTimeList.get(i).getTotalMinute() * 60) {
                                    seconds = seconds - tempTimeList.get(i).getTotalMinute() * 60;
                                } else {
                                    localTime = tempTimeList.get(i).getStartTime().plusSeconds(seconds);
                                    break;
                                }
                            }
                            // 该下一天了
                            if (i == tempTimeList.size() - 1) {
                                i = -1;
                                do {
                                    plusDays = plusDays.plusDays(1);
                                } while (isWeekDay(plusDays, dayList));

                            }
                        }
                        return LocalDateTime.of(plusDays.toLocalDate(), localTime);

                    }
                } else {

                    while (isWeekDay(plusDays, dayList)) {
                        plusDays = plusDays.plusDays(1);
                    }

                    index = -1;
                    for (int i = index + 1; i < tempTimeList.size(); i++) {
                        if (tempTimeList.get(i).isFlag()) {
                            if (seconds >= tempTimeList.get(i).getTotalMinute() * 60) {
                                seconds = seconds - tempTimeList.get(i).getTotalMinute() * 60;
                            } else {
                                localTime = tempTimeList.get(i).getStartTime().plusSeconds(seconds);
                                break;
                            }
                        }
                        // 该下一天了
                        if (i == tempTimeList.size() - 1) {
                            i = -1;
                            do {
                                plusDays = plusDays.plusDays(1);
                            } while (isWeekDay(plusDays, dayList));

                        }
                    }
                    return LocalDateTime.of(plusDays.toLocalDate(), localTime);
                }
            } else {
                while (isWeekDay(plusDays)) {
                    plusDays = plusDays.plusDays(1);
                }
                return plusDays.plusMinutes(minutes);
            }
        }


    }

    /**
     * 默认的处理
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    private Long defaultMethod(LocalDateTime startTime, LocalDateTime endTime) {
        if (isSameDay(startTime, endTime)) {
            if (isWeekDay(startTime)) {
                return Duration.between(startTime, endTime).toMinutes();
            } else {
                return 0L;
            }
        } else {
            long totalMin = 0L;
            List<LocalDate> between = LocalDateTimeUtil.between(startTime.toLocalDate(), endTime.toLocalDate());

            if (LocalDateTimeUtil.isWeekDay(startTime.toLocalDate())) {
                totalMin = totalMin + Duration.between(startTime, startTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)).toMinutes();
            }

            for (int i = 1; i < between.size() - 1; i++) {
                if (LocalDateTimeUtil.isWeekDay(between.get(0))) {
                    totalMin = totalMin + LocalDateTimeUtil.ONE_DAY_MINUTE;
                }
            }

            if (LocalDateTimeUtil.isWeekDay(endTime.toLocalDate())) {
                totalMin = totalMin + Duration.between(endTime.plusDays(0).withHour(0).withMinute(0).withSecond(0).withNano(0), startTime).toMinutes();
            }

            return totalMin;

        }

    }

    /**
     * 判断是否休息
     */
    private boolean isWeekDay(LocalDateTime dateTime) {
        return LocalDateTimeUtil.isWeekDay(dateTime.toLocalDate());
    }

    /**
     * 判断是否休息
     */
    private boolean isWeekDay(LocalDateTime dateTime, List<Day> dayList) {
        Optional<Day> optionalDay = dayList.stream().filter(day -> day.getCalendar().equals(dateTime.toLocalDate())).findAny();
        if (optionalDay.isPresent()) {
            Day day = optionalDay.get();
            return day.isNotWork();
        }
        return isWeekDay(dateTime);
    }

    private boolean isWorkDay(LocalDateTime dateTime, List<Day> dayList) {
        return !isWeekDay(dateTime, dayList);
    }


    /**
     * 重新构建一个List 包含休息时间和工作时间
     */
    @Cacheable(value = "activiti_build_value")
    public List<TempTime> buildTempTimeList(List<TimeScheme> oldList) {
        // TODO 这里可能会有BUG
//        if (oldList.size() == 1) {
//            TempTime tempTime = new TempTime();
//            tempTime.setStartTime(oldList.get(0).getStartTime());
//            tempTime.setEndTime(oldList.get(0).getEndTime());
//            tempTime.setFlag(true);
//            newList.add(tempTime);
//            return newList;
//        }


        List<TempTime> newList1 = buildNewList(oldList, new ArrayList<TempTime>());

        List<TempTime> newList2 = new ArrayList<>();

        //补头
        newList2.add(TempTime.builder()
                .startTime(LocalTime.MIN)
                .endTime(oldList.get(0).getStartTime())
                .build());

        // 补尾
        newList2.add(TempTime.builder()
                .startTime(oldList.get(oldList.size() - 1).getEndTime())
                .endTime(LocalTime.MAX)
                .build());

        for (int i = 0; i < newList1.size(); i++) {
            if (i != newList1.size() - 1) {
                TempTime tempTime = new TempTime();
                tempTime.setStartTime(newList1.get(i).getEndTime());
                tempTime.setEndTime(newList1.get(i + 1).getStartTime());
                tempTime.setFlag(false);
                newList2.add(tempTime);
            }
        }

        newList1.addAll(newList2);

        for (TempTime tempTime : newList1) {
            tempTime.setTotalMinute(Duration.between(tempTime.getStartTime(), tempTime.getEndTime()).toMinutes());
        }

        return newList1.stream().sorted(Comparator.comparing(TempTime::getStartTime, Comparator.naturalOrder())).collect(Collectors.toList());
    }

    private List<TempTime> buildNewList(List<TimeScheme> oldList, List<TempTime> newList) {
        TempTime tempTime = null;

        int i = -1;
        do {
            i++;
            tempTime = new TempTime();
            tempTime.setStartTime(oldList.get(0).getStartTime());
            tempTime.setEndTime(oldList.get(i).getEndTime());
            tempTime.setFlag(true);

        } while (oldList.size() - 1 != i && oldList.get(i).getEndTime().equals(oldList.get(i + 1).getStartTime()));

        newList.add(tempTime);
        List<TimeScheme> newOldList = new ArrayList<>();
        for (int i1 = i + 1; i1 < oldList.size(); i1++) {
            newOldList.add(oldList.get(i1));

        }
        if (newOldList.size() > 0) {
            return buildNewList(newOldList, newList);
        } else {
            return newList;
        }
    }

    /**
     * 计算需要扣除多少分钟=总时间-休息日的总分钟-工作日的休息时间
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param tempTimeList  集合
     */
    private long getMinute(LocalDateTime startDateTime, LocalDateTime endDateTime, List<TempTime> tempTimeList, TimePlan timePlan) {
        // 开始日期和结束日期为同一天
        if (isSameDay(startDateTime, endDateTime)) {
            return oneDay(startDateTime, endDateTime, tempTimeList, timePlan);
        } else {
            List<LocalDate> localDateList = LocalDateTimeUtil.between(startDateTime, endDateTime);

            List<Day> dayList = dayRepository.findByCalendarInAndTimePlanOrderByCalendar(localDateList, timePlan);

            long startMin = 0;


//            for (Day day : dayList) {
//                boolean exists = localDateList.contains(day.getCalendar());
//                if (exists) {
//
//                }else{
//                    Day newDay = new Day();
//                    day.setCalendar();
//                }
//            }


            List<Day> days = new ArrayList<>();
            for (LocalDate localDate : localDateList) {
                boolean exists = dayList.stream().anyMatch(day -> day.getCalendar().equals(localDate));
                if (exists) {

                } else {
                    Day day = new Day();
                    day.setCalendar(localDate);
                    if (LocalDateTimeUtil.isWeekDay(localDate)) {
                        day.setWork(Day.Work.NON_WORK);
                    } else {
                        day.setWork(Day.Work.WORK);
                    }
                    days.add(day);
                }


            }

            dayList.addAll(days);

            dayList = dayList.stream().sorted(Comparator.comparing(Day::getCalendar)).collect(Collectors.toList());

            if (dayList.get(0).isWork()) {
                int startIndex = -1;

                for (int i = tempTimeList.size() - 1; i >= 0; i--) {
                    if ((startDateTime.toLocalTime().isAfter(tempTimeList.get(i).getStartTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getStartTime())) && (startDateTime.toLocalTime().isBefore(tempTimeList.get(i).getEndTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getEndTime()))) {
                        startIndex = i;
                        break;
                    }
                }
                TempTime startTempTime = tempTimeList.get(startIndex);


                if (startTempTime.isFlag()) {
                    startIndex = startIndex + 1;
                } else {
                    startMin = startMin + Duration.between(startDateTime.toLocalTime(), tempTimeList.get(startIndex).getEndTime()).toMinutes();
                    startIndex = startIndex + 1;
                }

                for (int i = startIndex; i < tempTimeList.size(); i++) {
                    if (!tempTimeList.get(i).isFlag()) {
                        startMin = startMin + tempTimeList.get(i).getTotalMinute();
                    }
                }

            } else {
                startMin = startMin + Duration.between(startDateTime.toLocalTime(), LocalTime.MAX).toMinutes();
            }

            long endMin = 0;

            if (dayList.get(dayList.size() - 1).isWork()) {

                int endIndex = -1;

                for (int i = 0; i < tempTimeList.size(); i++) {
                    if ((endDateTime.toLocalTime().isAfter(tempTimeList.get(i).getStartTime()) || endDateTime.toLocalTime().equals(tempTimeList.get(i).getStartTime())) && (endDateTime.toLocalTime().isBefore(tempTimeList.get(i).getEndTime()) || endDateTime.toLocalTime().equals(tempTimeList.get(i).getEndTime()))) {
                        endIndex = i;
                        break;
                    }
                }


                TempTime endTempTime = tempTimeList.get(endIndex);


                if (endTempTime.isFlag()) {
                    endIndex = endIndex - 1;
                } else {
                    endMin = endMin + Duration.between(tempTimeList.get(endIndex).getStartTime(), endDateTime.toLocalTime()).toMinutes();
                    endIndex = endIndex - 1;
                }


                for (int i = 0; i < endIndex; i++) {
                    if (!tempTimeList.get(i).isFlag()) {
                        endMin = endMin + tempTimeList.get(i).getTotalMinute();
                    }
                }

            } else {
                endMin = endMin + Duration.between(LocalTime.MIN, endDateTime.toLocalTime()).toMinutes();
            }


            long totalMinute = startMin + endMin;


            for (int i = 1; i < dayList.size() - 1; i++) {
                Day day = dayList.get(i);
                if (day.isWork()) {
                    totalMinute = totalMinute + getRestMinute(tempTimeList);
                } else {
                    totalMinute = totalMinute + LocalDateTimeUtil.ONE_DAY_MINUTE;
                }

            }


            return totalMinute;

        }

    }

    /**
     * 是否为同一天
     */
    private boolean isSameDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return LocalDateTimeUtil.isSameDay(startDateTime.toLocalDate(), endDateTime.toLocalDate());
    }

    /**
     * 计算一天当中休息多少分钟
     */
    private long getRestMinute(List<TempTime> tempTimeList) {
        long minute = 0;
        for (TempTime tempTime : tempTimeList) {
            if (!tempTime.isFlag()) {
                minute = minute + tempTime.getTotalMinute();
            }
        }
        return minute;
    }


    /**
     * 计算一天当中工作多少分钟
     */
    private long getWorkMinute(List<TempTime> tempTimeList) {
        long minute = 0;
        for (TempTime tempTime : tempTimeList) {
            if (tempTime.isFlag()) {
                minute = minute + tempTime.getTotalMinute();
            }
        }
        return minute;
    }

    private long oneDay(LocalDateTime startDateTime, LocalDateTime endDateTime, List<TempTime> tempTimeList, TimePlan timePlan) {
        Optional<Day> optionalDay = dayRepository.getByCalendarAndTimePlan(startDateTime.toLocalDate(), timePlan);
        if (optionalDay.isPresent()) {
            Day day = optionalDay.get();
            if (day.isWork()) {
                return extracted(startDateTime, endDateTime, tempTimeList);
            } else {
                return Duration.between(startDateTime, endDateTime).toMinutes();
            }

        } else {
            boolean weekDay = LocalDateTimeUtil.isWeekDay(startDateTime.toLocalDate());
            if (!weekDay) {
                return extracted(startDateTime, endDateTime, tempTimeList);
            } else {
                return Duration.between(startDateTime, endDateTime).toMinutes();
            }
        }
    }

    private long extracted(LocalDateTime startDateTime, LocalDateTime endDateTime, List<TempTime> tempTimeList) {
        int startIndex = -1;
        int endIndex = -1;
        for (int i = tempTimeList.size() - 1; i >= 0; i--) {
            if ((startDateTime.toLocalTime().isAfter(tempTimeList.get(i).getStartTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getStartTime())) && (startDateTime.toLocalTime().isBefore(tempTimeList.get(i).getEndTime()) || startDateTime.toLocalTime().equals(tempTimeList.get(i).getEndTime()))) {
                startIndex = i;
                break;
            }
        }
        for (int i = 0; i < tempTimeList.size(); i++) {
            if ((endDateTime.toLocalTime().isAfter(tempTimeList.get(i).getStartTime()) || endDateTime.toLocalTime().equals(tempTimeList.get(i).getStartTime())) && (endDateTime.toLocalTime().isBefore(tempTimeList.get(i).getEndTime()) || endDateTime.toLocalTime().equals(tempTimeList.get(i).getEndTime()))) {
                endIndex = i;
                break;
            }
        }
        // 在同一个区间里面
        if (startIndex == endIndex) {
            TempTime tempTime = tempTimeList.get(startIndex);
            // 上班区间
            if (tempTime.isFlag()) {
                return 0;
            } else {
                // 休息时间
                return Duration.between(startDateTime, endDateTime).toMinutes();
            }
        } else {
            TempTime startTempTime = tempTimeList.get(startIndex);
            TempTime endTempTime = tempTimeList.get(endIndex);
            long min = 0;
            if (startTempTime.isFlag()) {
                startIndex = startIndex + 1;
            } else {
                min = min + Duration.between(startDateTime.toLocalTime(), tempTimeList.get(startIndex).getEndTime()).toMinutes();
                startIndex = startIndex + 1;
            }
            if (endTempTime.isFlag()) {
                endIndex = endIndex - 1;
            } else {
                min = min + Duration.between(tempTimeList.get(endIndex).getStartTime(), endDateTime.toLocalTime()).toMinutes();
                endIndex = endIndex - 1;
            }
            for (int i = startIndex; i <= endIndex; i++) {
                if (!tempTimeList.get(i).isFlag()) {
                    min = min + tempTimeList.get(i).getTotalMinute();
                }
            }
            return min;
        }
    }


}
