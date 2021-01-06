package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 综合评价
 *
 * @author liubozhi
 */
@Data
public class ComprehensiveVO {
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 处置数
     */
    private Integer disposeNum;
    /**
     * 应处置数
     */
    private Integer needDisposeNum;
    /**
     * 按时处置数
     */
    private Integer inTimeDisposeNum;
    /**
     * 超期处置数
     */
    private Integer overtimeDisposeNum;
    /**
     * 处置率
     */
    private Double disposeRateNum;
    private String disposeRate;
    /**
     * 按时处置率
     */
    private Double inTimeDisposeRateNum;
    private String inTimeDisposeRate;
    /**
     * 返工数
     */
    private Integer reworkNum;
    /**
     * 返工率
     */
    private Double reworkRateNum;
    private String reworkRate;
    /**
     * 排名
     */
    private String index;
    /**
     * 趋势
     */
    private String patrolReport;
}
