package com.unicom.urban.management.web.project.home;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.SecurityUtil;
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
        Map<String, Object> indexValueByWeek = statisticsService.getIndexValueByWeek();
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
