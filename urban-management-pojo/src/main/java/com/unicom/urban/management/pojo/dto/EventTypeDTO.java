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

    private String id;

    @NotBlank(message = "类别名称不能为空")
    private String name;

    @NotBlank(message = "请选择父级分类")
    private String parent;

    @NotBlank(message = "类别编码不能为空")
    private String code;

}
