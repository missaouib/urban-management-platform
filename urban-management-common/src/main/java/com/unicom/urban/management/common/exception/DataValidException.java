package com.unicom.urban.management.common.exception;

/**
 * 数据校验异常
 *
 * @author liukai
 */
public class DataValidException extends BaseException {

    private final String message;

    public DataValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
