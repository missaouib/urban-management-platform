package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DayVo {

    private String id;

    /**
     * 工作日还是节假日
     */
    private Integer workDayMark;

    /**
     * 是否上班
     */
    private Integer work;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate calendar;


}
