package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.mapper.EventButtonMapper;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.mapper.EventMapper;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 事件
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventConditionRepository eventConditionRepository;
    @Autowired
    private WorkService workService;
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private DeptTimeLimitService deptTimeLimitService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private PetitionerService petitionerService;

    public Page<EventVO> search(EventDTO eventDTO, Pageable pageable) {
        Page<Event> page = eventRepository.findAll((Specification<Event>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotBlank(eventDTO.getUserName())) {
                list.add(criteriaBuilder.equal(root.get("user").get("username").as(String.class), eventDTO.getUserName()));
            }
            if (eventDTO.getSts() != null) {
                list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), eventDTO.getSts()));
            }

            if (StringUtils.isNotBlank(eventDTO.getRepresent())) {
                list.add(criteriaBuilder.like(root.get("represent").as(String.class), "%" + eventDTO.getRepresent() + "%"));
            }

            if (StringUtils.isNotBlank(eventDTO.getGrid())) {
                list.add(criteriaBuilder.equal(root.get("grid").get("id").as(String.class), eventDTO.getGrid()));
            }

            if (StringUtils.isNotBlank(eventDTO.getEventTypeId())) {
                list.add(criteriaBuilder.equal(root.get("eventType").get("id").as(String.class), eventDTO.getEventTypeId()));
            }

            if (StringUtils.isNotBlank(eventDTO.getConditionId())) {
                list.add(criteriaBuilder.equal(root.get("condition").get("id").as(String.class), eventDTO.getConditionId()));
            }

            if (StringUtils.isNotBlank(eventDTO.getEventSourceId())) {
                list.add(criteriaBuilder.equal(root.get("eventSource").get("id").as(String.class), eventDTO.getEventSourceId()));
            }

//            if (StringUtils.isNotBlank(eventDTO.getEventCondition())) {
//                list.add(criteriaBuilder.equal(root.get("condition").get("parent").get("id").as(String.class), eventDTO.getEventCondition()));
//            }

            if (StringUtils.isNotBlank(eventDTO.getTimeType())) {
                list.add(criteriaBuilder.equal(root.get("timeLimit").get("id").as(String.class), eventDTO.getTimeType()));
            }

            if (eventDTO.getTaskName() != null) {
                /* 查询当前登陆人所拥有的任务 */
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                List<String> type = workService.queryTaskByAssigneeAndTaskName(eventDTO.getTaskName());
                if (type.size() == 0) {
                    type = Collections.singletonList("");
                }
                type.forEach(in::value);
                list.add(in);
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<EventVO> eventVOList = EventMapper.INSTANCE.eventListToEventVOList(page.getContent());
//        setListDataByStatistics(eventVOList);
        return new PageImpl<>(eventVOList, page.getPageable(), page.getTotalElements());
    }

    private void setListDataByStatistics(List<EventVO> eventVOList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (EventVO eventVO : eventVOList) {
            Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventVO.getId());
            eventVO.setTaskName(statistics.getTaskName());
            eventVO.setStartTime(simpleDateFormat.format(statistics.getStartTime()));
            eventVO.setTimeLimit(statistics.getProcessTimeLimit().getTimeLimit());
        }

    }

    public List<EventConditionVO> findEventConditionByEventType(String eventTypeId) {
        List<EventCondition> list = eventConditionRepository.findAllByEventTypeId_IdAndTypeAndParentIsNull(eventTypeId, 1);
        if (list != null) {
            return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(list);
        }
        return null;
    }

    public List<EventConditionVO> findConditionValueByRegion(String region) {
        List<EventCondition> list = eventConditionRepository.findAllByParent_Id(region);
        if (list != null) {
            return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(list);
        }
        return null;
    }

    public List<DeptTimeLimitVO> findDeptTimeLimitByCondition(String conditionId) {
        return deptTimeLimitService.findDeptTimeLimitByCondition(conditionId);
    }

    public DeptTimeLimitVO findDeptTimeLimit(String deptTimeLimitId) {
        return deptTimeLimitService.findDeptTimeLimit(deptTimeLimitId);
    }

    /**
     * 获取按钮
     *
     * @param eventId 事件id
     * @return 按钮
     */
    public List<EventButtonVO> getButton(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        if (statistics != null) {
            List<EventButton> eventButtons = activitiService.queryButton(statistics.getTaskId());
            return EventButtonMapper.INSTANCE.eventButtonListToEventButtonVOList(eventButtons);
        } else {
            return null;
        }
    }


    /**
     * 保存事件
     *
     * @param eventDTO 事件参数
     */
    public void save(EventDTO eventDTO) {
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());

        Petitioner petitioner = new Petitioner();
        petitioner.setName(eventDTO.getPeopleName());
        petitioner.setSex(eventDTO.getSex());
        petitioner.setPhone(eventDTO.getPetitionerPhone());
        Petitioner saveP = petitionerService.save(petitioner);
        event.setPetitioner(saveP);
        Event save = eventRepository.save(event);
        /* 受理员核实 */
        if (eventDTO.getInitSts() != null && eventDTO.getInitSts() == 2) {
            workService.caseAcceptanceByDispatch(save.getId());
        }
        /* 受理员保存 */
        if (eventDTO.getInitSts() != null && eventDTO.getInitSts() == 3) {
            workService.caseAcceptanceByReceive(save.getId(), eventDTO.getUserId());
        }
    }
    /**
     * 上报自处理事件
     *
     * @param eventId  事件ID
     * @return 流程实例
     */
    public void reportAutoEvent(String eventId){
        workService.saveAutoReport(eventId);
    }
    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核实
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByReceptionist(String eventId, String userId, String button) {
        workService.completeByReceptionist(eventId, userId, button);
    }
    /**
     * 监督员信息核实
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByVerification(String eventId, String userId, String button) {
        workService.completeByVerificationist(eventId, userId, button);
    }
    /**
     * 监督员案件核查
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByInspect(String eventId, String userId, String button) {
        workService.completeByInspect(eventId, userId, button);
    }

    /**
     * 受理员 核实反馈
     * 受理开启值班长流程
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByReceptionistForDo(String eventId, String button) {
        workService.completeByReceptionistForDo(eventId, button);
    }

    /**
     * 受理员 核实反馈
     * 不予受理直接结束
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByReceptionistForNotDo(String eventId, String button) {
        workService.completeByReceptionistForNotDo(eventId, button);
    }

    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核查
     * 需要先领取任务
     *
     * @param eventId 事件id
     * @param userId  指派的人的id
     * @param button  按钮
     */
    public void completeByReceptionistWithClaim(String eventId, String userId, String button) {
        workService.claimByReceptionist(eventId);
        workService.completeByReceptionist(eventId, userId, button);
    }

    public String createCode(String eventTypeId) {
        String eventCode = "";
        //查询当天最大序号
        Integer maxNum = eventRepository.findMaxNum();
        EventType eventType = eventTypeService.getEventType(eventTypeId);
        String level = eventType.getLevel();
        String code = eventType.getCode();
        int type = eventType.getType();
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (level.length() == 1) {
            level = 0 + level;
        }
        if (code.length() == 1) {
            code = 0 + code;
        }
        //部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101
        String maxNumStr = "";
        if (maxNum == null) {
            maxNum = 0;
        }
        maxNum++;
        if (maxNum < 10) {
            maxNumStr = "000" + maxNum;
        } else if (maxNum < 100) {
            maxNumStr = "00" + maxNum;
        } else if (maxNum < 1000) {
            maxNumStr = "0" + maxNum;
        } else {
            maxNumStr = String.valueOf(maxNum);
        }
        if (type == 1) {
            eventCode = "C" + level + code + now + maxNumStr;
        } else {
            eventCode = "E" + level + code + now + maxNumStr;
        }
        return eventCode;
    }

    /**
     * findOne
     *
     * @param eventId 事件id
     * @return 事件
     */
    public Event findOne(String eventId) {
        return eventRepository.findById(eventId).orElse(new Event());
    }

    /**
     * findOne
     *
     * @param eventId 事件id
     * @return 事件
     */
    public EventOneVO findOneToVo(String eventId) {
        Event one = eventRepository.findById(eventId).orElse(new Event());
        EventOneVO eventOneVO = EventMapper.INSTANCE.eventToEventOneVO(one);
        eventOneVO.setEventTypeStr(one.getEventType().getParent().getParent().getName() + "-" + one.getEventType().getParent().getName() + "-" + one.getEventType().getName());
        eventOneVO.setTimeLimitStr(one.getTimeLimit().getTimeLimit() + one.getTimeLimit().getTimeType().getValue());
        eventOneVO.setCommunity(one.getGrid().getParent().getGridName());
        eventOneVO.setStreet(one.getGrid().getParent().getGridName());
        eventOneVO.setEventRegion(one.getGrid().getParent().getParent().getParent().getGridName());
        eventOneVO.setRegionStr(one.getCondition().getParent().getRegion());
        eventOneVO.setConditionValue(one.getCondition().getConditionValue());
        eventOneVO.setGirdStr(one.getGrid().getGridName());
        eventOneVO.setLevel(one.getTimeLimit().getLevel().getValue());
        eventOneVO.setEventButtonVOS(this.getButton(eventId));
        eventOneVO.setRecTypeStr(one.getRecType().getValue());
        eventOneVO.setUserName(Optional.ofNullable(one.getUser()).map(User::getName).orElse(""));
        Optional<Petitioner> petitioner = Optional.ofNullable(one.getPetitioner());
        eventOneVO.setPetitionerName(petitioner.map(Petitioner::getName).orElse(""));
        eventOneVO.setPetitionerPhone(petitioner.map(Petitioner::getPhone).orElse(""));
        eventOneVO.setPetitionerSex(petitioner.map(Petitioner::getSex).orElse(""));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventOneVO.setCDate(df.format(one.getCreateTime()));
        eventOneVO.setEventSourceStr(one.getEventSource().getValue());
        return eventOneVO;
    }

    /**
     * update
     *
     * @param event 需要操作的实体
     */
    public Event update(Event event) {
       return eventRepository.saveAndFlush(event);
    }

    public void saveAutoReport(EventDTO eventDTO) {
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        Event saved = eventRepository.save(event);
        Boolean doBySelf = eventDTO.getDoBySelf();
//        if (doBySelf != null) {
//            //自处理上报
//            if (doBySelf) {
//                this.reportAutoEvent(saved.getId());
//            }
//        }
    }
}
