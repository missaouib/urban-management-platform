package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 发布实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class Release extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String releaseName;

    /**
     * 图层ID:当发布后GIS平台将把该图层ID返回城管中，城管再次添加元素可以找到对应GIS平台的哪个图层
     */
    private String layerId;

    /**
     * GIS平台提供的发布的图层地址
     */
    private String url;

    /**
     * 类型：编辑的是部件、网格。
     */
    @ManyToOne
    @JoinColumn
    private KV kv;

    /**
     * 状态：用于判断是编辑中、发布、未发布等状态
     */
    private int sts;

    /**
     * 用于记录谁发布的
     */
    @ManyToOne
    @JoinColumn
    private User user;

}