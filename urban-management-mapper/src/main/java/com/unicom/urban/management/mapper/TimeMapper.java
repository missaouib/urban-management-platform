package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.vo.TimeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TimeMapper {

    TimeMapper INSTANCE = Mappers.getMapper(TimeMapper.class);

    List<TimeVO> convertList(List<TimePlan> menuList);


    @Mappings({
            @Mapping(target = "status", expression = "java(timePlan.getSts().getValue())")
    })
    TimeVO convert(TimePlan timePlan);

}
