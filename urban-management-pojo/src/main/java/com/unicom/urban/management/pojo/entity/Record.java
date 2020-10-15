package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 编辑实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 坐标
     */
    private String coordinate;

    @ManyToOne
    @JoinColumn
    private Publish release;

    /**
     * 状态：用于判断是编辑中、发布等状态
     */
    private int sts;

}
