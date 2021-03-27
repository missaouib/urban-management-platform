package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSchemeVO {

    private String id;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean check = true;

}
