package com.simonalong.neo.exception;

/**
 * @author shizi
 * @since 2020/6/9 2:59 PM
 */
public class NeoNotSupport extends NeoException {

    public NeoNotSupport(){
        super();
    }

    public NeoNotSupport(String msg){
        super(msg);
    }

    public NeoNotSupport(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
