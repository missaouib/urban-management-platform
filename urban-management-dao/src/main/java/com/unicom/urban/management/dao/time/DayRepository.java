package com.unicom.urban.management.dao.time;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.time.Day;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends CustomizeRepository<Day, String> {

    List<Day> findByCalendarIn(List<LocalDate> localDateList);

    Optional<Day> getByCalendar(LocalDate localDate);

}
