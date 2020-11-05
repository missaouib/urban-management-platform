package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.EventButton;
import com.unicom.urban.management.pojo.vo.EventButtonVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 事件mapper
 *
 * @author jiangwen
 */
@Mapper
public interface EventButtonMapper {

    EventButtonMapper INSTANCE = Mappers.getMapper(EventButtonMapper.class);

    /**
     * 转list
     *
     * @param eventButtonList list
     * @return vo
     */
    List<EventButtonVO> eventButtonListToEventButtonVOList(List<EventButton> eventButtonList);

    /**
     * 转
     *
     * @param eventButton 实体
     * @return vo
     */
    EventButtonVO eventButtonToEventButtonVO(EventButton eventButton);

}
