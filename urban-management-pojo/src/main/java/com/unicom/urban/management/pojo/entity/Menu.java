package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String path;

    private String icon;

    @OneToMany(mappedBy = "parent")
    private List<Menu> child;

    @ManyToOne
    @JoinColumn
    private Menu parent;


}
