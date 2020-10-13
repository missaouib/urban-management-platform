package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 部件信息
 *
 * @author 顾志杰
 * @date 2020/10/13-19:04
 */
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
    private String deptCode1;

    /**
     * 主管部门名称
     */
    private String deptName1;

    /**
     * 权属部门代码
     */
    private String deptCode2;

    /**
     * 权属部门名称
     */
    private String deptName2;

    /**
     * 养护部门代码
     */
    private String deptCode3;

    /**
     * 养护部门名称
     */
    private String deptName3;

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
    private LocalDateTime ORDate;

    /**
     * 变更日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime CHDate;

    /**
     * 数据来源
     */
    private String  dataSource;

    /**
     * 备注
     */
    private String note;
}
