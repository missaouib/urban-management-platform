package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.TimePlan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimePlanRepository extends CustomizeRepository<TimePlan, String> {

    Optional<TimePlan> getBySts(TimePlan.Status sts);

    List<TimePlan> findAllByStartTimeIsBeforeAndEndTimeIsAfterAndSts(LocalDateTime starTime,LocalDateTime endTime,TimePlan.Status sts);

}
