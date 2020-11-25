package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 受理员岗位评价
 *
 * @author liubozhi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptorEvaluateVO {
    /**
     * 受理人
     */
    private String operateHumanNameId;
    private String operateHumanName;
    /**
     * 受理数
     */
    private Integer operate;
    /**
     * 核实按时派发数
     */
    private Integer intimeSendVerify;
    /**
     * 核实应派发数
     */
    private Integer needSendVerify;
    /**
     * 核实按时派发率
     */
    private String SendVerifyRate;
    /**
     * 核查按时派发数
     */
    private Integer intimeSendCheck;

    /**
     * 核查应派发数
     */
    private Integer needSendCheck;

    /**
     * 核查按时派发率
     */
    private String needSendCheckRate;
    /**
     *综合指标值
     */
    private String aggregativeIndicator;
    /**
     *评价等级
     */
    private String ratingLevel;
}
