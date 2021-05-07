package com.unicom.urban.management.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:27
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    /**
     * 考勤人员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

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
