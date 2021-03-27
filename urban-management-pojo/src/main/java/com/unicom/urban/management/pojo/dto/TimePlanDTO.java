package com.unicom.urban.management.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Setter
@Getter
public class TimePlanDTO {

    private String id;

    @NotBlank(message = "名称不能为空")
    private String name;

    private LocalDate startTime;

    private LocalDate endTime;

}
