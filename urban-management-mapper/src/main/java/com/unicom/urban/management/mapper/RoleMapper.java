package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    UserDTO userToUserDTO(User user);

    List<RoleVO> roleListToRoleVOList(List<Role> roleList);

    RoleVO roleToRoleVO(Role role);

}
