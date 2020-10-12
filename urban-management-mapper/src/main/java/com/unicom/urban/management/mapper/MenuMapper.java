package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    List<MenuVO> menuListToMenuVOList(List<Menu> menuList);

    @Mapping(source = "parent.id", target = "parentId")
    MenuVO menuToMenuVO(Menu menu);

}
