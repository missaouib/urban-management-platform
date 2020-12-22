package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 业务的流程实体类 和Activiti的工作流程没有关系
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
     * 父
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Process parent;

    /**
     * 为首页进行对应待办跳转
     */
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

}
