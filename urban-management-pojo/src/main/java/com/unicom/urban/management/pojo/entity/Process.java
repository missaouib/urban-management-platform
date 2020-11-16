package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 流程实体类
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
public class Process extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 七步环节名称
     */
    private String nodeName;

    /**
     * 子环节名称
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private KV node;

    /**
     * 父
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Process parent;


}
