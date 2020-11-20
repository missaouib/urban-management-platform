package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 派遣员岗位评价
 *
 * @author liubozhi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatcherEvaluateVO {
    /**
     * 姓名
     */
    private String dispatch;
    /**
     * '派遣数'
     */
    private Integer toDispatch;
    /**
     * '按时派遣数'
     */
    private Integer intimeDispatch;
    /**
     * 应派遣数
     */
    private Integer needDispatch;
    /**
     * '按时派遣率'
     */
    private String intimeDispatchRate;
    /**
     * 准确派遣数（派遣数-返工数）
     */
    private Integer accuracyDispatch;
    /**
     * '准确派遣率'
     */
    private String accuracyDispatchRate;
    /**
     * 综合指标值
     */
    private String aggregativeIndicator;
    /**
     * 评价等级
     */
    private String ratingLevel;
}