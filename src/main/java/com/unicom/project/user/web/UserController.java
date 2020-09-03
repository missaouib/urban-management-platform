package com.unicom.project.user.web;

import com.unicom.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {


    @GetMapping("/user")
    public ModelAndView user() {
        return new ModelAndView(SystemConstant.PAGE + "/user/user");
    }

    @GetMapping("/user/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/user/add");
    }

}
