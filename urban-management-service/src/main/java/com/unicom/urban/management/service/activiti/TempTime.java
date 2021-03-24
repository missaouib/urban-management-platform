package com.unicom.urban.management.service.activiti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempTime {

    private LocalTime startTime;

    private LocalTime endTime;


    /**
     * 这个时间段是否工作
     */
    private boolean flag;

    private Long totalMinute;


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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getTotalMinute() {
        return totalMinute;
    }

    public void setTotalMinute(Long totalMinute) {
        this.totalMinute = totalMinute;
    }
}
