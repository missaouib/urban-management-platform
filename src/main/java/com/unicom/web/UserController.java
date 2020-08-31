package com.unicom.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {


    @GetMapping("/user")
    public ModelAndView user() {
        return new ModelAndView("user/user");
    }

}
