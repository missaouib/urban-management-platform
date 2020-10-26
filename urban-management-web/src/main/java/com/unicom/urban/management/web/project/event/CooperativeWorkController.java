package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 协同工作子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class CooperativeWorkController {

    @Autowired
    private EventService eventService;

    @GetMapping("/toCooperativeWorkList")
    public ModelAndView toCooperativeWorkList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/cooperativeWork/list");
    }

}
