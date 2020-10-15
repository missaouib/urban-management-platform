package com.unicom.urban.management.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 顾志杰
 * @date 2020/10/14-9:28
 */
@Data
public class ComponentDTO {

    private String id;

    private String componentTypeId;

    private String kvId;

    /**
     * 部件标识码
     */
    private String objId;

    /**
     * 部件名称
     */
    private String objName;

    /**
     * 主管部门代码
     */
    private String mainDeptCode;

    /**
     * 主管部门名称
     */
    private String mainDeptName;

    /**
     * 权属部门代码
     */
    private String ownershipDeptCode;

    /**
     * 权属部门名称
     */
    private String ownershipDeptName;

    /**
     * 养护部门代码
     */
    private String maintenanceDeptCode;

    /**
     * 养护部门名称
     */
    private String maintenanceDeptName;

    /**
     * 所在单元网格
     */
    private String bgid;

    /**
     * 部件状态
     */
    private String objState;

    /**
     * 初始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initialDate;

    /**
     * 变更日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changeDate;

    /**
     * 数据来源
     */
    private String  dataSource;

    /**
     * 备注
     */
    private String note;

    private String releaseName;

    private String coordinate;


}
