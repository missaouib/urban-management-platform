package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监督员评价实体VO
 * @author Liubozhi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  SupervisorEvaluateVO {

    /**
     * 姓名
     */
    private String supervisorName;
    /**
     * 责任网格
     */
    private String gridOnwer;

    /**
     *监督员上报数
     */
    private Integer patrolReport;
    /**
     *监督员有效上报数
     */
    private Integer validPatrolReport;
    /**
     *监督员有效上报率
     */
    private Double reportVaildNumRateNum;
    private String reportVaildNumRate;
    /**
     *'按时核实数
     */
    private Integer intimeVerify;

    /**
     *'应核实数
     */
    private Integer needVerify;
    /**
     *'按时核实率
     */
    private Double inTimeVerifyRateNum;
    private String inTimeVerifyRate;
    /**
     *'按时核查数
     */
    private Integer inTimeCheck;
    /**
     * 漏报数(公众举报)
     */
    private Integer publicReport;
    /**
     * 立案数(监督员有效上报 立案+作废)
     */
    private Integer inst;
    /**
     * 漏报率(漏报数/立案数×100%)
     */
    private String publicReportRate;
    /**
     *综合指标值
     */
    private String aggregativeIndicator;
    /**
     *评价等级
     */
    private String ratingLevel;
    /**
     *按时核查率
     */
    private Double inTimeCheckRateNum;
    private String inTimeCheckRate;
    /**
     * 排名
     */
    private String index;
}
