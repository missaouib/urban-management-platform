package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserVO> userListToUserVOList(List<User> userList);

    @Mapping(source = "dept.id", target = "deptName")
    UserVO userToUserVO(User user);

}
