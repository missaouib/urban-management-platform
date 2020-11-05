package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private EventType eventType;


    /**
     * 立案条件
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private EventCondition condition;

    /**
     * 问题描述
     */
    private String represent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private DeptTimeLimit timeLimit;

    /**
     * 网格
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Grid grid;

    /**
     * 案件地址
     */
    private String location;

    /**
     * 上报人
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    private String phone;

    /**
     * 问题来源
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private KV eventSource;

    private double x;

    private double y;

    /**
     * 案件状态 比如挂账
     */
    private int sts;

    /**
     * 案件类型 日常管理、专项普查、其他
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private KV recType;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 案件状态 未定
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private KV eventSate;
    /**
     * 条件实体类
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private EventCondition eventCondition;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private List<Statistics> statisticsList;

    /**
     * 诉求人
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Petitioner petitioner;
    /**
     * 事件附件实体类
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<EventFile> eventFileList;

}
