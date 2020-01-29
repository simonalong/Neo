package com.simonalong.neo.exception.xa;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午11:24
 */
public class XaEndException extends NeoXaException {

    public XaEndException(Throwable e){
        super(e);
    }
}
