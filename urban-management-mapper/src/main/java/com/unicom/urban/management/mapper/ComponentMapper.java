package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.dto.ComponentInfoDTO;
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
            @Mapping(source = "componentInfo.initialDate", target = "initialDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "componentInfo.changeDate", target = "changeDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "componentInfo.dataSource.id", target = "dataSourceId"),
            @Mapping(source = "componentInfo.dataSource.value", target = "dataSource"),
            @Mapping(source = "componentInfo.note", target = "note"),
            @Mapping(source = "eventType.id", target = "eventTypeId"),
            @Mapping(source = "eventType.name", target = "eventType")
    })
    ComponentVO componentToComponentVO(Component component);


    @Mappings({
            @Mapping(source = "id",target = "componentId"),
            @Mapping(source = "objId",target = "objId"),
            @Mapping(source = "objName",target = "objName"),
            @Mapping(source = "mainDeptCode",target = "mainDeptCode"),
            @Mapping(source = "mainDeptName",target = "mainDeptName"),
            @Mapping(source = "ownershipDeptCode",target = "ownershipDeptCode"),
            @Mapping(source = "ownershipDeptName",target = "ownershipDeptName"),
            @Mapping(source = "maintenanceDeptCode",target = "maintenanceDeptCode"),
            @Mapping(source = "maintenanceDeptName",target = "maintenanceDeptName"),
            @Mapping(source = "bgid",target = "bgid"),
            @Mapping(source = "objStateId",target = "objState"),
            @Mapping(source = "initialDate",target = "initialDate"),
            @Mapping(source = "changeDate",target = "changeDate"),
            @Mapping(source = "dataSourceId",target = "dataSource"),
    })
    ComponentDTO componentInfoDTOToComponentDTO(ComponentInfoDTO componentInfoDTO);

    List<ComponentDTO> componentInfoDTOListToComponentDTOList(List<ComponentInfoDTO> componentInfoDTOList);
























}
