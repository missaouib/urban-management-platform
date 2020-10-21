package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.EventTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-10:08
 */

@Mapper
public interface EventTypeMapper {
    EventTypeMapper INSTANCE = Mappers.getMapper(EventTypeMapper.class);

    /**
     * list转换
     * @param eventTypeList from
     * @return list
     */
    List<EventTypeVO> eventTypeVOToEventTypeVOList(List<EventType> eventTypeList);

    /**
     * 实体转换
     * @param eventType from
     * @return class
     */
    @Mapping(source = "parent.id", target = "parent")
    EventTypeVO EventTypeToEventTypeVO(EventType eventType);
}
