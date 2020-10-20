package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.vo.EventVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 事件mapper
 *
 * @author jiangwen
 */
@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    /**
     * 转list
     *
     * @param eventList list
     * @return vo
     */
    List<EventVO> eventListToEventVOList(List<Event> eventList);

    /**
     * 转
     *
     * @param event 实体
     * @return vo
     */
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "eventTypeId.eventTypeName", target = "eventTypeName")
    EventVO eventToEventVO(Event event);

}
