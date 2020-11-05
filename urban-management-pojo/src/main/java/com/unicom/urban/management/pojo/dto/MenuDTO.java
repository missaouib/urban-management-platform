package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuDTO {

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @NotBlank(message = "菜单路径不能为空")
    private String path;

    private String parentId;


}
