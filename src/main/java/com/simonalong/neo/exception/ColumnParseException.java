package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/5/10 下午4:42
 */
public class ColumnParseException extends NeoException {

    public ColumnParseException(String msg) {
        super("列解析异常：" + msg);
    }
}
