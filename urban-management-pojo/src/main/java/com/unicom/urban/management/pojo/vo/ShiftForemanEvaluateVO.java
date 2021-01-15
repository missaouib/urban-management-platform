package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 值班长岗位评价
 *
 * @author liubozhi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftForemanEvaluateVO {

    /**
     * 姓名
     */
    private String instHumanName;
    /**
     * '立案数'
     */
    private Integer inst;
    /**
     * '按时立案数'
     */
    private Integer intimeInst;
    /**
     * 按时立案率(按时立案数/立案数×100%)
     */
    private Double intimeInstRateNum;
    private String intimeInstRate;
    /**
     * 准确立案数(立案数  - 作废数)
     */
    private Integer exactInst;
    /**
     * 准确立案率(准确立案数/立案数×100%)
     */
    private Double exactInstRateNum;
    private String exactInstRate;
    /**
     * '按时结案数'
     */
    private Integer inTimeClose;
    /**
     * 应结案数
     */
    private Integer close;
    /**
     * 按时结案率（按时结案数/结案数×100%）
     */
    private Double inTimeCloseRateNum;
    private String inTimeCloseRate;
    /**
     * 综合指标值
     */
    private String aggregativeIndicator;
    /**
     * 评价等级
     *
     */
    private String ratingLevel;

    /**
     * 排名
     */
    private String index;


}
