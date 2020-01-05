package com.simonalong.neo.exception.xa;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午10:36
 */
public class XaRollbackException extends NeoXaException {

    public XaRollbackException(Throwable e){
        super(e);
    }
}
