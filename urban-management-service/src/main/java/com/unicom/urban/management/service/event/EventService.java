package com.unicom.urban.management.service.event;

import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.mapper.EventMapper;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
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
    private EventConditionRepository eventConditionRespository;

    public Page<EventVO> search(EventDTO eventDTO, Pageable pageable) {
        Page<Event> page = eventRepository.findAll((Specification<Event>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotBlank(eventDTO.getUserName())) {
                list.add(criteriaBuilder.equal(root.get("user").get("username").as(String.class), eventDTO.getUserName()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<EventVO> eventVOList = EventMapper.INSTANCE.eventListToEventVOList(page.getContent());
        return new PageImpl<>(eventVOList, page.getPageable(), page.getTotalElements());
    }

    public List<EventConditionVO> findEventConditionByEventType(String eventTypeId) {
        List<EventCondition>  list = eventConditionRespository.findAllByEventTypeId_Id(eventTypeId);
        if (list != null){
            return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(list);
        }
        return null;
    }

    /**
     * 保存事件
     * @param event
     */
    public void save(Event event){
        eventRepository.save(event);
    }
}
