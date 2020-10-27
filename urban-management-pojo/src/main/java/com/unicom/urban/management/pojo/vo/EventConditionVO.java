package com.unicom.urban.management.pojo.vo;

import com.unicom.urban.management.pojo.entity.EventType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 立案条件实体类
 *
 * @author liubozhi
 */
@Data
public class EventConditionVO {

    private String id;

    //private EventTypeVO eventType;

    private String conditionValue;

}
