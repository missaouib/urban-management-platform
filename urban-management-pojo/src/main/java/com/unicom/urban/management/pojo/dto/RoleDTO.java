package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色dto
 *
 * @author jiangwen
 */
@Data
public class RoleDTO {

    private String id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /**
     * 部门id
     */
    @NotBlank(message = "所在部门不能为空")
    private String deptId;

    /**
     * 描述
     */
    private String describes;

}
