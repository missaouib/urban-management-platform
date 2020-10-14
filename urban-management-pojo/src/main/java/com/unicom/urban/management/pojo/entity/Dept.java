package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 责任部门实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class Dept extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String deptName;

    private int sts;

    private String level;

    @ManyToOne
    @JoinColumn
    private Dept parent;

}
