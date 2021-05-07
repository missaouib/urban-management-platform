package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MenuVO {

    private String id;

    private String name;

    private String path;

    private String icon;

    private Integer sort;

    private String parentId;

    private String menuTypeId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer checkbox;

    /**
     * 用途   PC  、 App
     */
    private Integer purpose;

}
