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
    private EventType eventTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition conditionId;

    private String represent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TimeLimit timeLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Grid gridId;

    private String location;

    /**
     * 上报人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    private String phone;

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
    private EventType recTypeId;

    /**
     * 案件状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV eventSate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition eventCondition;

}
