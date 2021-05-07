package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends CustomizeRepository<Day, String> {

    List<Day> findByCalendarInAndTimePlanOrderByCalendar(List<LocalDate> localDateList, TimePlan timePlan);

    List<Day> findByTimePlanOrderByCalendar(TimePlan timePlan);

    Optional<Day> getByCalendar(LocalDate localDate);

    Optional<Day> getByCalendarAndTimePlan(LocalDate localDate, TimePlan timePlan);

    Page<Day> findByTimePlanOrderByCalendar(TimePlan timePlan, Pageable pageable);

    boolean existsByCalendarAndTimePlan(LocalDate calendar, TimePlan timePlan);

    @Modifying
    @Query(value = "delete from Day where timePlan = ?1")
    void deleteByTimePlan(TimePlan timePlan);

    @Modifying
    @Query(value = "delete from Day where id = ?1")
    void deleteById(String id);

}
