package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-10:08
 */

@Mapper
public interface ComponentMapper {
    ComponentMapper INSTANCE = Mappers.getMapper(ComponentMapper.class);

    /**
     * list转换
     * @param componentList from
     * @return list
     */
    List<ComponentVO> componentListToComponentVOList(List<Component> componentList);

    /**
     * 实体转换
     * @param component from
     * @return class
     */
    @Mappings({
            @Mapping(source = "componentInfo.objId", target = "objId"),
            @Mapping(source = "componentInfo.objName", target = "objName"),
            @Mapping(source = "componentInfo.mainDeptCode", target = "mainDeptCode"),
            @Mapping(source = "componentInfo.mainDeptName", target = "mainDeptName"),
            @Mapping(source = "componentInfo.ownershipDeptCode", target = "ownershipDeptCode"),
            @Mapping(source = "componentInfo.ownershipDeptName", target = "ownershipDeptName"),
            @Mapping(source = "componentInfo.maintenanceDeptCode", target = "maintenanceDeptCode"),
            @Mapping(source = "componentInfo.maintenanceDeptName", target = "maintenanceDeptName"),
            @Mapping(source = "componentInfo.bgid.id", target = "bgid"),
            @Mapping(source = "componentInfo.bgid.gridName", target = "bgname"),
            @Mapping(source = "componentInfo.objState.id", target = "objStateId"),
            @Mapping(source = "componentInfo.objState.value", target = "objState"),
            @Mapping(source = "componentInfo.initialDate", target = "initialDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "componentInfo.changeDate", target = "changeDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "componentInfo.dataSource.id", target = "dataSourceId"),
            @Mapping(source = "componentInfo.dataSource.value", target = "dataSource")
    })
    ComponentVO componentToComponentVO(Component component);
}
