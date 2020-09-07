package com.unicom.framework.logininfo.web;

import com.unicom.common.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return SystemConstant.PAGE + "/home";
    }

}
