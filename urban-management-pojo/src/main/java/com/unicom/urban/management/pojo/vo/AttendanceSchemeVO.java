package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门考勤范围
 *
 * @author 顾志杰
 * @date 2021/3/23-8:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSchemeVO {
    private String id;


    /**
     * 应用组织单位
     */
    private String deptId;

    private String deptName;

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
