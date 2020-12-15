package com.unicom.urban.management.pojo.vo;

import lombok.Data;

/**
 * 立案条件实体类
 *
 * @author liubozhi
 */
@Data
public class EventConditionVO {

    private String id;

    private String eventTypeId;

    private String eventTypeName;

    private String eventTypeParentName;

    private String region;

}
