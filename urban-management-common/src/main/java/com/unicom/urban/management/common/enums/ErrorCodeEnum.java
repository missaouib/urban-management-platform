package com.unicom.urban.management.common.enums;

public enum ErrorCodeEnum {

    SUCCESS(0, "成功"),

    // 1开头的为用户端错误
    AUTHENTICATION_FAIL(10001, "账号或密码错误"),
    TOKEN_EXPIRED(10002, "授权已经过期"),

    FIELD_VALIDATED_FAIL(10003, "参数校验异常"),

    // 2开头为服务端错误
    SERVER_ERROR(20001, "服务器出现异常");

    private Integer code;

    private String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
