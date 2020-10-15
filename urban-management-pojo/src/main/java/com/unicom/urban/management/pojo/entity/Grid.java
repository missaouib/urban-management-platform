package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 网格实体类
 *
 * @author jiangwen
 */
@Data
@Entity
public class Grid extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String gridCode;

    private String gridName;

    private String remark;

    /**
     * 区域分类
     */
    @ManyToOne
    @JoinColumn
    private KV kv;

    private String area;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime initialDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime terminationDate;

    private int sts;

    @ManyToOne
    @JoinColumn
    private Publish publish;

    @ManyToOne
    @JoinColumn
    private Record record;

    @ManyToOne
    @JoinColumn
    private Dept dept;

    @ManyToOne
    @JoinColumn
    private User user;

}
