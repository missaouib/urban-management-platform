package com.unicom.urban.management.common.error;

import com.unicom.urban.management.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 定制错误处理页面
 *
 * @author liukai
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController extends AbstractErrorController {


    public CustomizeErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String handlerError(HttpServletRequest request) {
        int status = getStatus(request).value();
        if (status == HttpStatus.NOT_FOUND.value()) {
            return SystemConstant.PAGE + "/error/404";
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return SystemConstant.PAGE + "/error/500";
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
