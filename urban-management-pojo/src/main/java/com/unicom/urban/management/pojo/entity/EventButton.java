package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 页面提交按钮
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Where(clause = "deleted = " + Delete.NORMAL)
public class EventButton extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 流程图中任务名称
     */
    private String taskName;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer sort;

    private String buttonText;


}
