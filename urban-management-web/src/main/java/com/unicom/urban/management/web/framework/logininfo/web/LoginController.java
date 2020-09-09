package com.unicom.urban.management.web.framework.logininfo.web;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (alreadyLogin()) {
            return "redirect:/index";
        }
        return SystemConstant.PAGE + "/login";
    }


    private boolean alreadyLogin() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

}
