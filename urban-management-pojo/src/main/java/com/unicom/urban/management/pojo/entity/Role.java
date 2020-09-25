package com.unicom.urban.management.pojo.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 角色
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    /**
     * 角色名称
     */
    private String name;

}
