package com.github.simonalong;

import java.util.Optional;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午11:41
 */
public class BaseTest {

    public void show(Object object) {
        if(null == object){
            System.out.println("obj is null ");
            return;
        }
        Optional.of(object).ifPresent(objects1 -> System.out.println(objects1.toString()));
    }
}
