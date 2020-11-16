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

    /**
     * search 问题描述
     */
    private String represent;

    /**
     * search 所在网格
     */
    private String grid;

    private String location;

    private String userName;

    private String gridId;

    private Integer sts;

    private Integer initSts;

    /**
     * search 问题类型
     */
    private String eventTypeId;

    private String timeLimitId;

    /**
     * search 立案条件
     */
    private String conditionId;

    /**
     * search 事件来源
     */
    private String eventSourceId;

    private Double x;

    private Double y;

    private String recTypeId;

    private String peopleName;

    private List<EventFile> eventFileList;

    private String userId;

    private String petitionerPhone;

    private String sex;

    private List<String> taskName;

    private List<String> taskId;

    private String button;

    /**
     * search 立案区域
     */
    private String eventCondition;

    /**
     * search 计时等级
     */
    private String timeType;

    private Boolean doBySelf;


    private List<String> close;

    /**
     * 已办列表
     */
    private String me;

    /**
     * 挂账
     */
    private String hang;

    /**
     * 作废
     */
    private String cancel;

    /**
     * 图片集合
     */
    private List<String> imageUrlList;
    /**
     * 处置后图片
     */
    private List<String> imageUrlListAfter;

    /**
     * 不予受理 受理员
     */
    private Integer notOperate;


}
