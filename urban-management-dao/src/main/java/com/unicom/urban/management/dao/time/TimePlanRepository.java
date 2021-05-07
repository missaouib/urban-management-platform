package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimePlanRepository extends CustomizeRepository<TimePlan, String> {

    Optional<TimePlan> getBySts(TimePlan.Status sts);

    List<TimePlan> findAllByStartTimeIsBeforeAndEndTimeIsAfterAndSts(LocalDate starTime, LocalDate endTime, TimePlan.Status sts);


    @Modifying
    @Query(value = "update TimePlan set sts = ?1")
    void updateStatus(TimePlan.Status status);

    @Modifying
    @Query(value = "update TimePlan set sts = ?1 where id = ?2")
    void updateStatus(TimePlan.Status status, String id);

    @Modifying
    @Query(value = "delete from TimePlan where id = ?1")
    void deleteById(String id);

}
