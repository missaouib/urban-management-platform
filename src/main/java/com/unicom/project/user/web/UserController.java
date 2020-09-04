package com.unicom.project.user.web;

import com.unicom.constant.SystemConstant;
import com.unicom.framework.Result;
import com.unicom.project.user.entity.User;
import com.unicom.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
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
    public Result saveUser(User user) {
        userService.saveUser(user);
        return Result.success();
    }


}
