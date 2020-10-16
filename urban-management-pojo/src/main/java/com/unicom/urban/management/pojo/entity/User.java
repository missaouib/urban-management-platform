package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.enums.Delete;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录账号
     */
    @Column(nullable = false, unique = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * 手机号码
     */
    private String mobileNumber;

    /**
     * 邮箱
     */
    private String mail;

//    @ManyToOne
//    private DictData sex;

    /**
     * 头像图片URL
     */
    @Column(nullable = false)
    private String profilePhotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Dept dept;


}
