package com.unicom.urban.management.pojo.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime stateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

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
    @JoinColumn(name = "statistics_id")
    private List<EventFile> eventFileList;

    /**
     * 判断当前环节是否超时2绿灯 1黄灯 0红灯
     * processTimeLimit时限的 0-80% 是2
     * processTimeLimit时限的 80-100% 是1
     * processTimeLimit时限的 100%+ 是0
     */
    private String sts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    /* --------------------------------------------------------- */

    /**
     * 监督员上报
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer patrolReport = 0;

    /**
     * 公众举报 = 漏报数
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer publicReport = 0;

    /**
     * 上报
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer report = 0;

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
     * 上报监督员岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role reportPatrolRole;

    /**
     * 监督员有效上报 立案+作废
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer validPatrolReport = 0;

    /**
     * 公众举报有效上报
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer validPublicReport = 0;

    /**
     * 有效上报
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer validReport = 0;

    /* --------------------------------------------------------- */

    /**
     * 应核实 监督员 应核实 = 受理员发给该监督员的核实的案件
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needVerify = 0;

    /**
     * 核实 监督员 有监督员回馈的核实结果的事
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer verify = 0;

    /**
     * 按时核实 监督员 不超期的 核实数
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeVerify = 0;

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

    /**
     * 核实监督员岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Role verifyPatrolRole;

    /* --------------------------------------------------------- */

    /**
     * 不予受理 受理员
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer notOperate = 0;

    /**
     * 待受理 未批转值班长
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toOperate = 0;

    /**
     * 按时受理
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeOperate = 0;

    /**
     * 受理 批转值班长 触发
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer operate = 0;

    /**
     * 受理人标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @CreatedBy
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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needSendVerify = 0;

    /**
     * 发核实 受理员 只要派出去就算
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer sendVerify = 0;

    /**
     * 按时发核实 受理员 按时下发核实
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeSendVerify = 0;

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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer sendCheck = 0;

    /**
     * 应发核查 受理员
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needSendCheck = 0;

    /**
     * 发核查人标识 受理员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_check_human")
    private User sendCheckHuman;

    /**
     * 发核查人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_check_human_name")
    private User sendCheckHumanName;

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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inst = 0;

    /**
     * 按时立案 值班长 不超时立案
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeInst = 0;

    /**
     * 待立案 值班长 未立案的数
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toInst = 0;

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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer close = 0;

    /**
     * 按时结案
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeClose = 0;

    /**
     * 待结案
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toClose = 0;

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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer dispatch = 0;

    /**
     * 待派遣 派遣员
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toDispatch = 0;

    /**
     * 应派遣 派遣员 应派遣 = 已派遣 + 未派遣
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needDispatch = 0;

    /**
     * 按时派遣 派遣员 = 未超时的派遣
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeDispatch = 0;

    /**
     * 准确派遣数 未出现重新派的其他专业部门 结案触发
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer accuracyDispatch = 0;

    /**
     * 错误派遣 出现派遣2个或者以上的专业部门 结案触发 例如 第一派遣A 第二次派遣 A B 两个部门
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer errorDispatch = 0;

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
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer dispose = 0;

    /**
     * 应处置
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needDispose = 0;

    /**
     * 待处置
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toDispose = 0;

    /**
     * 按时处置
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeDispose = 0;

    /**
     * 超时未处置
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer overtimeToDispose = 0;

    /**
     * 超时处置
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer overtimeDispose = 0;

    /**
     * 处置部门标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Dept disposeUnit;

    /**
     * 处置部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Dept disposeUnitName;

    /* --------------------------------------------------------- */

    /**
     * 应核查   监督员
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer needCheck = 0;

    /**
     * 按时核查
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeCheck = 0;

    /**
     * 按时发核查
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer inTimeSendCheck = 0;

    /**
     * 核查批转
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer checkTrans = 0;

    /**
     * 核查监督员标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_patrol_id")
    private Role checkPatrolId;

    /**
     * 核查监督员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_patrol_name")
    private User checkPatrolName;

    /**
     * 核查受理员标识
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer checkTransHuman = 0;

    /**
     * 核查受理员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_trans_human_name")
    private User checkTransHumanName;

    /**
     * 核查受理员岗位标识
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_trans_human_role")
    private Role checkTransHumanRole;

    /**
     * 核查批转时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkTransTime;

    /* --------------------------------------------------------- */

    /**
     * 作废
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer cancel = 0;

    /**
     * 作废时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelDate;

    /**
     * 挂账
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer hang = 0;

    /**
     * 挂账时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hangDate;

    /**
     * 返工 二次或者多次派遣同一个专业部门 经历核查阶段 结案触发
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer rework = 0;

    /**
     * 显示类型标识
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer displayStyleId = 0;

    /**
     * 推诿
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer shuffle = 0;

    /**
     * 发核查
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer sendCheckNum = 0;

    /**
     * 结案存档（自处理被判定有效案件）
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer closingFiling = 0;

    /**
     * 无效案件（自处理被判定无效案件）
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer invalidEvent = 0;

    /**
     * 延时
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer delayedState = 0;

    /**
     * 延时       申请延时的时间
     */
    private LocalDateTime delayedDate;

    /**
     * 回退
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer backOff = 0;

    /**
     * 回退        申请回退的时间
     */
    private LocalDateTime backOffDate;

    /**
     * 特殊环节开始时间   例如同意挂账的时间     同意作废时间   同意回退的时间
     */
    private LocalDateTime specialStartTime;

    /**
     * 特殊环节结束时间   例如挂账的恢复时间     同意作废时间   同意回退的时间
     */
    private LocalDateTime specialEndTime;

    /**
     * 延时多久
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer delayedHours = 0;

    /**
     * 排序
     */
    @Column(columnDefinition = "TINYINT(50)")
    private Integer sort;

    /**
     * 核查数
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer checkNum = 0;

    /**
     * 待结案受理
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer toCloseEvent = 0;

    /**
     * 结案受理
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer closeOperate = 0;

    /**
     * 按时结案受理
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private Integer closeInTimeOperate = 0;

}
