package com.unicom.framework.web;

import com.unicom.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginInfoController {


    @GetMapping("/logininfo")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/logininfo/list");
    }


}
