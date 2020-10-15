package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * 部件信息
 *
 * @author 顾志杰
 * @date 2020/10/13-19:04
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ComponentInfo extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

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
    private Grid bgid;

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
}
