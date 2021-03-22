package com.unicom.urban.management.pojo.entity.time;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.entity.BaseEnum;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 计时管理方案实体类
 *
 * @author liukai
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "time_plan")
public class TimePlan {

    private String id;

    /**
     * 方案开始时间
     */
    private LocalDateTime startTime;

    /**
     * 方案结束时间
     */
    private LocalDateTime endTime;

    private List<Calendar> calendarList;

    private List<TimeScheme> timeSchemeList;

    /**
     * 状态
     */
    private Status sts;

    @Override
    public String toString() {
        return "TimePlan{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", calendarList=" + calendarList +
                ", sts=" + sts +
                '}';
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Convert(converter = StatusConverter.class)
    public Status getSts() {
        return sts;
    }

    public void setSts(Status sts) {
        this.sts = sts;
    }

    @JoinColumn(name = "time_plan_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(List<Calendar> calendarList) {
        this.calendarList = calendarList;
    }

    @JoinColumn(name = "time_plan_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<TimeScheme> getTimeSchemeList() {
        return timeSchemeList;
    }

    public void setTimeSchemeList(List<TimeScheme> timeSchemeList) {
        this.timeSchemeList = timeSchemeList;
    }


    /**
     * 一定要实现BaseEnum
     *
     * @author liukai
     */
    public enum Status implements BaseEnum {

        ENABLE(0, "启用"),
        DISABLE(1, "未启用");

        Status(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        private Integer value;
        private String description;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class StatusConverter implements AttributeConverter<Status, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Status attribute) {
            if (attribute == null) {
                throw new BusinessException("Unknown status text  ");
            }
            return attribute.getValue();

        }

        @Override
        public Status convertToEntityAttribute(Integer dbData) {
            for (Status status : Status.values()) {
                if (status.getValue().equals(dbData)) {
                    return status;
                }
            }
            throw new BusinessException("Unknown status text : " + dbData);
        }
    }

}

