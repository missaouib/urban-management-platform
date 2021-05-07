package com.unicom.urban.management.service.activiti;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.util.Date;

public class DateTest {

    @Test
    public void time() {

        String dateStr1 = "2017-03-01 22:33:23";
        Date date1 = DateUtil.parse(dateStr1);

        String dateStr2 = "2017-03-02 23:33:23";
        Date date2 = DateUtil.parse(dateStr2);

        //相差一个月，31天
        long hour = DateUtil.between(date1, date2, DateUnit.HOUR);

        System.out.println(hour);


    }
}
