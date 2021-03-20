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
public class Calendar {

    private String id;

    /**
     * 是否为工作日
     */
    private WorkDayMark workDayMark;


    private LocalDate calendar;

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

}
