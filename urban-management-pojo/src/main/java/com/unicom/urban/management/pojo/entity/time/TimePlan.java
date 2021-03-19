package com.unicom.urban.management.pojo.entity.time;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 计时管理方案实体类
 *
 * @author liukai
 */
@Entity
@Table(name = "time_plan")
public class TimePlan {

    private String id;

    /**
     * 方案开始时间
     */
    private LocalDateTime startTime;

    /**
     * 方案结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态 0启用 1未启用
     */
    private Status sts;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Enumerated(EnumType.ORDINAL)
    public Status getSts() {
        return sts;
    }

    public void setSts(Status sts) {
        this.sts = sts;
    }


    public enum Status {

        ENABLE("启用"),
        DISABLE("未启用");

        Status(String type) {
            this.type = type;
        }

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

