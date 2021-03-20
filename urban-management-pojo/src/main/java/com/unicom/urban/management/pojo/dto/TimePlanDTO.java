package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.time.TimePlan;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TimePlanDTO {

    private String id;


    private TimePlan.Status status;


}
