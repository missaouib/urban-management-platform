package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:27
 */
@Data
public class AttendanceDTO {

    /**
     * 0上班1下班
     */
    @NotBlank(message = "考勤类型不能为空")
    private String attendanceType;

    @NotNull(message = "坐标不能为空")
    private Double x;
    @NotNull(message = "坐标不能为空")
    private Double y;

    /**
     * 地理编码地址
     */
    @NotBlank(message = "地理编码地址")
    private String address;

}
