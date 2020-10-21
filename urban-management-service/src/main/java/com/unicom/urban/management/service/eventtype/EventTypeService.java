package com.unicom.urban.management.service.eventtype;

import com.unicom.urban.management.dao.eventtype.EventTypeRepository;
import com.unicom.urban.management.mapper.EventTypeMapper;
import com.unicom.urban.management.pojo.dto.ComponentTypeDTO;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.EventTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 顾志杰
 * @date 2020/10/14-13:49
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    /**
     * 查询所有部件分类
     *
     * @return list
     */
    public List<EventTypeVO> getEventTypeList() {
        List<EventType> fromList = eventTypeRepository.findAll();
        return EventTypeMapper.INSTANCE.eventTypeVOToEventTypeVOList(fromList);
    }

    public EventTypeVO getEventType(ComponentTypeDTO componentTypeDTO) {
        Optional<EventType> ifnull = eventTypeRepository.findById(componentTypeDTO.getId());
        if (ifnull.isPresent()) {
            EventType eventType = ifnull.get();
            return EventTypeMapper.INSTANCE.EventTypeToEventTypeVO(eventType);
        }
        throw new RuntimeException("没有该分类");
    }

    public EventType getEventType(String id) {
        return eventTypeRepository.findById(id).orElse(new EventType());
    }

    public List<String> getComponentTypeIds(String id) {
        Optional<EventType> ifnull = eventTypeRepository.findById(id);
        List<String> ids = new ArrayList<>();
        if (ifnull.isPresent()) {
            ids.add(id);
            this.getIds(ids, ifnull.get());
        }
        return ids;
    }

    private void getIds(List<String> ids, EventType eventType) {
        if (eventType.getChildren() != null) {
            eventType.getChildren().forEach(c -> {
                ids.add(c.getId());
                this.getIds(ids,c);
            });
        }
    }
}
