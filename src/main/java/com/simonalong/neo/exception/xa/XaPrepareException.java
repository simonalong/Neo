package com.simonalong.neo.exception.xa;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午11:17
 */
public class XaPrepareException extends NeoXaException {

    public XaPrepareException(){
        super();
    }
    public XaPrepareException(Throwable e){
        super(e);
    }
}
