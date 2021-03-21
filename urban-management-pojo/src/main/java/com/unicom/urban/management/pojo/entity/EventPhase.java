package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 流转记录实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class EventPhase {

    private String id;

    private Event eventId;

    private User userId;

    /**
     * 流程图实例id  (步骤)
     */
    private String taskId;

    /**
     * 流程名
     */
    private String taskName;

    /**
     * 处理意见
     */
    private String opinions;

    /**
     * 附件
     */
    private EventFile eventFileId;

    private LocalDateTime starTime;

    private LocalDateTime endTime;

    private DeptTimeLimit deptTimeLimit;

    private ProcessTimeLimit processTimeLimit;

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
    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public EventFile getEventFileId() {
        return eventFileId;
    }

    public void setEventFileId(EventFile eventFileId) {
        this.eventFileId = eventFileId;
    }

    public LocalDateTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalDateTime starTime) {
        this.starTime = starTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public DeptTimeLimit getDeptTimeLimit() {
        return deptTimeLimit;
    }

    public void setDeptTimeLimit(DeptTimeLimit deptTimeLimit) {
        this.deptTimeLimit = deptTimeLimit;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public ProcessTimeLimit getProcessTimeLimit() {
        return processTimeLimit;
    }

    public void setProcessTimeLimit(ProcessTimeLimit processTimeLimit) {
        this.processTimeLimit = processTimeLimit;
    }
}
