package com.unicom.urban.management.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author 顾志杰
 * @date 2020/10/14-14:06
 */
@Data
public class ComponentInfoDTO {

    private String id;



    /**
     * 部件标识码
     */
    @NotBlank(message = "部件标识码不能为空")
    private String objId;

    /**
     * 部件名称
     */
    @NotBlank(message = "部件名称不能为空")
    private String objName;

    /**
     * 主管部门代码
     */
    @NotBlank(message = "主管部门编码不能为空")
    private String mainDeptCode;

    /**
     * 主管部门名称
     */
    @NotBlank(message = "主管部门名称不能为空")
    private String mainDeptName;

    /**
     * 权属部门代码
     */
    @NotBlank(message = "权属部门编码不能为空")
    private String ownershipDeptCode;

    /**
     * 权属部门名称
     */
    @NotBlank(message = "权属部门名称不能为空")
    private String ownershipDeptName;

    /**
     * 养护部门代码
     */
    @NotBlank(message = "养护部门编码不能为空")
    private String maintenanceDeptCode;

    /**
     * 养护部门名称
     */
    @NotBlank(message = "养护部门名称不能为空")
    private String maintenanceDeptName;

    /**
     * 所在单元网格
     */
    @NotBlank(message = "所在单元网格不能为空")
    private String bgid;

    /**
     * 部件状态
     */
    @NotBlank(message = "部件状态不能为空")
    private String objStateId;

    /**
     * 初始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate initialDate;

    /**
     * 变更日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate changeDate;

    /**
     * 数据来源
     */
    @NotBlank(message = "数据来源不能为空")
    private String  dataSourceId;
}
