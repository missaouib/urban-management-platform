package com.unicom.urban.management.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author liukai
 */
public class TimeUtil {


    private TimeUtil() {

    }

    /**
     * 获取今天0点
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取当前时间周的第一天
     *
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String time = f.format(cal.getTime());
        try {
            return f.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前月的第一天
     */
    public static Date getSamemonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String time = format.format(c.getTime());
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前年的第一天
     *
     * @param
     * @return
     */
    public static Date getYear() {
        Calendar cale = null;
        cale = Calendar.getInstance();
        int yearNum = cale.get(Calendar.YEAR);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return f.parse(String.valueOf(yearNum) + "-01-01 00:00:00");
        } catch (ParseException e) {
            return null;
        }
    }

    public static List<String> getLastDate(Integer i) {
        List<String> list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        list.add(sdf.format(c.getTime()));
        int lastMonthMaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
        //按格式输出
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        list.add(sdf1.format(c.getTime())); //上月最后一天);
        int actualMinimum = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01  00:00:00");
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), actualMinimum, 23, 59, 59);
        list.add(sdf2.format(c.getTime())); //上月第一天);
        return list;
    }


    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String timeSec = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    private static String timeDate = "yyyy-MM-dd";
    /**
     * HH:mm:ss
     */
    private static String timeHour = "HH:mm:ss";

    //排空处理
    public static String CheckString(Object object) {
        String ret = "";
        if (object != null && !"".equals(object.toString())) {
            ret = object.toString();
        }
        return ret;
    }

    /**
     * yyyyMMdd
     */
    public static String getDateString() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat("yyyyMMdd");
        str = formatStr.format(date);
        return str;
    }

    /**
     * yyyyMMddHHmmss
     */
    public static String getTimeString() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat("yyyyMMddHHmmss");
        str = formatStr.format(date);
        return str;
    }

    /**
     * 获取当前系统时间
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static String getTimeSec() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat(timeSec);
        str = formatStr.format(date);
        return str;
    }

    /**
     * 获取当前系统时间
     * "yyyy-MM-dd"
     */
    public static String getTimeDate() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat(timeDate);
        str = formatStr.format(date);
        return str;
    }

    /**
     * 获取当前系统时间
     * "HH:mm:ss"
     */
    public static String getTimeHour() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat(timeHour);
        str = formatStr.format(date);
        return str;
    }

    /**
     * 把日期字符串中的 - 换为 /
     *
     * @param strDate
     * @return
     */
    public static String getStrDate(String strDate) {
        return strDate.split(" ")[0].replace("-", "/");
    }

    /**
     * @return
     * @Description: 获取当前时间的时间戳
     * @Date:2013-4-7
     */

    public static String getTimestamp() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat formatStr = new SimpleDateFormat(timeHour);
        str = formatStr.format(date);
        String str1 = "";
        Date date1 = new Date();
        SimpleDateFormat formatStr1 = new SimpleDateFormat(timeDate);
        str1 = formatStr1.format(date1);
        return str1.split(" ")[0].replace("-", "") + str.split(" ")[0].replace(":", "");
    }

    /**
     * @return
     * @Description: 获取自定义时间的时间戳
     * @Date:2013-4-7
     */

    public static String getCustomTimestamp(Date date) {
        String str = "";
        SimpleDateFormat formatStr = new SimpleDateFormat(timeHour);
        str = formatStr.format(date);
        String str1 = "";
        SimpleDateFormat formatStr1 = new SimpleDateFormat(timeDate);
        str1 = formatStr1.format(date);
        return str1.split(" ")[0].replace("-", "") + str.split(" ")[0].replace(":", "");
    }

    /**
     * 格式化日期为 STRING yyyy-MM-dd HH:mm:ss
     */
    public static String formatTime(Date date) {
        SimpleDateFormat formatStr = new SimpleDateFormat(timeSec);
        return formatStr.format(date);
    }

    /**
     * 格式化日期为 STRING yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return formatDate(date, timeDate);
    }

    /**
     * 格式化日期为 STRING
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat formatStr = new SimpleDateFormat(pattern);
        return formatStr.format(date);
    }

    /**
     * 将使用预设格式将字符串转为Date
     * yyyy-MM-dd
     *
     * @param strDate
     * @return java.util.Date
     * @throws ParseException
     */
    public static Date parse(String strDate) {
        if (strDate == null || "".equals(strDate)) {
            strDate = null;
        }
        return strDate == null ? null : parse(strDate, timeSec);
    }

    /**
     * 将使用预设格式将字符串转为Date
     * yyyy-MM-dd
     *
     * @param strDate
     * @return java.util.Date
     * @throws ParseException
     */
    public static Date parseShort(String strDate) {
        if (strDate == null || "".equals(strDate)) {
            strDate = null;
        }
        return strDate == null ? null : parse(strDate, timeDate);
    }

    /**
     * 将使用自定义模式的字符串 转为 java.util.Date
     *
     * @param strDate
     * @param pattern
     * @return java.util.Date
     * @throws ParseException
     */
    public static Date parse(String strDate, String pattern) {
        try {
            strDate = "".equals(strDate) ? null : strDate;
            return strDate == null ? null : new SimpleDateFormat(pattern)
                    .parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取上周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static String getLastWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, -7);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        return formatDate(calendar.getTime());
    }

    /**
     * 取本周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static String getThisWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        return formatDate(calendar.getTime());
    }

    /**
     * 取下周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static String getNextWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, +7);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        return formatDate(calendar.getTime());
    }

    /**
     * 取上周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static Date getLastWeekDate(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, -7);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取本周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static Date getThisWeekDate(int week) {
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取下周日期
     *
     * @param week 周几 1-7
     * @return
     */
    public static Date getNextWeekDate(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, +7);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取周日期
     *
     * @param number 从本周起第几周.0:本周, -1:上周, 1下周....
     * @param week   周几 1-7
     * @return
     */
    public static String getWeek(int number, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, +7 * number);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        return formatDate(calendar.getTime());
    }

    /**
     * 取周日期
     *
     * @param number 从本周起第几周.0:本周, -1:上周, 1下周....
     * @param week   周几 1-7
     * @return
     */
    public static Date getWeekDate(int number, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, +7 * number);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + week);
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取某月第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取某月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 取当前年
     *
     * @return
     */
    public static int getThisYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 取当前月
     *
     * @return
     */
    public static int getThisMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 取当前日
     *
     * @return
     */
    public static int getThisDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取当前季度
     *
     * @return
     */
    public static int getThisQuarter() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if (month == 1 || month == 2 || month == 3) {
            return 1;
        } else if (month == 4 || month == 5 || month == 6) {
            return 2;
        } else if (month == 7 || month == 8 || month == 9) {
            return 3;
        } else if (month == 10 || month == 11 || month == 12) {
            return 4;
        }
        return 0;
    }

    /**
     * 取当前日期 yyyy-MM-dd
     *
     * @return
     */
    public static Date getThisDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(timeDate);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 比较日期大小 true 表示 小于 false 表示 大于
     */
    public static boolean compareTo(String strDate) {
        Date date = null;
        Date now = new Date();
        if (strDate != null || !"".equals(strDate.trim())) {
            date = parse(strDate, timeSec);
        }
        return (date == null) ? false
                : (date.compareTo(now) < 0 ? true : false);
    }

    /**
     * @param date
     * @return
     * @Description: 获取 中国日期
     * @Date:Mar 25, 2013
     * @author JIA
     */
    public static String getChinaDate(Date date) {
        //日期转换为日历并获取参数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//Calendar.HOUR为12小时制
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);


        int AM_PM = calendar.get(Calendar.AM_PM);
        String weekDayStr = turnWeekDaytoUpperCase(weekDay - 1);

        //组织数据打印输出
        StringBuffer info = new StringBuffer();
        info.append(year).append("年").append(month + 1).append("月").append(day).append("日")
                .append("星期").append(weekDayStr)
                .append(AM_PM == 0 ? "上午" : "下午")
                .append(hour).append("点").append(minute).append("分");
//        .append(second)

        return info.toString();
    }

    /**
     * 转换日期小写为大写
     *
     * @param weekDay
     * @return
     */
    private static String turnWeekDaytoUpperCase(int weekDay) {
        String weekDayStr = null;
        switch (weekDay) {
            case 1:
                weekDayStr = "一";
                break;
            case 2:
                weekDayStr = "二";
                break;
            case 3:
                weekDayStr = "三";
                break;
            case 4:
                weekDayStr = "四";
                break;
            case 5:
                weekDayStr = "五";
                break;
            case 6:
                weekDayStr = "六";
                break;
            default:
                weekDayStr = "日";
                break;
        }
        return weekDayStr;
    }

    /**
     * 获取该Date 的年
     */
    public static int getYear(Date date) {
        int year = date.getYear();
        return year + 1900;
    }

    /**
     * 获取该Date 的月
     */
    public static int getMonth(Date date) {
        int month = date.getMonth() + 1;
        return month;
    }

    /**
     * 获取该Date 的日
     */
    public static int getDay(Date date) {
        int day = date.getDate();
        return day;
    }

    /**
     * 获取该Date 的星期
     */
    public static int getWeek(Date date) {
        int week = date.getDay();
        return week;
    }

    /**
     * 获取该Date 的季度
     */
    public static int getQuarter(Date date) {
        int month = date.getMonth() + 1;
        return getQuarter(month);
    }

    public static int getQuarter(int month) {
        if (month == 1 || month == 2 || month == 3) {
            return 1;
        } else if (month == 4 || month == 5 || month == 6) {
            return 2;
        } else if (month == 7 || month == 8 || month == 9) {
            return 3;
        } else if (month == 10 || month == 11 || month == 12) {
            return 4;
        }
        return 0;
    }


    /**
     * @param date
     * @param num
     * @return
     * @Description: 获得某日期增加天数的日期
     * @Date:Mar 22, 2013
     */
    public static Date updateDate(Date date, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, num);
//		((Date) c).setDate(date);
//		c.add(Calander.DAY_OF_YEAR,num);
//		return c.getDate();
        return c.getTime();
    }

    /**
     * @param date
     * @param num
     * @return
     * @Description: 获得某日期增加 小时的 日期
     * @Date:Mar 27, 2013
     */
    public static Date updateHour(Date date, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, num);
        return c.getTime();
    }


    /**
     * @param date
     * @param num
     * @return
     * @Description: 获得某日期增加 分总的 日期
     * @Date:Mar 27, 2013
     */
    public static Date updateMINUTE(Date date, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, num);
        return c.getTime();
    }

    /**
     * @param date
     * @param num
     * @return
     * @Description: 修改 日期 + 秒 的日期
     * @Date:Apr 7, 2013
     */
    public static Date updateSECOND(Date date, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, num);
        return c.getTime();
    }

    /**
     * 比较两个字符串类型时间之间相差天数。
     * Startdate - enddate
     *
     * @param startdate
     * @param enddate
     * @return
     * @author Hanh
     */
    public static long daysaPart(String startdate, String enddate) {
        long i = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        ParsePosition pos1 = new ParsePosition(0);
        Date dt1 = sdf.parse(startdate, pos);
        Date dt2 = sdf.parse(enddate, pos1);
        long l = dt1.getTime() - dt2.getTime();
        i = l / (1000 * 60 * 60 * 24);
        return i;
    }

    public static long minPart(String startdate, String enddate) {
        long i = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        ParsePosition pos1 = new ParsePosition(0);
        Date dt1 = sdf.parse(startdate, pos);
        Date dt2 = sdf.parse(enddate, pos1);
        long l = dt1.getTime() - dt2.getTime();
        i = l / (1000 * 60);
        return i;
    }

    public static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        return date.format(fmt);
    }

    /**
     * 获取当前月第一天
     *
     * @return 例如2018-04-01
     * @author liukai
     */
    public static String getFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }


    /**
     * 获取当前月最后一天
     *
     * @return 例如2018-04-30
     * @author liukai
     */
    public static String getEndDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }
}
