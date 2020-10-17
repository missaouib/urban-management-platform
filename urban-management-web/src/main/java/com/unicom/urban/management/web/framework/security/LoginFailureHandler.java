package com.unicom.urban.management.web.framework.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.common.exception.authentication.BadCaptchaException;
import com.unicom.urban.management.common.exception.authentication.CaptchaExpiredException;
import com.unicom.urban.management.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author liukai
 */
@Slf4j
@Component("LoginFailureHandler")
public class LoginFailureHandler extends AbstractAuthenticationHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        // 记录登录日志
        String content = getContentByException(exception);

//        saveLoginInfo(request, response, content);

        handleResponse(request, response, content);


    }


    /**
     * 通过异常得到要返回给前台的内容
     */
    public String getContentByException(AuthenticationException exception) throws JsonProcessingException {
        String content = null;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            content = objectMapper.writeValueAsString(Result.fail("302", "账号或密码错误"));
        }
        if (exception instanceof BadCaptchaException) {
            content = objectMapper.writeValueAsString(Result.fail("302", "验证码错误！请重新输入"));
        }
        if (exception instanceof CaptchaExpiredException) {
            content = objectMapper.writeValueAsString(Result.fail("302", "验证码已过期，情重新输入"));
        }
        return content;
    }

}
