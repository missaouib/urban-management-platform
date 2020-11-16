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
@SQLDelete(sql = "update dept set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
@Entity
public class Dept extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String deptName;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer sts;

    private String level;

    /**
     * 部门父节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Dept parent;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_dept", joinColumns = @JoinColumn(name = "dept_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private List<Role> roleList;

    public Dept() {
    }

    public Dept(String id) {
        this.id = id;
    }

}
