package com.unicom.urban.management.service.eventcondition;

import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.pojo.dto.EventConditionDTO;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
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
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventConditionService {

    @Autowired
    private EventConditionRepository eventConditionRepository;
    @Autowired
    private EventTypeService eventTypeService;

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

}
