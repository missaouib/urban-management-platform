package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.TreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TreeMapper {

    TreeMapper INSTANCE = Mappers.getMapper(TreeMapper.class);


    List<TreeVO> eventTypeListToTreeVOList(List<EventType> eventTypeList);

    @Mapping(source = "parent.id", target = "parentId")
    TreeVO eventTypeToTreeVO(EventType eventType);

}
