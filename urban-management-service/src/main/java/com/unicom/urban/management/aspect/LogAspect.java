package com.unicom.urban.management.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.util.IPUtil;
import com.unicom.urban.management.common.util.RequestContext;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.common.util.UserAgentUtil;
import com.unicom.urban.management.pojo.entity.OperateLog;
import com.unicom.urban.management.service.log.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 系统日志切面
 *
 * @author liukai
 */
@Slf4j
@Aspect
@Component
public class LogAspect {


    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("@annotation( com.unicom.urban.management.common.annotations.Log)")
    public void logPointCut() {
    }

    @AfterReturning("logPointCut()")
    public void saveLog(JoinPoint joinPoint) throws JsonProcessingException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Log annotationLog = method.getAnnotation(Log.class);

        Object[] args = joinPoint.getArgs();
        String params = objectMapper.writeValueAsString(args);

        HttpServletRequest request = RequestContext.getRequest();
        String os = UserAgentUtil.getOperatingSystem(request);
        String browser = UserAgentUtil.getBrowser(request);

        OperateLog operateLog = new OperateLog();
        if (annotationLog != null) {
            String name = annotationLog.name();
            operateLog.setOperateName(name);
        }
        operateLog.setUsername(SecurityUtil.getUsername());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setIp(IPUtil.getIpAddress(request));
        operateLog.setOs(os);
        operateLog.setBrowser(browser);
        operateLog.setParams(params);
        try {
            operateLogService.save(operateLog);
        } catch (Exception e) {
            log.error("记录操作日志出现异常:", e);
        }
    }


}
