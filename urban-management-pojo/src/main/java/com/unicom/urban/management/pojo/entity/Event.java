package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 案件实体类
 *
 * @author jiangwen
 */
@Data
@Entity
@SQLDelete(sql = "update event set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String eventCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventType eventType;
    /**
     * 立案条件
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition condition;

    private String represent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DeptTimeLimit timeLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Grid grid;
    /**
     * 案件地址
     */
    private String location;

    /**
     * 上报人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    private String phone;

    /**
     * 问题来源
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV eventSource;

    private double x;

    private double y;

    private String taskId;

    /**
     * 区域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV region;

    /**
     * 时间状态 未处理、处理中、关单
     */
    private int sts;

    /**
     * 问题类型 事件 部件
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventType recType;

    /**
     * 案件状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV eventSate;
    /**
     * 立案条件
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition eventCondition;
    /**
     * 案件类型 日常管理、专项普查、其他
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV recTypeId;
}
