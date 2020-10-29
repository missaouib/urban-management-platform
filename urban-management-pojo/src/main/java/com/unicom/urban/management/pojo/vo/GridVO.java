package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网格实体类
 *
 * @author jiangwen
 */
@Data
public class GridVO {

    private String id;

    private String parentId;

    private String coordinate;

    private String gridCode;

    private String gridName;

    private String remark;

    private String area;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initialDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime terminationDate;

}
