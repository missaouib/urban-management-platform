package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.mapper.EventMapper;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventButton;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.pojo.vo.EventVO;
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

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private DeptTimeLimitService deptTimeLimitService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private StatisticsService statisticsService;

    public Page<EventVO> search(EventDTO eventDTO, Pageable pageable) {

        Page<Event> page = eventRepository.findAll((Specification<Event>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotBlank(eventDTO.getUserName())) {
                list.add(criteriaBuilder.equal(root.get("user").get("username").as(String.class), eventDTO.getUserName()));
            }
            if (eventDTO.getSts() != null) {
                list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), eventDTO.getSts()));
            }
            if (StringUtils.isNotBlank(eventDTO.getEventTypeId())) {
                list.add(criteriaBuilder.equal(root.get("eventType").get("id").as(String.class), eventDTO.getEventTypeId()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<EventVO> eventVOList = EventMapper.INSTANCE.eventListToEventVOList(page.getContent());
        return new PageImpl<>(eventVOList, page.getPageable(), page.getTotalElements());
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
    public List<EventButton> getbutton(String eventId) {
        //TODO 获取按钮
        return new ArrayList<>();
    }


    /**
     * 处理任务
     */
    public void chuli(String eventId, List<String> userId,String buttonId) {
        workService.eventHandle(eventId,userId,buttonId);
    }


    /**
     * 保存事件
     *
     * @param event
     */
    public void save(EventDTO eventDTO) {
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        Event save = eventRepository.save(event);
        /* 受理员上报 */
        if (save.getSts() == 2) {
            workService.acceptanceReportingByReceptionist(save.getId());
        }
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
        return eventRepository.getOne(eventId);
    }

    /**
     * update
     *
     * @param event 需要操作的实体
     */
    public void update(Event event) {
        eventRepository.saveAndFlush(event);
    }

}
