package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends CustomizeRepository<Day, String> {

    List<Day> findByCalendarInAndTimePlanOrderByCalendar(List<LocalDate> localDateList, TimePlan timePlan);

    List<Day> findByTimePlanOrderByCalendar(TimePlan timePlan);

    Optional<Day> getByCalendar(LocalDate localDate);

    Page<Day> findByTimePlanOrderByCalendar(TimePlan timePlan, Pageable pageable);

    boolean existsByCalendarAndTimePlan(LocalDate calendar, TimePlan timePlan);

}
