package com.unicom.urban.management.common.util;

import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.entity.Dept;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

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
     *
     * @return 部门ID集合
     */
    public static List<String> getDeptId() {
        return getUser().getDeptList().stream().map(Dept::getId).collect(Collectors.toList());
    }


    /**
     * 获取当前登录人部门名称
     *
     * @return 部门名称集合
     */
    public static List<String> getDeptName() {
        return getUser().getDeptList().stream().map(Dept::getDeptName).collect(Collectors.toList());
    }


//    public static String getUserId() {
//        User user = getUser();
//        return
//    }

}
