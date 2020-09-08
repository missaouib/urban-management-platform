package com.unicom.project.user.web;

import com.unicom.common.constant.SystemConstant;
import com.unicom.framework.annotations.ResponseResultBody;
import com.unicom.project.user.entity.User;
import com.unicom.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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


    @GetMapping("/user/search")
    public Page<User> search(User user, @PageableDefault Pageable pageable) {
        return userService.search(user, pageable);
    }

    @PostMapping("/user/save")
    public void saveUser(@Valid User user) {
        userService.saveUser(user);
    }


}
