package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * 登录日志
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_login_info")
public class LoginInfo extends BaseEntity {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ip;

    private String browser;

    private String os;

    private String message;


}
