package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志
 *
 * @author liukai
 */
@Data
public class LoginLogVO {

    private String id;

    private String username;

    private String ip;

    private String browser;

    private String os;

    private Integer message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;


}
