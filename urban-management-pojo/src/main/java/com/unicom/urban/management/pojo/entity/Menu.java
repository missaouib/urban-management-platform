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
 * 菜单
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@SQLDelete(sql = "update sys_menu set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
@Table(name = "sys_menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String path;

    private String icon;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Menu> child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Menu parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_type_id")
    private MenuType menuType;


}
