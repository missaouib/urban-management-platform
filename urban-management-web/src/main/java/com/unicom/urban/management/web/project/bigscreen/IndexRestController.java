package com.unicom.urban.management.web.project.bigscreen;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.bigscreen.IndexService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/11/25-14:43
 */
@RequestMapping("/bigscreen")
@RestController
@ResponseResultBody
public class IndexRestController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/count")
    public Map<String,Object> count(String timeType){
        return indexService.count(timeType);
    }

    @GetMapping("/showPoints")
    public List<Map<String, String>> showPoints(String timeType,String showType){
        return indexService.showPoints(timeType,showType);
    }

    /**
     * 大屏数据展示
     * report 上报数
     * inst 立案数
     * dispatch 派遣数
     * close 结案数
     * operate 上报数
     * dispose 处置数
     * @return 数据
     */
    @GetMapping("/getIndexValue")
    public Result getIndexValueByWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusYears(5).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Map<String, Object> indexValueByWeek = statisticsService.getIndexValueByWeek(monday,sunday);
        return Result.success(indexValueByWeek);
    }
}
