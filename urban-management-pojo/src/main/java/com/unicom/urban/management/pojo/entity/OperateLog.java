package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author liukai
 */
@Setter
@Getter
@Entity
@Table(name = "sys_operate_log")
public class OperateLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    /**
     * 操作名称
     */
    public String operateName;

    /**
     * 操作人的账号
     */
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ip;

    private String browser;

    private String os;

    @Column(nullable = false)
    private LocalDateTime operateTime;

    /**
     * 参数
     */
    private String params;


}
