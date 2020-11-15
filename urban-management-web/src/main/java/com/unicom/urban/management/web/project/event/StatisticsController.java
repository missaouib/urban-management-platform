package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/11/6-8:50
 */
@RestController
@RequestMapping("/statistics")
@ResponseResultBody
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @GetMapping("/byEventId")
    public List<StatisticsVO> statistics(String eventId){
        return statisticsService.findByEventId(eventId);
    }
    @GetMapping("/findOpinions")
    public StatisticsVO findOpinions(String statisticsId){
        return statisticsService.findById(statisticsId);
    }
}
