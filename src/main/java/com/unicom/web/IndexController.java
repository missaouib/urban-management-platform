package com.unicom.web;

import com.unicom.constant.SystemConstant;
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
