package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.EventFile;
import lombok.Data;

import java.util.List;

/**
 * 事件
 *
 * @author jiangwen
 */
@Data
public class EventDTO {

    private String id;

    private String eventCode;

    private String represent;

    private String location;

    private String userName;

    private String gridId;

    private Integer sts;

    private Integer initSts;

    private String eventTypeId;

    private String timeLimitId;

    private String conditionId;

    private String eventSourceId;

    private double x;

    private double y;

    private String recTypeId;

    private String peopleName;

    private List<EventFile> eventFileList;

    private String userId;

    private String petitionerPhone;

    private String sex;

    private List<String> taskName;

}
