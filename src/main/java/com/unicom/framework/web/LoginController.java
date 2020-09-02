package com.unicom.framework.web;

import com.unicom.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录控制器
 *
 * @author liukai
 */
@Controller
public class LoginController {

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String login() {
        return SystemConstant.PAGE + "/login";
    }


}
