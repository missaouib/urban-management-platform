package com.unicom.urban.management.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TimePlanDTO {

    private String id;

    private LocalDate startTime;

    private LocalDate endTime;

}
