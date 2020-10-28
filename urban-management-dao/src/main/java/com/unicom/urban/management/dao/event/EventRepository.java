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
    @Query(value = "SELECT max(substr(event_code,13,15)) FROM event WHERE date_format(create_time,'%y-%m-%d') = date_format(now(),'%y-%m-%d')", nativeQuery = true)
    Integer findMaxNum();
}
