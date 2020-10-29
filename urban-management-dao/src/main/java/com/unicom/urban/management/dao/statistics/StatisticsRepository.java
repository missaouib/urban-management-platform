package com.unicom.urban.management.dao.statistics;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Statistics;

import java.util.List;

/**
 * 网格
 *
 * @author jiangwen
 */
public interface StatisticsRepository extends CustomizeRepository<Statistics, String> {

    List<Statistics> findAllByEvent_Id(String eventId);

}
