package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 立案区域Controller
 * @author liubozhi
 */
@RestController
@RequestMapping("/eventCondition")
@ResponseResultBody
public class EventConditionController {
    @GetMapping("/toEventConditionList")
    public ModelAndView toEventConditionList() {
        return new ModelAndView(SystemConstant.PAGE + "/eventCondition/area/area");
    }
}
