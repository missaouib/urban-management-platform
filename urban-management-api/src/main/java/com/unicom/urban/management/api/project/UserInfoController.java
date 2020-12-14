package com.unicom.urban.management.api.project;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.SecurityUserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    /**
     * 获取当前登录人信息
     */
    @GetMapping("/getuserinfo")
    public SecurityUserBean getUserInfo() {
        SecurityUserBean user = SecurityUtil.getUser();
        user.setPassword(null);
        return user;
    }

}
