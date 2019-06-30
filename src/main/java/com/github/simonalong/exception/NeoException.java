package com.github.simonalong.exception;

/**
 * @author zhouzhenyong
 * @since 2019/3/13 上午8:37
 */
public class NeoException extends RuntimeException {

    public NeoException(String msg, Throwable throwable){
        super("Neo异常：" + msg, throwable);
    }

    public NeoException(String msg){
        super("Neo异常：" + msg);
    }

    public NeoException(Throwable e){
        super(e);
    }
}
