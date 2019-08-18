package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/8/18 下午4:47
 */
public class DbNotSetException extends NeoException{

    public DbNotSetException(String msg, Throwable throwable) {
        super("db not set:" + msg, throwable);
    }

    public DbNotSetException(){
        super("db not set:");
    }
}
