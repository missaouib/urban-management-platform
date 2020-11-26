package com.unicom.urban.management.web.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器
 *
 * @author liukai
 */
@Slf4j
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends AbstractAuthenticationHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        try {
            loginLog(request, response, 0);
        } catch (Exception e) {
            log.error("登录成功 记录登录日志发成异常", e);
        }
        handleResponse(request, response, objectMapper.writeValueAsString(Result.success()));

    }


}
