package com.unicom.urban.management.web.project.publish;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.PublishVO;
import com.unicom.urban.management.pojo.vo.RecordVO;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 地图发布
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    private PublishService publishService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/toPublishList")
    public ModelAndView toGridSave() {
        return new ModelAndView(SystemConstant.PAGE + "/publish/publishList");
    }

    @GetMapping("/getPublishList")
    public List<PublishVO> getPublishList() {
        return publishService.search();
    }

    @GetMapping("/getPublishOne/{publishId}")
    public List<RecordVO> getPublishOne(@PathVariable String publishId) {
        return recordService.findAllByPublishId(publishId);
    }

    @PostMapping("/layerPublish")
    public String layerPublish(String id, String type) {
        publishService.layerPublish(id, type);
        return "发布成功";
    }

}
