package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.entity.ComponentType;
import com.unicom.urban.management.pojo.vo.ComponentTypeVO;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-10:08
 */

@Mapper
public interface ComponentTypeMapper {
    ComponentTypeMapper INSTANCE = Mappers.getMapper(ComponentTypeMapper.class);

    /**
     * list转换
     * @param componentTypeList from
     * @return list
     */
    List<ComponentTypeVO> componentTypeListToComponentTypeVOList(List<ComponentType> componentTypeList);

    /**
     * 实体转换
     * @param componentType from
     * @return class
     */
    @Mapping(source = "parent.id", target = "parent")
    ComponentTypeVO componentTypeToComponentTypeVO(ComponentType componentType);
}
