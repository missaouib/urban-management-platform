package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import com.unicom.urban.management.pojo.vo.DayVo;
import com.unicom.urban.management.pojo.vo.TimeSchemeVO;
import com.unicom.urban.management.pojo.vo.TimeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TimeMapper {

    TimeMapper INSTANCE = Mappers.getMapper(TimeMapper.class);

    List<TimeVO> convertList(List<TimePlan> timePlanList);


    @Mappings({
            @Mapping(target = "status", expression = "java(timePlan.getSts().getValue())")
    })
    TimeVO convert(TimePlan timePlan);

    List<DayVo> convertDayList(List<Day> dayList);

    @Mappings({
            @Mapping(target = "workDayMark", expression = "java(day.getWorkDayMark().getValue())"),
            @Mapping(target = "work", expression = "java(day.getWork().getValue())")
    })
    DayVo convert(Day day);

    List<TimeSchemeVO> convertSchemeList(List<TimeScheme> timeSchemeList);

}
