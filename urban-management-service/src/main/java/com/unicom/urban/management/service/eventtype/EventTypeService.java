package com.unicom.urban.management.service.eventtype;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.eventtype.EventTypeRepository;
import com.unicom.urban.management.mapper.EventTypeMapper;
import com.unicom.urban.management.mapper.TreeMapper;
import com.unicom.urban.management.pojo.dto.ComponentTypeDTO;
import com.unicom.urban.management.pojo.dto.EventTypeDTO;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.EventTypeVO;
import com.unicom.urban.management.pojo.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    public List<EventTypeVO> getEventTypeList(int type) {
        List<EventType> fromList;
        if (KvConstant.COMPONENT_TYPE == type) {
            fromList = eventTypeRepository.findAllByType(KvConstant.COMPONENT_TYPE);
        } else {
            fromList = eventTypeRepository.findAll();
        }
        return EventTypeMapper.INSTANCE.eventTypeVOToEventTypeVOList(fromList);
    }

    public EventTypeVO getEventType(ComponentTypeDTO componentTypeDTO) {
        Optional<EventType> ifNull = eventTypeRepository.findById(componentTypeDTO.getId());
        if (ifNull.isPresent()) {
            EventType eventType = ifNull.get();
            return EventTypeMapper.INSTANCE.EventTypeToEventTypeVO(eventType);
        }
        throw new DataValidException("没有该分类");
    }

    public EventType getEventType(String id) {
        return eventTypeRepository.findById(id).orElse(new EventType());
    }

    public List<String> getComponentTypeIds(String id) {
        Optional<EventType> ifNull = eventTypeRepository.findById(id);
        List<String> ids = new ArrayList<>();
        if (ifNull.isPresent()) {
            ids.add(id);
            this.getIds(ids, ifNull.get());
        }
        return ids;
    }

    private void getIds(List<String> ids, EventType eventType) {
        if (eventType.getChildren() != null) {
            eventType.getChildren().forEach(c -> {
                ids.add(c.getId());
                this.getIds(ids, c);
            });
        }
    }

    @Cacheable(value = "eventTypeTree")
    public List<TreeVO> searchTree() {
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        return TreeMapper.INSTANCE.eventTypeListToTreeVOList(eventTypeList);
    }

    @Log(name = "类别配置-新增")
    public void saveEventType(EventTypeDTO eventTypeDTO) {
        EventType eventType = new EventType();
        eventType.setLevel("2");
        eventType.setCode(eventTypeDTO.getCode());
        eventType.setName(eventTypeDTO.getName());
        EventType one = eventTypeRepository.getOne(eventTypeDTO.getId());
        eventType.setParent(one);
        eventType.setType(one.getType());
        eventTypeRepository.save(eventType);
    }

    @Log(name = "类别配置-修改")
    public void updateEventType(EventTypeDTO eventTypeDTO) {
        EventType one = eventTypeRepository.getOne(eventTypeDTO.getId());
        one.setCode(eventTypeDTO.getCode());
        one.setName(eventTypeDTO.getName());
        eventTypeRepository.saveAndFlush(one);
    }

    @Log(name = "类别配置-删除")
    public void deleteEventType(String id) {
        eventTypeRepository.deleteById(id);
    }

    public List<EventTypeVO> getEventTypeTree() {
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        return EventTypeMapper.INSTANCE.eventTypeVOToEventTypeVOList(eventTypeList);
    }

    public EventType findOne(String id) {
        return eventTypeRepository.getOne(id);
    }

}
