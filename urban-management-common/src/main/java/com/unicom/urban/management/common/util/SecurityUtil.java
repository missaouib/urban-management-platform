package com.unicom.urban.management.common.util;

import com.unicom.urban.management.pojo.SecurityUserBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前登录人工具类
 *
 * @author liukai
 */
public abstract class SecurityUtil {


    public static SecurityUserBean getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (SecurityUserBean) authentication.getPrincipal();
    }

    public static String getUserId() {
        return getUser().getId();
    }

    public static String getUsername() {
        return getUser().getUsername();
    }


//    public static String getUserId() {
//        User user = getUser();
//        return
//    }

}
