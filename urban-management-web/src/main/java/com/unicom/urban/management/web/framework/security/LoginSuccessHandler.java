package com.unicom.urban.management.web.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.common.util.IpUtils;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import com.unicom.urban.management.service.logininfo.LoginInfoService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功处理器
 *
 * @author liukai
 */
@Slf4j
@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginInfoService loginInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 记录登录日志
        saveLoginInfo(request, authentication);


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String content = objectMapper.writeValueAsString(Result.success("登录成功!"));
        writer.write(content);
        writer.close();
    }

    private void saveLoginInfo(HttpServletRequest request, Authentication authentication) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();


        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(authentication.getName());
        loginInfo.setIp(getIpAddress(request));
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfoService.save(loginInfo);

    }


    private String getIpAddress(HttpServletRequest request) {
        return IpUtils.getIpAddr(request);
    }


}
