package com.unicom.urban.management.web.project.home;

import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    public String home() {
        return SystemConstant.PAGE + "/home";
    }

}
