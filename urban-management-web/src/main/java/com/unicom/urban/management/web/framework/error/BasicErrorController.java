package com.unicom.urban.management.web.framework.error;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 定制错误处理页面
 *
 * @author liukai
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController implements ErrorController {


    @GetMapping(produces = {"text/html"})
    public String handlerError(HttpServletRequest request) {
        int status = getStatus(request).value();
        if (status == 404) {
            return SystemConstant.PAGE + "/error/404";
        } else if (status == 500) {
            return SystemConstant.PAGE + "/error/500";
        }
        return null;
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }


}
