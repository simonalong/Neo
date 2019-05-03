package com.simon.neo.exception;

/**
 * @author zhouzhenyong
 * @since 2019/5/3 下午10:17
 */
public class UidGeneratorNotInitException extends NeoException {

    public UidGeneratorNotInitException(){
        super("全局id生成器没有开启");
    }
}
