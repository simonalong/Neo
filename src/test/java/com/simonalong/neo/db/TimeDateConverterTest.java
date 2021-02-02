package com.simonalong.neo.db;

import com.simonalong.neo.BaseTest;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/16 下午8:34
 */
public class TimeDateConverterTest extends BaseTest {

    @Test
    public void longToEntityTimeTest(){
        Long time = new Date().getTime();
        Assert.assertEquals(java.sql.Date.class, TimeDateConverter.valueToEntityTime(java.sql.Date.class, time).getClass());

        Assert.assertEquals(String.class, TimeDateConverter.valueToEntityTime(String.class, "").getClass());
    }
}
