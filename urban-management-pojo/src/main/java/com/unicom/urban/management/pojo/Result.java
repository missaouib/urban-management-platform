package com.unicom.urban.management.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.unicom.urban.management.common.enums.ErrorCodeEnum;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 接口返回封装类
 *
 * @author liukai
 */
@Getter
public class Result {

    /**
     * 处理结果code
     */
    private Integer code;

    /**
     * 处理结果描述信息
     */
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time;

    public Result() {
        this.time = LocalDateTime.now();
    }

    /**
     * @param errorType
     */
//    public Result(ErrorType errorType) {
//        this.code = errorType.getCode();
//        this.time = ZonedDateTime.now().toInstant();
//    }

//    public Result(ErrorType errorType, String message) {
//        this(errorType);
//        this.message = message;
//    }

    /**
     * 内部使用，用于构造成功的结果
     */
    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = LocalDateTime.now();
    }

    /**
     * 内部使用，用于构造成功的结果
     */
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    /**
     * 快速创建成功结果并返回结果数据
     */
    public static Result success(Object data) {
        return new Result(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 快速创建成功结果
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     */
//    public static Result fail() {
//        return new Result(SystemErrorType.SYSTEM_ERROR);
//    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException
     * @return Result
     */
//    public static Result fail(BaseException baseException) {
//        return fail(baseException, null);
//    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param message
     * @return Result
     */
//    public static Result fail(BaseException baseException, String message) {
//        return new Result(baseException.getErrorType(), message);
//    }

    /**
     * 系统异常类并返回结果数据
     */
    public static Result fail(ErrorCodeEnum errorCodeEnum) {
        return new Result(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }

    public static Result fail(Integer code, String message) {
        return new Result(code, message);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @return Result
     */
//    public static Result fail(ErrorType errorType) {
//        return Result.fail(errorType, null);
//    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param message
     * @return Result
     */
//    public static Result fail(String message) {
//        return new Result(SystemErrorType.SYSTEM_ERROR, message);
//    }


//    public static Result error(String message) {
//        return new Result(SystemErrorType.SYSTEM_ERROR, message);
//    }


}
