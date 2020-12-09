package com.unicom.urban.management.pojo.entity;


import com.unicom.urban.management.pojo.Delete;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


/**
 * 角色
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_role")
@SQLDelete(sql = "update sys_role set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String describes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
    @OrderBy("sort ASC ")
    private List<Menu> menuList;

    private Integer sort;


    public Role() {

    }

    public Role(String id) {
        this.id = id;
    }


}
