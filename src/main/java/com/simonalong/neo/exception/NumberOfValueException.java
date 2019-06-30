package com.simonalong.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/3/13 上午9:02
 */
public class NumberOfValueException extends NeoException {

    public NumberOfValueException(String msg){
        super("参数个数不匹配：" + msg);
    }
}
