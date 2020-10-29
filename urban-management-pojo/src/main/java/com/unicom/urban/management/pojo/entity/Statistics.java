package com.unicom.urban.management.pojo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 流转统计实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class Statistics {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event event;

    /**
     * 状态时间 计算的时候转化成分
     * 挂账或作废
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime stateCdae;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime starTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 整体时限
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DeptTimeLimit deptTimeLimit;

    /**
     * 环节时限
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ProcessTimeLimit processTimeLimit;

    /**
     * 处理意见
     */
    private String opinions;

    /**
     * 附件
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<EventFile> eventFileList;

    /* --------------------------------------------------------- */

    /**
     * 监督员上报
     */
    private int patrolReport;

    /**
     * 公众举报 = 漏报数
     */
    private int publicReport;

    /**
     * 上报
     */
    private int report;

    /**
     * 上报监督员标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User reportPatrol;

    /**
     * 上报监督员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User reportPatrolName;

    /**
     * 监督员有效上报 立案+作废
     */
    private int validPatrolReport;

    /**
     * 公众举报有效上报
     */
    private int validPublicReport;

    /**
     * 有效上报
     */
    private int validReport;

    /* --------------------------------------------------------- */

    /**
     * 应核实 监督员 应核实 = 受理员发给该监督员的核实的案件
     */
    private int needVerify;

    /**
     * 核实 监督员 有监督员回馈的核实结果的事
     */
    private int verify;

    /**
     * 按时核实 监督员 不超期的 核实数
     */
    private int intimeVerify;

    /**
     * 核实监督员标识 监督员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User verifyPatrol;

    /**
     * 核实监督员 监督员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User verifyPatrolName;

    /* --------------------------------------------------------- */

    /**
     * 不予受理 受理员
     */
    private int notOperate;

    /**
     * 待受理 未批转值班长
     */
    private int toOperate;

    /**
     * 按时受理
     */
    private int intimeOperate;

    /**
     * 受理 批转值班长 触发
     */
    private int operate;

    /**
     * 受理人标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User operateHuman;

    /**
     * 受理人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User operateHumanName;

    /**
     * 受理人岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role operateRole;

    /* --------------------------------------------------------- */

    /**
     * 应发核实 受理员 按时完成的核实 = 待发核实 + 已发核实
     */
    private int needSendVerify;

    /**
     * 发核实 受理员 只要派出去就算
     */
    private int sendVerify;

    /**
     * 按时发核实 受理员 按时下发核实
     */
    private int intimeSendVerify;

    /**
     * 发核实人姓名
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User sendVerifyHumanName;

    /**
     * 发核实人id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User sendVerifyHuman;

    /**
     * 发核实人岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role sendVerifyHumanRole;

    /* --------------------------------------------------------- */

    /**
     * 发核查 受理员
     */
    private int sendCheck;

    /**
     * 应发核查 受理员
     */
    private int needSendCheck;

    /**
     * 发核查人标识 受理员
     */
    private int sendCheckHuman;

    /**
     * 发核查人
     */
    private int sendCheckHumanName;

    /**
     * 发核查人岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role sendCheckHumanRole;

    /* --------------------------------------------------------- */

    /**
     * 立案 值班长 立案
     */
    private int inst;

    /**
     * 按时立案 值班长 不超时立案
     */
    private int intimeInst;

    /**
     * 待立案 值班长 未立案的数
     */
    private int toInst;

    /**
     * 立案人标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User instHuman;

    /**
     * 立案人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User instHumanName;

    /**
     * 立案人岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role instRole;

    /* --------------------------------------------------------- */

    /**
     * 结案
     */
    private int close;

    /**
     * 按时结案
     */
    private int inTimeClose;

    /**
     * 待结案
     */
    private int toClose;

    /**
     * 结案人标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User closeHuman;

    /**
     * 结案人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User closeHumanName;

    /**
     * 结案人岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role closeRole;

    /* --------------------------------------------------------- */

    /**
     * 派遣 派遣员 已派遣
     */
    private int dispatch;

    /**
     * 待派遣 派遣员
     */
    private int toDispatch;

    /**
     * 应派遣 派遣员 应派遣 = 已派遣 + 未派遣
     */
    private int needDispatch;

    /**
     * 按时派遣 派遣员 = 未超时的派遣
     */
    private int intimeDispatch;

    /**
     * 准确派遣数 未出现重新派的其他专业部门 结案触发
     */
    private int accuracyDispatch;

    /**
     * 错误派遣 出现派遣2个或者以上的专业部门 结案触发 例如 第一派遣A 第二次派遣 A B 两个部门
     */
    private int errorDispatch;

    /**
     * 派遣员标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User dispatchHuman;

    /**
     * 派遣员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User dispatchHumanName;

    /**
     * 派遣员岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role dispatchHumanRole;

    /* --------------------------------------------------------- */

    /**
     * 处置   专业部门
     */
    private int dispose;

    /**
     * 应处置
     */
    private int needDispose;

    /**
     * 待处置
     */
    private int toDispose;

    /**
     * 按时处置
     */
    private int intimeDispose;

    /**
     * 超时未处置
     */
    private int overtimeToDispose;

    /**
     * 超时处置
     */
    private int overtimeDispose;

    /**
     * 处置部门标识
     */
    private int disposeUnit;

    /**
     * 处置部门
     */
    private String disposeUnitName;

    /* --------------------------------------------------------- */

    /**
     * 应核查   监督员
     */
    private int needCheck;

    /**
     * 按时核查
     */
    private int intimeCheck;

    /**
     * 按时发核查
     */
    private int intimeSendCheck;

    /**
     * 核查批转
     */
    private int checkTrans;

    /**
     * 核查监督员标识
     */
    private int checkPatrolId;

    /**
     * 核查监督员
     */
    private String checkPatrolName;

    /**
     * 核查受理员标识
     */
    private int checkTransHuman;

    /**
     * 核查受理员
     */
    private String checkTransHumanName;

    /**
     * 核查受理员岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role checkTransHumanRole;

    /**
     * 核查批转时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime checkTransTime;

    /* --------------------------------------------------------- */

    /**
     * 作废
     */
    private int cancel;

    /**
     * 作废时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime cancelDate;

    /**
     * 挂账
     */
    private int hang;

    /**
     * 挂账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime hangDate;

    /**
     * 返工 二次或者多次派遣同一个专业部门 经历核查阶段 结案触发
     */
    private int rework;

    /**
     * 显示类型标识
     */
    private int displayStyleId;

    /**
     * 推诿
     */
    private int shuffle;


}
