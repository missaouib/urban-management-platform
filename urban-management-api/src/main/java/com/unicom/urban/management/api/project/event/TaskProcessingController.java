package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.FileUploadUtil;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.event.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private FileUploadUtil fileUploadUtil;

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

    @PostMapping("/common/uploads")
    public List<Map<String, Object>> uploadFiles(MultipartFile[] files) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> map = new HashMap<>(3);
            String url = fileUploadUtil.uploadFileToFastDFS(file);
            map.put("url", url);
            map.put("fileName", file.getOriginalFilename());
            list.add(map);
        }
        return list;
    }
}
