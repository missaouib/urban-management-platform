package com.unicom.framework.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * 登录日志
 *
 * @author liukai
 */
@Data
@Entity
@Table(name = "sys_login_info")
public class LoginInfo {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String username;

    private String ip;

    @CreatedDate
    private LocalDateTime createTime;


}
