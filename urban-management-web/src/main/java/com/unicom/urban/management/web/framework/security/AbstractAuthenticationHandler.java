package com.unicom.urban.management.web.framework.security;

import cn.hutool.extra.servlet.ServletUtil;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import com.unicom.urban.management.service.logininfo.LoginInfoService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liukai
 */
public abstract class AbstractAuthenticationHandler {

    @Autowired
    private LoginInfoService loginInfoService;

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
    protected void saveLoginInfo(HttpServletRequest request, HttpServletResponse response, String message) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();


        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(request.getParameter("username"));
        loginInfo.setIp(getIpAddress(request));
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setMessage(message);
        loginInfoService.save(loginInfo);

    }


    private String getIpAddress(HttpServletRequest request) {
        return ServletUtil.getClientIP(request);
    }

}
