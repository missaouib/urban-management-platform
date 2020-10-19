package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 事件分类
 *
 * @author jiangwen
 */
@Data
@Entity
public class EventType {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String eventTypeId;

    private String eventTypeName;

    private String mainTypeId;

    private String mainTypeName;

    private String subTypeId;

    private String subTypeName;

}
