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

    /**
     * 获取当前登录人的用户ID
     */
    public static String getUserId() {
        return getUser().getId();
    }


    /**
     * 获取当前登录人的登录账号
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 获取当前登录人部门ID
     */
    public static String getDeptId() {
        return getUser().getDeptId();
    }


    /**
     * 获取当前登录人部门名称
     */
    public static String getDeptName() {
        return getUser().getDeptName();
    }


//    public static String getUserId() {
//        User user = getUser();
//        return
//    }

}
