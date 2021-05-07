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

    private String id;

    private EventCondition eventCondition;

    private KV level;

    /**
     * 时限
     */
    private int timeLimit;

    private KV timeType;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public EventCondition getEventCondition() {
        return eventCondition;
    }

    public void setEventCondition(EventCondition eventCondition) {
        this.eventCondition = eventCondition;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public KV getLevel() {
        return level;
    }

    public void setLevel(KV level) {
        this.level = level;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public KV getTimeType() {
        return timeType;
    }

    public void setTimeType(KV timeType) {
        this.timeType = timeType;
    }

}
