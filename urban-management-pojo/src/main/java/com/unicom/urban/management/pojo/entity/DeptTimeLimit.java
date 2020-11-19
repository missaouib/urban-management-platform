package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 专业部门时限实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class DeptTimeLimit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventCondition eventCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV level;

    /**
     * 时限
     */
    private int timeLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    private KV timeType;

}
