package com.unicom.urban.management.api.project.bigscreen;

import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

/**
 * 大屏接口
 *
 * @author 顾志杰
 * @date 2021/3/29-9:25
 */
@RestController
@RequestMapping("/api/bigscreen")
public class BigScreenController {

    @Autowired
    private StatisticsService statisticsService;


    /**
     * 首页数据展示
     *
     * @return 数据
     */
    @GetMapping("/getIndexValueByWeek")
    public Result getIndexValueByWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusYears(5).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Map<String, Object> indexValueByWeek = statisticsService.getIndexValueByWeek(monday,sunday);
        return Result.success(indexValueByWeek);
    }
}
