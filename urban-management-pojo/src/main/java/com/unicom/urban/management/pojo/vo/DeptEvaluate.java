package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 专业部门评价
 *
 * @author 顾志杰
 * @date 2020/11/17-18:10
 */
@Data
public class DeptEvaluate {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 立案数
     */
    private Integer registerNum;

    /**
     * 按时结案数
     */
    private Integer onTimeCloseNum;

    /**
     * 结案数
     */
    private Integer closeNum;

    /**
     * 应结案数
     */
    private Integer mustCloseNum;

    /**
     * 结案率
     */
    private String closeRate;

    /**
     * 应处置数
     */
    private Integer mustManagementNum;

    /**
     * 按时处置数
     */
    private Integer onTimeManagementNum;

    /**
     * 按时处置率
     */
    private String onTimeManagementRate;

    /**
     * 返工数
     */
    private Integer reworkNum;

    /**
     * 处置数
     */
    private Integer managementNum;

    /**
     * 返工率
     */
    private String reworkRate;




}
