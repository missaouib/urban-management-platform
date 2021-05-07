package com.unicom.urban.management.pojo.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 督办实体类
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
public class Supervise extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * 督办人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 督办部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    /**
     * 督办意见
     */
    private String opinion;

    /**
     * 回复意见
     */
    private String replayOpinion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sts")
    private KV sts;

}
