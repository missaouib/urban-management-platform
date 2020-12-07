package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.pojo.Delete;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

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
@SQLDelete(sql = "update component_info set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Grid bgid;

    /**
     * 部件状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private KV objState;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private KV dataSource;

    /**
     * 备注
     */
    private String note;
}
