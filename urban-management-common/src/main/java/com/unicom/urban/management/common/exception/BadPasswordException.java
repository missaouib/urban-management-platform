package com.unicom.urban.management.common.exception;

/**
 * 密码错误异常
 *
 * @author liukai
 */
public class BadPasswordException extends DataValidException {

    public BadPasswordException(String message) {
        super(message);
    }
}
