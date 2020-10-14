package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModelDTO {

    @NotBlank(message = "名称不能为空")
    private String name;

    private String description;

}
