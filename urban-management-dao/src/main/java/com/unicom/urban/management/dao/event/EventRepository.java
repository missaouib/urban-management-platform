package com.unicom.urban.management.dao.event;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Event;
import org.springframework.data.jpa.repository.Query;

/**
 * 事件
 *
 * @author jiangwen
 */
public interface EventRepository extends CustomizeRepository<Event, String> {
    @Query(value = "SELECT max(substr(event_code,14,15)) FROM event WHERE date_format(create_time,'%y-%m-%d') = date_format(now(),'%y-%m-%d')", nativeQuery = true)
    long findMaxNum();

    @Query(value = "SELECT max(substr(event_code,14,15)) FROM event WHERE date_format(create_time,'%y-%m-%d') = date_format(now(),'%y-%m-%d')", nativeQuery = true)
    String findMaxNumNew();

    long countEventByConditionId(String conditionId);

    /**
     * 通过案件号验重
     *
     * @param eventCode 案卷号
     * @return 是否重复
     */
    boolean existsByEventCode(String eventCode);

    /**
     * 大屏 问题来源
     * 上报来源
     *
     * @param fieldName 字段名
     * @param tableName 表名
     * @param key key
     * @return 数量
     */
    long countEventByEventSource_FieldNameAndEventSource_TableNameAndEventSource_Key(String fieldName, String tableName, Integer key);
}
