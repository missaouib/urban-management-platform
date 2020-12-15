package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类别
 *
 * @author jiangwen
 */
@Data
public class EventTypeDTO {

    @NotBlank(message = "请选择分类")
    private String id;

    @NotBlank(message = "类别名称不能为空")
    private String name;

    private String parent;

    @NotBlank(message = "类别编码不能为空")
    private String code;

}
