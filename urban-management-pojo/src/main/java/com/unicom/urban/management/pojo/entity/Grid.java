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
import java.util.List;

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

    private String area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Grid parent;

    /**
     * 等级 1区域 - 2街道 - 3社区 - 4网格
     */
    @Column(columnDefinition = "TINYINT(1)")
    private Integer level;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    private List<User> userList;

}
