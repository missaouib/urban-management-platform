package com.unicom.urban.management.pojo.entity.time;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.entity.BaseEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 计时管理 日历记录实体类
 *
 * @author liukai
 */
@Entity
@Table(name = "time_calendar")
public class Day {

    private String id;

    /**
     * 工作日还是节假日
     */
    private WorkDayMark workDayMark;

    /**
     * 是否上班
     */
    private Work work;


    private LocalDate calendar;

    private TimePlan timePlan;

    /**
     * 是否为工作日
     */
    @Transient
    public boolean isWorkDay() {
        return WorkDayMark.WORKDAY.equals(this.workDayMark);
    }


    /**
     * 上班
     */
    @Transient
    public boolean isWork() {
        return Work.WORK.equals(this.work);
    }

    @Transient
    public boolean isNotWork() {
        return Work.NON_WORK.equals(this.work);
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

    @Convert(converter = WorkDayConverter.class)
    public WorkDayMark getWorkDayMark() {
        return workDayMark;
    }

    public void setWorkDayMark(WorkDayMark workDayMark) {
        this.workDayMark = workDayMark;
    }

    public LocalDate getCalendar() {
        return calendar;
    }

    public void setCalendar(LocalDate calendar) {
        this.calendar = calendar;
    }

    @Convert(converter = WorkConverter.class)
    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public TimePlan getTimePlan() {
        return timePlan;
    }

    public void setTimePlan(TimePlan timePlan) {
        this.timePlan = timePlan;
    }

    public enum WorkDayMark implements BaseEnum {

        WORKDAY(0, "工作日"),
        NON_WORKDAY(1, "非工作日");

        WorkDayMark(Integer value, String description) {
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

    public static class WorkDayConverter implements AttributeConverter<WorkDayMark, Integer> {

        @Override
        public Integer convertToDatabaseColumn(WorkDayMark attribute) {
            if (attribute == null) {
                throw new BusinessException("Unknown workDayMark text  ");
            }
            return attribute.getValue();

        }

        @Override
        public WorkDayMark convertToEntityAttribute(Integer dbData) {
            for (WorkDayMark dayMark : WorkDayMark.values()) {
                if (dayMark.getValue().equals(dbData)) {
                    return dayMark;
                }
            }
            throw new BusinessException("Unknown workDayMark text : " + dbData);
        }
    }


    public enum Work implements BaseEnum {

        WORK(0, "上班"),
        NON_WORK(1, "休息");

        Work(Integer value, String description) {
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


    public static class WorkConverter implements AttributeConverter<Work, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Work attribute) {
            if (attribute == null) {
                throw new BusinessException("Unknown workDayMark text  ");
            }
            return attribute.getValue();

        }

        @Override
        public Work convertToEntityAttribute(Integer dbData) {
            for (Work work : Work.values()) {
                if (work.getValue().equals(dbData)) {
                    return work;
                }
            }
            throw new BusinessException("Unknown workDayMark text : " + dbData);
        }
    }

}
