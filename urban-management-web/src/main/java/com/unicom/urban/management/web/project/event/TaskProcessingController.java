package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.service.event.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 顾志杰
 * @date 2020/11/5-18:30
 */
@RestController
@RequestMapping("/task")
public class TaskProcessingController {
    @Autowired
    private TaskProcessingService taskProcessingService;

    @PostMapping("/processing")
    public void processing(@RequestBody StatisticsDTO statisticsDTO){
        taskProcessingService.handle(statisticsDTO.getEventId(),null,statisticsDTO.getButtonId(),statisticsDTO);
    }
}
