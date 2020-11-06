package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.EventFile;
import lombok.Data;

import java.util.List;

/**
 * 流转统计DTO
 *
 * @author jiangwen
 */
@Data
public class StatisticsDTO {

    private String id;
    /**
     * 处理意见
     */
    private String opinions;

    /**
     * 附件列表
     */
    private List<EventFile> eventFileList;
    /**
     * 处理人
     */
    private String userId;

    private String eventId;

    private String buttonText;



}
