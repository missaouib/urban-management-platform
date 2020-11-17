package com.unicom.urban.management.pojo.vo;

/**
 * 监督员评价实体VO
 * @author Liubozhi
 */
public class  SupervisorEvaluateVO {

    /**
     * 姓名
     */
    private String SupervisorName;
    /**
     * 责任网格
     */
    private Integer gridOnwer;

    /**
     *监督员上报数
     */
    private Integer patrolReport;
    /**
     * 上报监督员
     */
    private String reportPatrolName;
    /**
     *监督员有效上报数
     */
    private Integer validPatrolReport;
    /**
     *监督员有效上报率
     */
    private Float reportVaildNumRate;
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
    private Float intimeVerifyRate;
    /**
     *'按时核查数
     */
    private Integer intimeCheck;
    /**
     * 漏报数(公众举报)
     */
    private Integer publicReport;
    /**
     * 立案数(监督员有效上报 立案+作废)
     */
    private Integer validReport;
    /**
     * 漏报率(漏报数/立案数×100%)
     */
    private Integer publicReportRate;
    /**
     *综合指标值
     */
    private Float aggregativeIndicator;
    /**
     *评价等级
     */
    private String ratingLevel;
}
