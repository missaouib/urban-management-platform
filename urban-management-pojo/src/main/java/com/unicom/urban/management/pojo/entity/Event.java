package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 事件
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
    private Condition conditionId;
    private String describe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TimeLimit timeLimit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Grid gridId;
    private String location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    private int phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV eventSource;
    private double longitude;
    private double latitude;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV region;

}
