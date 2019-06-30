package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/5/3 下午10:10
 */
public class RefreshRatioException extends NeoException {

    public RefreshRatioException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public RefreshRatioException(String msg){
        super("参数异常：" + msg);
    }
}
