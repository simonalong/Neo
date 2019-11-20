package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:29
 */
public class ParameterUnValidException extends NeoException {

    public ParameterUnValidException(String msg) {
        super(msg);
    }

    public ParameterUnValidException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
