package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 时限
 *
 * @author jiangwen
 */
@Data
@Entity
public class TimeLimit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventType eventTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV level;

    private int timeLimit;

}
