package com.unicom.urban.management.common.util;

import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liukai
 */
public abstract class IPUtil {


    public static String getIpAddress(HttpServletRequest request) {
        String ip = ServletUtil.getClientIP(request);
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        }
        return ip;
    }


}
