package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.event.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/11/5-18:30
 */
@RestController
@RequestMapping("/api/task")
@ResponseResultBody
public class TaskProcessingController {
    @Autowired
    private TaskProcessingService taskProcessingService;

    @Autowired
    private DeptService deptService;

    /**
     * 授权审批 案件处理 挂账恢复
     *
     * @param statisticsDTO 参数
     */
    @PostMapping("/processing")
    public void processing(@RequestBody StatisticsDTO statisticsDTO){
        taskProcessingService.handle(statisticsDTO.getEventId(),statisticsDTO.getButtonId(),statisticsDTO);
    }


    @GetMapping("/dept")
    public List<DeptVO> dept(){
        return deptService.getAll();
    }
}
