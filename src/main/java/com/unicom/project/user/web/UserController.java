package com.unicom.project.user.web;

import com.unicom.common.constant.SystemConstant;
import com.unicom.framework.annotations.ResponseResultBody;
import com.unicom.project.user.entity.User;
import com.unicom.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ResponseResultBody
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ModelAndView user() {
        return new ModelAndView(SystemConstant.PAGE + "/user/user");
    }

    @GetMapping("/user/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/user/add");
    }

    @PostMapping("/user/save")
    public void saveUser(User user) {
        userService.saveUser(user);
    }


}