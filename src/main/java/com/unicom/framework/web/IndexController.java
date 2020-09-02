package com.unicom.framework.web;

import com.unicom.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liukai
 */
@Controller
public class IndexController {

    @GetMapping({SystemConstant.URL_PREFIX + "/index", SystemConstant.URL_PREFIX + "/"})
    public String index() {
        return SystemConstant.PAGE + "/index";
    }

}
