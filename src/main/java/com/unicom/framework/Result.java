package com.unicom.framework;

//import com.cucsi.gis.framework.exception.BaseException;
//import com.cucsi.gis.framework.exception.ErrorType;
//import com.cucsi.gis.framework.exception.SystemErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * 接口返回封装类
 *
 * @author liukai
 */
@Getter
public class Result {

    public static final String SUCCESSFUL_CODE = "0";
    public static final String SUCCESSFUL_MESG = "处理成功";

    /**
     * 处理结果code
     */
    private String code;

    /**
     * 处理结果描述信息
     */
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    /**
     * 时间戳
     */
    private final Instant time;

    public Result() {
        this.time = ZonedDateTime.now().toInstant();
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
     *
     * @param code
     * @param message
     * @param data
     */
    private Result(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param message
     */
    public Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static Result success(Object data) {
        return new Result(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
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
     *
     * @param errorType
     * @param message
     * @return Result
     */
//    public static Result fail(ErrorType errorType, String message) {
//        return new Result(errorType, message);
//    }

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

    /**
     * 成功code=200
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
