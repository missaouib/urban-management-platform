package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author liukai
 */
@Data
public class OperateLogVO {

    private String id;

    private String username;

    private String ip;

    private String browser;

    private String os;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * 操作名称
     */
    public String operateName;

    /**
     * 参数
     */
    private String params;

}
