package com.unicom.urban.management.web.project.categoryconfiguration;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 类别配置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/categoryConfiguration")
public class CategoryConfigurationController {

    @GetMapping("/toList")
    public ModelAndView grid() {
        return new ModelAndView(SystemConstant.PAGE + "/categoryConfiguration/list");
    }

}
