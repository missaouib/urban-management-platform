package com.unicom.urban.management.dao.statistics;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
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

    List<Statistics> findByNotOperate(Integer notOperate);

    /**
     * 按期结案数
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.inTimeClose= 1 and g.id = ?1 ")
    List<Statistics> findAllByInTimeClose(String gridId);


    List<Statistics> findAllByEventInAndTaskNameAndDisposeUnit_Id(Collection<Event> event, String taskName, String disposeUnit_id);


}
