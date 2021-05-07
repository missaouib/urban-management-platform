package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.entity.Grid;
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


    List<TreeVO> deptListToTreeVOList(List<Dept> deptList);

    @Mapping(source = "deptName", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "type",target = "dtoType")
    TreeVO deptToTreeVO(Dept dept);

    List<TreeVO> gridListToTreeVOList(List<Grid> gridList);

    @Mapping(source = "gridName", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    TreeVO gridToTreeVO(Grid grid);

}
