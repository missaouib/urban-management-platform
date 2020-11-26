package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.LoginLog;
import com.unicom.urban.management.pojo.vo.LoginLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoginLogMapper {


    LoginLogMapper INSTANCE = Mappers.getMapper(LoginLogMapper.class);

    List<LoginLogVO> loginLogListToLoginLogVOList(List<LoginLog> loginLogList);



}
