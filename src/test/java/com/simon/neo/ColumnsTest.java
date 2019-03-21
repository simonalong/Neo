package com.simon.neo;

import com.simon.neo.entity.DemoEntity;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:26
 */
public class ColumnsTest {

    @Test
    public void testOf(){
        System.out.println(Columns.of("name", "name1"));
    }

    @Test
    public void testFrom(){
        System.out.println(Columns.from(DemoEntity.class));
    }
}
