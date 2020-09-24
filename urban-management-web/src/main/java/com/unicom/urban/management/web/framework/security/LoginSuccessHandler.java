package com.unicom.urban.management.web.framework.security;

import cn.unicom.hlj.snr.gem.ruby.repository.Repository;
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

    @Autowired
    protected Repository repository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String content = objectMapper.writeValueAsString(Result.success());

        // 记录登录日志
        saveLoginInfo(request, response, content);

        handleResponse(request, response, content);

    }


}
