package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.time.Day;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DayDTO {

    private String id;

    /**
     * 工作日还是节假日
     */
    private Day.WorkDayMark workDayMark;

    /**
     * 是否上班
     */
    private Day.Work work;


    private LocalDate calendar;

    private String timePlanId;


}
