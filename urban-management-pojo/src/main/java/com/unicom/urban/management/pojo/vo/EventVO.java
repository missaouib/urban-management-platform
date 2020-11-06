package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.pojo.entity.EventFile;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件VO
 *
 * @author jiangwen
 */
@Data
public class EventVO {

    private String id;

    private String userName;

    private String represent;

    private String eventTypeName;

    private Double x;

    private Double y;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private List<EventFile> eventFileList;
    /**
     * 事件来源id
     */
    private String eventSourceId;
    /**
     * 事件来源回显
     */
    private String eventSourceName;
    /**
     * 案件类型
     */
    private String recTypeName;
    /**
     * 案件编号
     */
    private String eventCode;

    private String taskId;


}
