package com.unicom.urban.management.pojo.entity.time;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.entity.BaseEnum;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 计时管理方案实体类
 *
 * @author liukai
 */
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "time_plan")
public class TimePlan {

    private String id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案开始时间
     */
    private LocalDate startTime;

    /**
     * 方案结束时间
     */
    private LocalDate endTime;

    private List<Day> dayList = new ArrayList<>();


    private List<TimeScheme> timeSchemeList = new ArrayList<>();


    /**
     * 状态
     */
    private Status sts;

    public TimePlan() {
    }

    public TimePlan(String id) {
        this.id = id;
    }

    @Transient
    public void addDay(Day day) {
        this.dayList.add(day);
    }

    @Transient
    public void addTimeScheme(TimeScheme timeScheme) {
        this.timeSchemeList.add(timeScheme);
    }

    @Transient
    public void cleanTimeScheme() {
        this.timeSchemeList.clear();
    }


    /**
     * 判断此日期是否已经设置
     */
    @Transient
    public boolean hasDay(LocalDate localDate) {
        return dayList.stream().anyMatch(day -> day.getCalendar().equals(localDate));
    }

    @Transient
    public void enable() {
        this.sts = Status.ENABLE;
    }

    @Transient
    public void disable() {
        this.sts = Status.DISABLE;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
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
    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }

    @OrderBy("startTime ASC")
    @JoinColumn(name = "time_plan_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
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

