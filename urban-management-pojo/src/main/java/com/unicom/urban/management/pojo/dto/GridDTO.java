package com.unicom.urban.management.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.pojo.entity.KV;
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

    /**
     * 区域分类
     */
    @NotBlank(message = "区域分类不能为空")
    private KV gridKv;

    @NotBlank(message = "单元网格标识码不能为空")
    private String gridCode;

    @NotBlank(message = "单元网格不能为空")
    private String gridName;

    private String remark;

    @NotBlank(message = "面积不能为空")
    private String area;

    @NotBlank(message = "初始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime initialDate;

    @NotBlank(message = "终止日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime terminationDate;

    /* -Release----------------------------------- */
    /**
     * 类型：编辑的是部件、网格
     */
    private KV releaseKv;

    private String releaseName;

    /* -Record----------------------------------- */
    /**
     * 坐标
     */
    private String coordinate;

}
