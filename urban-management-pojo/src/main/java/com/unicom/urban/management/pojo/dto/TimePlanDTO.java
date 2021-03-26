package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.time.TimePlan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TimePlanDTO {

    private String id;

    private LocalDate startTime;

    private LocalDate endTime;

    private TimePlan.Status status;

}
