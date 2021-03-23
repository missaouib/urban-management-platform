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
    public Long between(LocalDateTime startTime, LocalDateTime endTime) {

        if (endTime.isBefore(startTime)) {
            throw new BusinessException("结束时间不可在开始时间之前");
        }


        // 总共差了多少分钟
        long totalMinute = Duration.between(startTime, endTime).toMinutes();

        Optional<TimePlan> optionalTimePlan = timePlanRepository.getBySts(TimePlan.Status.ENABLE);

        TimePlan timePlan = optionalTimePlan.orElseThrow(() -> new BusinessException("未找到可用的时间计划"));

        return totalMinute - getMinute(startTime, endTime, timePlan);

    }

    /**
     * 计算需要扣除多少分钟=总时间-休息日的总分钟-工作日的休息时间
     */
    private long getMinute(LocalDateTime startDateTime, LocalDateTime endDateTime, TimePlan timePlan) {
        // 开始日期和结束日期为同一天
        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            Optional<Day> optionalDay = dayRepository.getByCalendar(startDateTime.toLocalDate());
            if (optionalDay.isPresent()) {
                Day day = optionalDay.get();
                if (day.isWork()) {
                    List<TimeScheme> timeSchemeList = timePlan.getTimeSchemeList();
                    for (TimeScheme timeScheme : timeSchemeList) {

                    }

                } else {
                    return Duration.between(startDateTime, endDateTime).toMinutes();
                }
            } else {
                LocalDateTimeUtil.isWeekDay(startDateTime.toLocalDate());
            }

        }


        List<LocalDate> localDateList = LocalDateTimeUtil.between(startDateTime, endDateTime);


        // 周六周天的天数
        long weekDayCount = getWeekDay(localDateList);
        // 计算周六周天的总分钟
        long minute = weekDayCount * LocalDateTimeUtil.ONE_DAY_MINUTE;

        // 工作日的休息时间
        long workDayMinute = getWorkDayMinute(timePlan) * (localDateList.size() - weekDayCount);
        return minute + workDayMinute;

    }

    /**
     * 计算工作日中的休息时间
     *
     * @return 分钟
     */
    private long getWorkDayMinute(TimePlan timePlan) {
        List<TimeScheme> timeSchemeList = timePlan.getTimeSchemeList();
        long workMinute = 0L;
        for (TimeScheme timeScheme : timeSchemeList) {
            LocalTime startTime = timeScheme.getStartTime();
            LocalTime endTime = timeScheme.getEndTime();
            long minutes = Duration.between(startTime, endTime).toMinutes();
            workMinute = workMinute + minutes;
        }
        return LocalDateTimeUtil.ONE_DAY_MINUTE - workMinute;
    }

    /**
     * 计算经过了多少个周六周天
     *
     * @return 分钟
     */
    private long getWeekDay(List<LocalDate> localDateList) {
        List<Day> dayList = dayRepository.findByCalendarIn(localDateList);
        Map<String, LocalDate> map = new HashMap<>();
        int dayCount = 0;
        for (Day day : dayList) {
            if (day.isNotWork()) {
                map.put(day.getCalendar().toString(), day.getCalendar());
                dayCount = dayCount + 1;
            }
        }

        for (LocalDate localDate : localDateList) {
            // 如果为周六或周日
            if (LocalDateTimeUtil.isWeekDay(localDate)) {
                if (systemNoSet(map, localDate)) {
                    dayCount = dayCount + 1;
                }
            }
        }
        return dayCount;

    }

    private boolean systemNoSet(Map<String, LocalDate> map, LocalDate localDate) {
        return map.get(localDate.toString()) == null;
    }


}
