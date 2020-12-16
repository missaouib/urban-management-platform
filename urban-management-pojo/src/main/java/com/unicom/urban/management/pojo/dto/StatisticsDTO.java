package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 流转统计DTO
 *
 * @author jiangwen
 */
@Data
public class StatisticsDTO {

    private String eventId;
    private String buttonId;
    /**
     * 处理意见
     */
    private String opinions;

    private String deptId;

    /**
     * 图片集合
     */
    private List<String> imageUrlList;
    /**
     * 音频集合
     */
    private List<String> musicUrlList;
    /**
     * 视频集合
     */
    private List<String> videoUrlList;
    /**
     * 处理人
     */
    private String userId;

    private String buttonText;

    private Integer delayedTime;



}
