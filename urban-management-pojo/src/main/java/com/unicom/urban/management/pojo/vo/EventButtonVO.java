package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 事件VO
 *
 * @author jiangwen
 */
@Data
public class EventButtonVO {

    private String id;

    /**
     * 流程图中任务名称
     */
    private String taskName;

    private String buttonName;

    private String buttonValue;

    private Integer sort;

}
