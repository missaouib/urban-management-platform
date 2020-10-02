package com.unicom.urban.management.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * 获取当前登录人工具类
 *
 * @author liukai
 */
public abstract class SecurityUtil {


    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

//    public static String getUserId() {
//        User user = getUser();
//        return
//    }

}
