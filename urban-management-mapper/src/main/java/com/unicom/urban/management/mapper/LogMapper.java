package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.LoginLog;
import com.unicom.urban.management.pojo.entity.OperateLog;
import com.unicom.urban.management.pojo.vo.LoginLogVO;
import com.unicom.urban.management.pojo.vo.OperateLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LogMapper {


    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    List<LoginLogVO> loginLogListToLoginLogVOList(List<LoginLog> loginLogList);


    List<OperateLogVO> operateLogListToOperateLogVOList(List<OperateLog> operateLogList);


}
