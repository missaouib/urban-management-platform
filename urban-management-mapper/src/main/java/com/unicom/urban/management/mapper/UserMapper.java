package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserVO> convertList(List<User> userList);


    @Mappings({
            @Mapping(target = "phone", expression = "java(user.getRawPhone())"),
            @Mapping(source = "dept.deptName", target = "deptName")
    })
    UserVO convert(User user);

}
