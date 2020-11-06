package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.Statistics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 事件mapper
 *
 * @author jiangwen
 */
@Mapper
public interface StatisticsMapper {

    StatisticsMapper INSTANCE = Mappers.getMapper(StatisticsMapper.class);

    /**
     * 转
     *
     * @param statisticsDTO 实体
     * @return vo
     */
    Statistics StatisticsDTOToStatistics(StatisticsDTO statisticsDTO);

}
