package com.simonalong.neo.exception;

/**
 * @author shizi
 * @since 2020/6/11 1:57 PM
 */
public class NotFindDevideTableException extends NeoException {

    public NotFindDevideTableException(String msg) {
        super("没有找到可设置的切分表：" + msg);
    }

    public NotFindDevideTableException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
