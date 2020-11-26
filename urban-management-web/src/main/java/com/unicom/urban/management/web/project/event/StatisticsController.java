package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 高发区域（大屏）
     * @return
     */
    @GetMapping("/findHotGrid")
    public List<Map<String,Object>> findHotGrid(String time){
        return statisticsService.findHotGrid(time);
    }

    /**
     * 个人信息（首页）
     * @return
     */
    @GetMapping("/personInfo")
    public String[] personInfo(){
        Map<String,Object>  personMap = statisticsService.findPersonInfo();
        String[] arr = new String[4];
        arr[0] = personMap.get("roleName").toString();
        arr[1] = personMap.get("takeCase").toString();
        arr[2] = personMap.get("instCase").toString();
        arr[3] = personMap.get("closeCase").toString();
        return arr;
    }
    /**
     * 问题来源（大屏）
     * @return
     */
    @GetMapping("/findEventSource")
    public Map<String,String> findEventSource(String time){
        return statisticsService.findEventSource(time);
    }
}
