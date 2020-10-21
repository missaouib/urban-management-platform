package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 监督受理子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class SupervisionAcceptanceController {

    @GetMapping("/toSupervisionAcceptanceList")
    public ModelAndView toSupervisionAcceptanceList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/list");
    }

    @GetMapping("/toSupervisionAcceptanceSave")
    public ModelAndView toSupervisionAcceptanceSave() {
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/save");
    }

    @GetMapping("/toSupervisionAcceptanceUpdate/{id}")
    public ModelAndView toSupervisionAcceptanceUpdate(@PathVariable String id) {
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/update");
    }

}
