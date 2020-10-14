package com.unicom.urban.management.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.pojo.entity.Dept;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 网格实体类
 *
 * @author jiangwen
 */
@Data
public class GridDTO {

    private String id;

    @NotBlank(message = "网格图层名称不能为空")
    private String layerGridName;

    private String remark;

    @NotBlank(message = "所属责任部门不能为空")
    private Dept dept;

    @NotBlank(message = "创建日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime initialDate;

}
