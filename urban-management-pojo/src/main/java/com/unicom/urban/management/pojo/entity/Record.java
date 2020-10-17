package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 编辑实体类
 *
 * @author jiangwen
 */
@Data
@Entity
@SQLDelete(sql = "update record set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 坐标
     */
    private String coordinate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Publish publish;

    /**
     * 状态：用于判断是编辑中、发布等状态
     */
    private int sts;

}
