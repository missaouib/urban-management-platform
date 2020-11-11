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
 * 用户
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@SQLDelete(sql = "update sys_user set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
@Table(name = "sys_user", indexes = {@Index(columnList = "deleted")})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 姓名
     */
    @Column(nullable = false, columnDefinition = "char(15)")
    private String name;

    /**
     * 登录账号
     */
    @Column(nullable = false, unique = false)
    private String username;

    @Column(nullable = false, columnDefinition = "char(60)")
    private String password;

    /**
     * 手机号码
     */
    @Column(nullable = false, columnDefinition = "char(11)")
    private String mobileNumber;

    /**
     * 邮箱
     */
    @Column(columnDefinition = "varchar(50)")
    private String mail;

    /**
     * 头像图片URL
     */
    @Column(nullable = false)
    private String profilePhotoUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_dept", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private List<Dept> deptList;


}
