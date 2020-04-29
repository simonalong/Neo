package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/3/13 上午8:37
 */
public class NeoException extends RuntimeException {

    public NeoException(String msg, Throwable throwable){
        super(msg, throwable);
    }

    public NeoException(String msg){
        super(msg);
    }

    public NeoException(Throwable e){
        super(e);
    }
}
