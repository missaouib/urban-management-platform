package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.DeptTimeLimit;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface DeptTimeLimitMapper {
    DeptTimeLimitMapper INSTANCE = Mappers.getMapper(DeptTimeLimitMapper.class);

    /**
     * 转list
     *
     * @param deptTimeLimitList list
     * @return vo
     */
    List<DeptTimeLimitVO> deptTimeLimitListTodeptTimeLimitVOList(List<DeptTimeLimit> deptTimeLimitList);

    /**
     * 转
     *
     * @param deptTimeLimit 实体
     * @return vo
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "level.value", target = "level"),
            @Mapping(source = "timeType.value", target = "timeType")
    })
    DeptTimeLimitVO deptTimeLimitToDeptTimeLimitVO(DeptTimeLimit deptTimeLimit);
}
