package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 流程时限实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class ProcessTimeLimit {

    private String id;

    private String taskName;

    private KV level;

    private KV timeType;

    /**
     * 时限
     */
    private int timeLimit;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public KV getLevel() {
        return level;
    }

    public void setLevel(KV level) {
        this.level = level;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public KV getTimeType() {
        return timeType;
    }

    public void setTimeType(KV timeType) {
        this.timeType = timeType;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
