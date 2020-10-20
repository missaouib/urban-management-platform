package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventFile eventFileId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime starTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DeptTimeLimit deptTimeLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ProcessTimeLimit processTimeLimit;

}
