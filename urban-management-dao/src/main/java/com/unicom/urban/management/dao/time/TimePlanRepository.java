package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.TimePlan;

import java.util.Optional;

public interface TimePlanRepository extends CustomizeRepository<TimePlan, String> {

    Optional<TimePlan> getBySts(TimePlan.Status sts);

}
