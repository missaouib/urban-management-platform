package com.unicom.urban.management.api.project.user;

import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.user.UserService;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.pojo.SecurityUserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录人信息
     */
    @GetMapping("/getuserinfo")
    public SecurityUserBean getUserInfo() {
        SecurityUserBean user = SecurityUtil.getUser();
        user.setPassword(null);
        return user;
    }

    /**
     * 获取当前登录人信息
     */
    @GetMapping("/user/user")
    public Result getUser() {
        SecurityUserBean user = SecurityUtil.getUser();
        UserVO user1 = userService.getUser(user.getId());
        user1.setUsername(null);
        return Result.success(user1);
    }

}
