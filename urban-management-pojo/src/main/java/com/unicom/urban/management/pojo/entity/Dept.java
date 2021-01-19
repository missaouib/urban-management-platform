package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 责任部门实体类
 *
 * @author jiangwen
 */
@Data
@SQLDelete(sql = "update sys_dept set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
@Entity
@Table(name = "sys_dept")
public class Dept extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 部门名称
     */
    private String deptName;

    private String level;

    private String type;

    /**
     * 办公电话
     */
    private String deptPhone;


    /**
     * 部门地址
     */
    private String deptAddress;

    /**
     * 部门父节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Dept parent;

    @Where(clause = "deleted = " + Delete.NORMAL)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private List<User> userList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private List<Role> roleList;

    /**
     * 描述
     */
    private String describes;

    /**
     * 所属区域       到第三级  区、街道、社区
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    private Grid grid;

    private Integer sort;

    public Dept() {
    }

    public Dept(String id) {
        this.id = id;
    }

}
