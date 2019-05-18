package com.simon.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/5/17 上午9:57
 */
public class ValueCastClassException extends NeoException {

    public ValueCastClassException(String msg) {
        super("转换异常：" + msg);
    }
}
