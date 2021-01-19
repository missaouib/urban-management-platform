package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 顾志杰
 * @date 2020/12/7-16:46
 */
@Data
public class DeptDTO {

    private String id;

    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    private String describes;

    private String deptAddress;

    private String deptPhone;

    private String parentId;

    private String cdate;

    @NotBlank(message = "所属区域不能为空")
    private String gridId;

    private Integer sort;

    private String type;

    
    

}
