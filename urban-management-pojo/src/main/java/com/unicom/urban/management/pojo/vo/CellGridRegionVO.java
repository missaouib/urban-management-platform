package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 区域评价VO
 *
 * @author jiangwen
 */
@Data
public class CellGridRegionVO {

    private String gridName;

    /**
     * 按期结案数
     */
    private Integer inTimeCloseSize;

    /**
     * 结案数
     */
    private Integer closeSize;

    /**
     * 应结案数
     */
    private Integer closeOrToCloseSize;

    /**
     * 监督举报核实数
     */
    private Integer publicReportAndInstSize;

    /**
     * 立案数
     */
    private Integer instSize;

    /**
     * 监督举报率
     */
    private String publicReportAndInstRate;

    /**
     * 结案率
     */
    private String closeRate;

    /**
     * 按期结案数
     */
    private String inTimeCloseRate;

    /**
     * 综合指标值
     */
    private Double comprehensiveIndexValue;

    /**
     * 社区
     */
    private String communityName;

    /**
     * 街道
     */
    private String streetName;

}
