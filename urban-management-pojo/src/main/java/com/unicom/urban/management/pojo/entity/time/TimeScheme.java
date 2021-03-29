package com.unicom.urban.management.pojo.entity.time;

import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "time_scheme")
public class TimeScheme {

    private String id;

    private LocalTime startTime;

    private LocalTime endTime;

    private TimePlan timePlan;


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    @JoinColumn(name = "time_plan_id")
    @ManyToOne(fetch = FetchType.LAZY)
    public TimePlan getTimePlan() {
        return timePlan;
    }

    public void setTimePlan(TimePlan timePlan) {
        this.timePlan = timePlan;
    }

}
