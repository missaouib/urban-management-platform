package com.unicom.urban.management.common.constant;

/**
 * event状态列表
 *
 * @author jiangwen
 */
public class EventConstant {

    /**
     * 监督员草稿
     */
    public static final int SUPERVISE_SAVE = 0;

    /**
     * 监督员上报
     */
    public static final int SUPERVISE_REPORTING = 1;

    /**
     * 受理员上报事件
     */
    public static final int ACCEPTANCE_REPORTING = 3;

    /**
     * 值班长立案
     * 到派遣员
     */
    public static final int SHIFT_LEADER_FILING = 4;

    /**
     * 派遣员派遣
     * 到专业部门
     */
    public static final int DISPATCHER_DISPATCH = 5;

    /**
     * 专业部门完结
     * 到核查
     */
    public static final int PROFESSIONAL_DEPARTMENTS_FINISH = 6;

    /**
     * 专业部门申请挂账
     */
    public static final int PROFESSIONAL_DEPARTMENTS_ON_ACCOUNT = 7;

    /**
     * 专业部门挂账成功
     */
    public static final int PROFESSIONAL_DEPARTMENTS_ACCOUNT_SUCCESS = 8;

    public static final String ACCEPTANCE_CASE_REGISTRATION = "受理员-案件登记";

    public static final String ACCEPTANCE_CASE_VERIFICATION = "监督员-信息核实";

    public static final String ACCEPTANCE_SEND_VERIFICATION = "受理员-信息收集";

    public static final String SHIFTLEADER = "值班长-立案";

    public static final String DISPATCHER = "派遣员-派遣";

    public static final String PROFESSIONALAGENC = "专业部门";

}
