package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 权限分类实体类
 *
 * @author liubozhi
 */
@Setter
@Getter
@Entity
@Table(name = "sys_menu_type")
public class MenuType {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private Integer sort;
}
