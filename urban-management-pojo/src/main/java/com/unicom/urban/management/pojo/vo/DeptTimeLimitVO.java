package com.unicom.urban.management.pojo.vo;

import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.entity.KV;
import lombok.Data;

@Data
public class DeptTimeLimitVO {
    private String id;
    private EventType eventType;
    private KV level;

    /**
     * 时限
     */
    private int timeLimit;
}
