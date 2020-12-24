package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 事件VO
 *
 * @author jiangwen
 */
@Data
public class EventVO {

    private String id;

    private String userName;

    private String represent;

    private String eventTypeName;

    private Double x;

    private Double y;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

//    private List<EventFile> eventFileList;

    /**
     * 事件来源id
     */
    private String eventSourceId;

    /**
     * 事件来源回显
     */
    private String eventSourceName;

    /**
     * 案件类型
     */
    private String recTypeName;

    /**
     * 案件编号
     */
    private String eventCode;

    private String taskId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 当前状态
     */
    private String taskName;

    /**
     * 时限
     */
    private Integer timeLimit;

    /**
     * 时限单位
     */
    private String timeType;

    /**
     * 办结时间
     */
    private String closeTime;

    private String sts;

    private String statisticsId;


    /**
     * 督办状态
     */
    private String supSts;

    /**
     * 部门
     */
    private String deptName;

    /**
     * 列表新增列 时限 例 7工作日
     */
    private String timeLimitStr;

    /**
     * 列表新增列 应结束时间 开始时间+时限
     */
    private String endTimeStr;


    private String url;

    private Integer urgent;

}
