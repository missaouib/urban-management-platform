package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * @author 顾志杰
 * @date 2020/11/4-14:17
 */
@Data
public class EventOneVO {

    private String id;

    private String processInstanceId;

    private String eventCode;

    private String eventTypeStr;

    /**
     * 问题描述
     */
    private String represent;

    /**
     * 案件地址
     */
    private String location;

    /**
     * 立案区域
     */
    private String regionStr;

    
}
