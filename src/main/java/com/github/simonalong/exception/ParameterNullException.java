package com.github.simonalong.exception;

/**
 * @author zhouzhenyong
 * @since 2019/3/13 上午9:08
 */
public class ParameterNullException extends NeoException {

    public ParameterNullException(String msg){
        super("参数为空异常：" + msg);
    }
}
