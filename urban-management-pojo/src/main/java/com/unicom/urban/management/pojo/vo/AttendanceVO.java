package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:27
 */
@Data
public class AttendanceVO {
    private String id;


    /**
     * 考勤人员
     */
    private String userId;

    private String userName;

    /**
     * 0上班1下班
     */
    private String attendanceType;

    /**
     * 打卡时间
     */
    private LocalDateTime attendanceDate;


    private Double x;

    private Double y;

    /**
     * 地理编码地址
     */
    private String address;

    /**
     * 考勤状态 0正常，1迟到，2早退，3缺勤，4非工作日打卡
     */
    private String type;
}
