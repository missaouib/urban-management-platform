package com.unicom.urban.management.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/11/4-14:17
 */
@Data
public class EventOneVO {

    private String id;

    private String processInstanceId;

    private String eventCode;

    private String eventTypeId;
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
    private String regionIdStr;
    private String regionStr;

    /**
     * 立案条件
     */
    public String ConditionId;
    private String conditionValue;

    /**
     * 案件等级
     */
    private String level;

    /**
     * 处理时限
     */
    private String timeLimitId;
    private String timeLimitStr;

    /**
     * 所属网格
     */
    private String gridId;
    private String girdStr;

    /**
     * 所属社区
     */
    private String community;
    private String communityId;

    /**
     * 所属街道
     */
    private String street;
    private String streetId;

    /**
     * 所属区域
     */
    private String eventRegion;
    private String eventRegionId;


    /**
     * 附件
     */
    private List<Map<String,Object>> file;

    private double x;

    private double y;

    private List<EventButtonVO> eventButtonVOS;

    /**
     * 案件类型
     */
    private String recTypeId;
    private String recTypeStr;


    /**
     * 上报人
     */
    private String userId;
    private String userName;

    /**
     * 诉求人
     */
    private String petitionerName;

    /**
     * 联系方式
     */
    private String petitionerPhone;

    /**
     * 性别
     */
    private String petitionerSex;

    /**
     * 创建时间
     */
    private String cDate;

    /**
     * 问题来源
     */
    private String eventSourceId;
    private String eventSourceStr;

    /**
     * 部门
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 是否自处理
     */
    private Boolean doBySelf;

    /**
     * 延时时限
     */
    private Integer delayedHours;

    /**
     * 申请部门
     */
    private String taskDeptName;

    /**
     * 申请意见
     */
    private String opinions;

    /**
     * 部件编码
     */
    private String objId;

    /**
     * 督办状态
     */
    private String supSts;

}
