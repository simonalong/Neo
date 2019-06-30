package com.github.simonalong.neo.typeformat;

/**
 * @author zhouzhenyong
 * @since 2019/5/4 下午10:13
 */
public enum EnumEntity {
    T1("t1"),
    T2("t2"),
    T3("t3");

    private String name;
    EnumEntity(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
