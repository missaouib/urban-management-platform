package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.DeptTimeLimit;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface DeptTimeLimitMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    /**
     * 转list
     *
     * @param deptTimeLimitList list
     * @return vo
     */
    List<DeptTimeLimitVO> eventListToEventVOList(List<DeptTimeLimit> deptTimeLimitList);

    /**
     * 转
     *
     * @param deptTimeLimit 实体
     * @return vo
     */
    DeptTimeLimitVO deptTimeLimitToDeptTimeLimitVO(DeptTimeLimit deptTimeLimit);
}
