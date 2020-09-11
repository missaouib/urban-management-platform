package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * 登录日志
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    @NotBlank(message = "账号不能为空")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;


}
