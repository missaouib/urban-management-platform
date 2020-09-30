package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.common.annotations.validation.MobileNumber;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * 用户
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

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录账号
     */
    @NotBlank(message = "账号不能为空")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @MobileNumber(message = "手机号码格式不正确")
    private String mobileNumber;

    @ManyToOne
    private DictData sex;


}
