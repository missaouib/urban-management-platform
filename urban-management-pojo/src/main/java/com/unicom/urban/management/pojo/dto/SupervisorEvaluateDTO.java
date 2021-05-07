package com.unicom.urban.management.pojo.dto;

import java.time.LocalDateTime;

/**
 * 岗位评价实体DTO
 *
 * @author Liubozhi
 */
public class SupervisorEvaluateDTO {
    /**
     * 开始日期
     */
    private LocalDateTime startTime;
    /**
     * 结束日期
     */
    private LocalDateTime endTime;
    /**
     * 本周
     */
    private String name;
    /**
     * 本月
     */
    private Integer gridOnwer;

    /**
     *本年
     */
    private Integer reportNum;

}
