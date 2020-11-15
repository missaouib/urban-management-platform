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

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

}
