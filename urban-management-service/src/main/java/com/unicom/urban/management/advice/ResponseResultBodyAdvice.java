package com.unicom.urban.management.advice;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.enums.ErrorCodeEnum;
import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.HttpUtil;
import com.unicom.urban.management.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;


/**
 * JSON增强 把json用Result封装起来
 *
 * @author liukai
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("all")
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseResultBody.class;


    /**
     * 判断类或者方法是否使用了 @ResponseResultBody
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止重复包裹的问题出现
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }

    /**
     * Exception异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result exception(Exception exception) {
        log.error("exception", exception);
        return Result.fail(ErrorCodeEnum.SERVER_ERROR);
    }


    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Object runtimeException(RuntimeException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("系统发生异常", exception);
        if (HttpUtil.isAjaxRequest(request)) {
            return Result.fail(ErrorCodeEnum.SERVER_ERROR);
        }
        return new ModelAndView(SystemConstant.PAGE + "/error/500");
    }

    /**
     * 系统业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result businessException(BusinessException exception) {
        return Result.fail(ErrorCodeEnum.SERVER_ERROR.getCode(), ExceptionUtils.getStackTrace(exception));
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result validatedBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.fail(ErrorCodeEnum.FIELD_VALIDATED_FAIL.getCode(), ErrorCodeEnum.FIELD_VALIDATED_FAIL.getMessage() + message);
    }


    /**
     * 数据校验异常
     */
    @ExceptionHandler(DataValidException.class)
    public Result dataValidException(DataValidException e) {
        return Result.fail(400, e.getMessage());
    }


}
