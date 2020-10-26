package com.unicom.urban.management.web.project.evaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 专业部门评价
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/evaluate")
public class ProfessionalDepartmentsController {

    @Autowired
    private EventService eventService;

    /**
     * 专业部门评价
     *
     * @return 页面
     */
    @GetMapping("/toProfessionalDepartments")
    public ModelAndView toCellGridRegion() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/professionalDepartments");
    }

}
