package com.unicom.urban.management.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 事件附件实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class EventFile {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_type")
    private KV fileType;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private KV management;

}
