package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import org.mapstruct.Mapper;
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
    ComponentVO componentToComponentVO(Component component);
}
