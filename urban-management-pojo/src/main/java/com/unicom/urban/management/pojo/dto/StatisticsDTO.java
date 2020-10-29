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

    /**
     * 处理意见
     */
    private String opinions;

    /**
     * 附件列表
     */
    private List<String> eventFileList;

}
