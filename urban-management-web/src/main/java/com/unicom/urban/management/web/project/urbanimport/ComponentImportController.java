package com.unicom.urban.management.web.project.urbanimport;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 部件导入管理
 *
 * @author liubozhi
 */
@RestController
@ResponseResultBody
public class ComponentImportController {
    @GetMapping("/componentImport")
    public ModelAndView componentImport() {
        return new ModelAndView(SystemConstant.PAGE + "/urbanImport/componentImport");
    }
}
