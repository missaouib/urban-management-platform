package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author jiangwen
 */
@Data
public class EventConditionDTO {

    @NotBlank(message = "父级分类不能为空")
    private String eventTypeId;

}
