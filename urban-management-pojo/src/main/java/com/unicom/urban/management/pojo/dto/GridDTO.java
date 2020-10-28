package com.unicom.urban.management.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 网格实体类
 *
 * @author jiangwen
 */
@Data
public class GridDTO {

    private String id;

    @NotBlank(message = "单元网格标识码不能为空")
    private String gridCode;

    @NotBlank(message = "单元网格名称不能为空")
    private String gridName;

    private String remark;

    @NotBlank(message = "面积不能为空")
    private String area;

    @NotNull(message = "初始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initialDate;

    @NotNull(message = "终止日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime terminationDate;

    /* -Record----------------------------------- */
    /**
     * 坐标
     */
    private String coordinate;

}
