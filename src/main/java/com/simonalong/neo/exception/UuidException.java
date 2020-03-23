package com.simonalong.neo.exception;

/**
 * @author 柿子
 * @since 2019/11/7 10:25 下午
 */
public class UuidException extends RuntimeException {

    public UuidException() {
        super();
    }

    public UuidException(String msg) {
        super(msg);
    }

    public UuidException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
