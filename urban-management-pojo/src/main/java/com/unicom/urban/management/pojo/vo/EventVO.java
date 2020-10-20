package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事件VO
 *
 * @author jiangwen
 */
@Data
public class EventVO {

    private String userName;

    private String represent;

    private String eventTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
