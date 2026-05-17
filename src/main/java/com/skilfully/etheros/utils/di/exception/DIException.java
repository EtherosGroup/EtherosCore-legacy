package com.skilfully.etheros.utils.di.exception;

import lombok.Getter;

/**
 * DI容器运行时异常
 *
 * @author Etheros Group
 * @since 1.0.0
 */
public class DIException extends RuntimeException {

    @Getter
    private final String detail;

    public DIException(String message) {
        super(message);
        this.detail = message;
    }

    public DIException(String message, Throwable cause) {
        super(message, cause);
        this.detail = message;
    }
}
