package com.unicom.urban.management.web.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.authentication.UsernameMoreThanOneException;
import com.unicom.urban.management.common.util.HttpUtil;
import com.unicom.urban.management.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liukai
 */
@Slf4j
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (HttpUtil.isAjaxRequest(request)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            if (authException instanceof UsernameMoreThanOneException) {
                Result responseBody = Result.fail(500, "用户数据出现异常,请联系管理员");
                writer.write(objectMapper.writeValueAsString(responseBody));
                writer.close();
                return;
            }

            Result responseBody = Result.fail(403, "认证已失效,需要重新登录");

            writer.write(objectMapper.writeValueAsString(responseBody));
            writer.close();
        } else {
            response.sendRedirect(SystemConstant.DEFAULT_LOGIN_PAGE);
        }
    }

}
