package com.simonalong.neo.exception;

/**
 * @author shizi
 * @since 2020/4/6 10:51 AM
 */
public class NotFindDevideDbException extends NeoException {

    public NotFindDevideDbException(String msg){
        super("没有找到可设置的切分库：" + msg);
    }

    public NotFindDevideDbException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
