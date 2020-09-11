package com.unicom.urban.management.web.framework.logininfo.web;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liukai
 */
@Controller
public class IndexController {

    @GetMapping({"/index", "/"})
    public String index() {
        return SystemConstant.PAGE + "/index";
    }

}
