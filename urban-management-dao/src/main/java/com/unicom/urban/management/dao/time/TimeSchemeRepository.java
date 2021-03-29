package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 顾志杰
 * @date 2021/3/23-9:01
 */
public interface TimeSchemeRepository extends CustomizeRepository<TimeScheme, String> {

    @Modifying
    @Query(value = "delete from TimeScheme where timePlan = ?1")
    void deleteByTimePlan(TimePlan timePlan);

}
