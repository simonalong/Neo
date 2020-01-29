package com.simonalong.neo.exception.xa;

/**
 * @author zhouzhenyong
 * @since 2020/1/5 下午11:38
 */
public class XaCommitException extends NeoXaException {

    public XaCommitException(Throwable e){
        super(e);
    }
}
