package com.unicom.urban.management.pojo.dto;

import lombok.Data;

/**
 * 区域维护
 *
 * @author liubozhi
import javax.validation.constraints.NotBlank;

/**
 * @author jiangwen
 */
@Data
public class EventConditionDTO {

    String id;

    String region;
    @NotBlank(message = "父级分类不能为空")
    private String eventTypeId;

}
