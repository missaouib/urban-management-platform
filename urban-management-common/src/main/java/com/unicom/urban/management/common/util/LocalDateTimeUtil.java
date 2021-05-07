package com.unicom.urban.management.common.util;

import cn.hutool.core.date.DateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class LocalDateTimeUtil extends DateUtil {

    /**
     * 一天的分钟数
     */
    public static final long ONE_DAY_MINUTE = 60L * 24;


    /**
     * 计算经过了哪些日期
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 经过了哪些日期
     */
    public static List<LocalDate> between(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        return between(startDate, endDate);
    }

    /**
     * 计算经过了哪些日期
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 经过了哪些日期
     */
    public static List<LocalDate> between(LocalDate startDateTime, LocalDate endDateTime) {
        List<LocalDate> localDateList = new ArrayList<>();
        // 如果为同一天
        if (startDateTime.equals(endDateTime)) {
            localDateList.add(startDateTime);
            return localDateList;
        }
        long day = startDateTime.until(endDateTime, ChronoUnit.DAYS);
        for (long i = 0; i <= day; i++) {
            localDateList.add(startDateTime.plusDays(i));
        }
        return localDateList;
    }


    /**
     * 判断是否为周六周天
     */
    public static boolean isWeekDay(LocalDate localDate) {
        return DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek());
    }

    /**
     * 判断两个日期是否为同一天
     */
    public static boolean isSameDay(LocalDate startDate, LocalDate endDate) {
        return startDate.equals(endDate);
    }

    /**
     * 当前还剩下多少分钟
     */
    public static long remainingMinute(LocalDateTime localDateTime) {
        return ChronoUnit.MINUTES.between(localDateTime, localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static LocalTime getTodayStart() {
        return LocalTime.MIN;
    }

}
