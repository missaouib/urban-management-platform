package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    UserDTO userToUserDTO(User user);

    List<MenuVO> menuListToMenuVOList(List<Menu> menuList);

    UserVO userToUserVO(User user);

}
