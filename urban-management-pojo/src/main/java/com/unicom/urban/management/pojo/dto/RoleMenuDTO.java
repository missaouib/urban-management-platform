package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色dto
 *
 * @author liubozhi
 */
@Data
public class RoleMenuDTO {

    private String id;
    /**
     * 菜单id集合
     */
    private String[] menuIdList;

}
