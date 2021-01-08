package com.unicom.urban.management.web;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class TestLocalDate {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        //当前月
        //本月第一天
        LocalDate currFirstDay = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        System.out.println(currFirstDay);
        //本月的最后一天
        LocalDate currLastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(currLastDay);
        //上个月
        if(localDate.getMonthValue() == 1){
            LocalDate lastStartDay = LocalDate.of(localDate.getYear() -1, 12, 1);
            LocalDate lastEndDay = lastStartDay.with(TemporalAdjusters.lastDayOfMonth());
            System.out.println(lastStartDay);
            System.out.println(lastEndDay);
        } else {
            LocalDate lastStartDay = LocalDate.of(localDate.getYear(), localDate.getMonthValue() - 1, 1);
            LocalDate lastEndDay = lastStartDay.with(TemporalAdjusters.lastDayOfMonth());
        }

    }

}
