package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.vo.EventOneVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
    @Mapping(source = "eventType.name", target = "eventTypeName")
    EventVO eventToEventVO(Event event);

    /**
     * 转
     *
     * @param eventDTO 实体
     * @return event
     */
    @Mapping(source = "userName", target = "user.name")
    @Mapping(source = "timeLimitId", target = "timeLimit.id")
    @Mapping(source = "eventTypeId", target = "eventType.id")
    @Mapping(source = "conditionId", target = "condition.id")
    @Mapping(source = "eventSourceId", target = "eventSource.id")
    @Mapping(source = "recTypeId", target = "recType.id")
    @Mapping(source = "gridId", target = "grid.id")
    @Mapping(source = "peopleName", target = "petitioner.name")
    @Mapping(source = "sex", target = "petitioner.sex")
    @Mapping(source = "petitionerPhone", target = "petitioner.phone")
    Event eventDTOToEvent(EventDTO eventDTO);



    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "processInstanceId",target = "processInstanceId"),
            @Mapping(source = "eventCode",target = "eventCode"),
            @Mapping(source = "represent",target = "represent"),
            @Mapping(source = "location",target = "location"),

    })
    EventOneVO eventToEventOneVO(Event event);

}
