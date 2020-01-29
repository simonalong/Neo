package com.simonalong.neo.exception.xa;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午11:20
 */
public class XaStartException extends NeoXaException {

    public XaStartException(Throwable e){
        super(e);
    }
}
