package com.unicom.urban.management.web.framework.security;

import cn.hutool.extra.servlet.ServletUtil;
import com.unicom.urban.management.pojo.entity.LoginLog;
import com.unicom.urban.management.service.logininfo.LoginLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * @author liukai
 */
public abstract class AbstractAuthenticationHandler {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 返回给前台JSON
     */
    protected void handleResponse(HttpServletRequest request, HttpServletResponse response, String responseBody) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);
        writer.close();
    }


    /**
     * 记录登录日志
     *
     * @param message 是否登录成功
     */
    protected void loginLog(HttpServletRequest request, HttpServletResponse response, Integer message) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        String ip = getIpAddress(request);


        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(request.getParameter("username"));
        loginLog.setIp(ip);
        // IPv6本地地址
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            loginLog.setIp("127.0.0.1");
        }
        loginLog.setOs(os);
        loginLog.setBrowser(browser);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        loginLogService.save(loginLog);

    }


    private String getIpAddress(HttpServletRequest request) {
        return ServletUtil.getClientIP(request);
    }

}
