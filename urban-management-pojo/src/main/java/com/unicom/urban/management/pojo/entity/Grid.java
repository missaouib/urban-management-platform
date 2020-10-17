package com.unicom.urban.management.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@SQLDelete(sql = "update grid set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
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
    @ManyToOne(fetch = FetchType.LAZY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Publish publish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Dept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

}
