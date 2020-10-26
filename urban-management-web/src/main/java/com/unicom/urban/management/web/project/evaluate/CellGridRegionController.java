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
 * 单元网格区域评价
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/evaluate")
public class CellGridRegionController {

    @Autowired
    private EventService eventService;

    /**
     * 单元网格区域评价
     *
     * @return 页面
     */
    @GetMapping("/toCellGridRegion")
    public ModelAndView toCellGridRegion() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/cellGridRegion");
    }

}
