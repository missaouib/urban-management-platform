package com.unicom.urban.management.web.project.publish;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/toPublishList")
    public ModelAndView toGridSave() {
        return new ModelAndView(SystemConstant.PAGE + "/publish/publishList");
    }

}
