package com.unicom.urban.management.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 部门考勤范围
 *
 * @author 顾志杰
 * @date 2021/3/23-8:27
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceScheme {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    /**
     * 应用组织单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Dept dept;

    /**
     *  0启用  1 未启用
     */
    private String sts;

    /**
     * 半径
     */
    private Integer radius;


    private Double x;

    private Double y;

    /**
     * 地理编码地址
     */
    private String address;

}
