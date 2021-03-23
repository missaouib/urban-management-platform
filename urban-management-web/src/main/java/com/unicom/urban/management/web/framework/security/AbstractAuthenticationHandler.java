package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.common.util.IPUtil;
import com.unicom.urban.management.common.util.ResponseUtil;
import com.unicom.urban.management.common.util.UserAgentUtil;
import com.unicom.urban.management.pojo.entity.LoginLog;
import com.unicom.urban.management.service.log.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        ResponseUtil.write(response, responseBody, MediaType.APPLICATION_JSON_VALUE);
    }


    /**
     * 记录登录日志
     *
     * @param message 是否登录成功
     */
    protected void loginLog(HttpServletRequest request, HttpServletResponse response, Integer message) {
        String os = UserAgentUtil.getOperatingSystem(request);
        String browser = UserAgentUtil.getBrowser(request);
        String ip = IPUtil.getIpAddress(request);

        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(request.getParameter("username"));
        loginLog.setIp(ip);
        loginLog.setOs(os);
        loginLog.setBrowser(browser);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        loginLogService.save(loginLog);

    }


}
