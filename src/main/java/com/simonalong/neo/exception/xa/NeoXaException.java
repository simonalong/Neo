package com.simonalong.neo.exception.xa;

import javax.transaction.xa.XAException;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午11:25
 */
public class NeoXaException extends XAException {

    private Throwable e;

    public NeoXaException(){

    }

    public NeoXaException(Throwable e){
        super(e.getMessage());
        this.e = e;
    }
}
