package com.unicom.urban.management.service.eventcondition;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.pojo.dto.EventConditionDTO;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
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
 * 区域维护Service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventConditionService {

    @Autowired
    private EventConditionRepository eventConditionRepository;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private EventService eventService;

    public List<EventConditionVO> allByEventTypeIdAndRegionIsNotNull(String eventTypeId) {
        List<EventCondition> eventConditionList = eventConditionRepository.findAllByEventType_IdAndRegionIsNotNull(eventTypeId);
        return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(eventConditionList);
    }

    public Page<EventConditionVO> search(EventConditionDTO eventConditionDTO, Pageable pageable) {
        Page<EventCondition> page = eventConditionRepository.findAll((Specification<EventCondition>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("eventType").get("id").as(String.class), eventConditionDTO.getEventTypeId()));
            list.add(criteriaBuilder.isNotNull(root.get("region").as(String.class)));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<EventConditionVO> eventConditionVOList = EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(page.getContent());
        EventType one = eventTypeService.findOne(eventConditionDTO.getEventTypeId());
        for (EventConditionVO eventConditionVO : eventConditionVOList) {
            eventConditionVO.setCategoryName(one.getParent().getName());
            eventConditionVO.setSubclassName(one.getName());
        }
        return new PageImpl<>(eventConditionVOList, page.getPageable(), page.getTotalElements());
    }


    public Page<EventConditionVO> search(EventConditionVO eventConditionVO, Pageable pageable) {
        Page<EventCondition> page = eventConditionRepository.findAll((Specification<EventCondition>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(idiomsVO.getIdiomsValue())) {
//                list.add(criteriaBuilder.like(root.get("idiomsVO").get("idioms_value").as(String.class), "%" + idiomsVO.getIdiomsValue() + "%"));
//
//            }
//            Predicate[] p = new Predicate[list.size()];
//            return criteriaBuilder.and(list.toArray(p));
            return null;
        }, pageable);
        List<EventConditionVO> eventConditionVOList = EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(page.getContent());
        return new PageImpl<>(eventConditionVOList, page.getPageable(), page.getTotalElements());
    }

    public void update(EventConditionDTO eventConditionDTO) {
        String id = eventConditionDTO.getId();
        EventCondition eventCondition = this.findOne(id);
        eventCondition.setRegion(eventConditionDTO.getRegion());
        eventConditionRepository.saveAndFlush(eventCondition);
    }

    public void del(String id) {
        long exitCondition = eventService.findAllByConditionId(id);
        if (exitCondition > 0) {
            throw new DataValidException("该区域下包含立案与结案条件，不能删除");
        } else {
            EventCondition eventCondition = new EventCondition();
            eventCondition.setId(id);
            eventConditionRepository.delete(eventCondition);
        }

    }

    public EventCondition findOne(String id) {
        return eventConditionRepository.findById(id).orElse(new EventCondition());
    }

    public void save(EventConditionDTO eventConditionDTO) {
        EventCondition eventCondition = new EventCondition();
        EventType eventType = new EventType();
        eventType.setId(eventConditionDTO.getEventTypeId());
        eventCondition.setEventType(eventType);
        eventCondition.setRegion(eventConditionDTO.getRegion());
        eventCondition.setType(1);
        eventConditionRepository.save(eventCondition);
    }
}
