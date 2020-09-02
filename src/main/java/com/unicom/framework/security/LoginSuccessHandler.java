package com.unicom.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.framework.IpUtils;
import com.unicom.framework.Result;
import com.unicom.framework.entity.LoginInfo;
import com.unicom.framework.service.LoginInfoService;
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
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(authentication.getName());
        loginInfo.setIp(getIpAddress(request));
        loginInfoService.save(loginInfo);
    }


    private String getIpAddress(HttpServletRequest request) {
        return IpUtils.getIpAddr(request);
    }


}
