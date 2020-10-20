package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 分类实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class EventType {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String code;

    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EventType parentId;

}
