package com.unicom.urban.management.web.project.home;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.home.HomeService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
 * @date 2020/11/25-9:17
 */
@RestController
@ResponseResultBody
@RequestMapping("/home")
public class HomeRestController {


    @Autowired
    private HomeService homeService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private EventService eventService;


    @GetMapping("/unitCount")
    public List<Map<String, Object>> unitCount() {
        return homeService.eventTypeCount("部件");
    }

    @GetMapping("/eventCount")
    public List<Map<String, Object>> eventCount() {
        return homeService.eventTypeCount("事件");
    }

    @GetMapping("/getIndexValueByWeek")
    public Result getIndexValueByWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Map<String, Object> indexValueByWeek = statisticsService.getIndexValueByWeek(monday,sunday);
        return Result.success(indexValueByWeek);
    }

    /**
     * 公众信息待办列表
     *
     * @return list
     */
    @GetMapping("/eventListByToDoWithIndex")
    public Page<EventVO> eventListByToDoWithIndex(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setUserId(SecurityUtil.getUserId());
        return eventService.search(eventDTO, pageable);
    }

}
