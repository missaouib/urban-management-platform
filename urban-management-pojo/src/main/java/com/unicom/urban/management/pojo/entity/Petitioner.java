package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 诉求人
 *
 * @author liubozhi
 */
@Data
@Entity
@Table(name = "event_petitioner")
public class Petitioner {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String sex;

    private String phone;
}
