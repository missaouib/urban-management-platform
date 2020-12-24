package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 事件附件实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class EventFile {

    public final static Integer IMAGE = 1;

    public final static Integer VIDEO = 2;

    public final static Integer AUDIO = 3;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String fileName;

    @Column(columnDefinition = "tinyint")
    private Integer fileType;

    private String filePath;

    @Column(columnDefinition = "tinyint")
    private Integer management;

}
