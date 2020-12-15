package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
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
public interface EventConditionMapper {
    EventConditionMapper INSTANCE = Mappers.getMapper(EventConditionMapper.class);

    /**
     * list转换
     * @param eventConditionList from
     * @return list
     */
    List<EventConditionVO> eventConditionListToEventConditionVOList(List<EventCondition> eventConditionList);

    /**
     * 实体转换
     * @param eventCondition from
     * @return class
     */
    @Mapping(source = "eventType.name", target = "eventTypeName")
    @Mapping(source = "eventType.parent.name", target = "eventTypeParentName")
    EventConditionVO EventConditionToEventConditionVO(EventCondition eventCondition);
}
