package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * @author 顾志杰
 * @date 2020/10/14-10:07
 */
@Data
public class ComponentVO {

    private String id;

    private String eventTypeId;

    private String eventType;

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
    @ManyToOne
    private String bgid;
    private String bgname;

    /**
     * 部件状态
     */
    private String objState;
    private String objStateId;

    /**
     * 初始日期
     */
    private LocalDate initialDate;

    /**
     * 变更日期
     */
    private LocalDate changeDate;

    /**
     * 数据来源
     */
    private String  dataSource;
    private String  dataSourceId;

    private String note;
}
