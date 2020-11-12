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

    List<Statistics> findAllByEvent_IdOrderByStartTimeDesc(String eventId);

    /**
     * 根据sort字段排序
     *
     * @param eventId 事件id
     * @return list
     */
    List<Statistics> findAllByEvent_IdOrderBySortDesc(String eventId);

    /**
     * 根据事件id查询该事件最后环节的实体
     * 事件id和结束时间为空
     *
     * @param eventId 事件id
     * @return 环节
     */
    Statistics findByEvent_IdAndEndTimeIsNull(String eventId);


    List<Statistics> findAllByUser_IdAndEndTimeIsNotNull(String userId);

    /**
     * 已挂账列表
     *
     * @param hang 1
     * @return list
     */
    List<Statistics> findAllByHang(Integer hang);

    /**
     * 已挂账列表
     *
     * @param cancel 1
     * @return list
     */
    List<Statistics> findAllByCancel(Integer cancel);

}
