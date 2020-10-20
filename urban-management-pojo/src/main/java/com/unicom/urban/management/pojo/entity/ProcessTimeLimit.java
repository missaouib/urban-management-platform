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

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String taskName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private KV level;

    /**
     * 时限
     */
    private int timeLimit;

}
