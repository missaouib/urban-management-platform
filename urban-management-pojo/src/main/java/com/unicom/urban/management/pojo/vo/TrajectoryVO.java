package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 轨迹记录表
 *
 * @author jiangwen
 */
@Data
public class TrajectoryVO {

    private String userId;

    private String userName;

    private Double x;

    private Double y;

    private LocalDateTime createTime;

}
