package com.github.simonalong.db;

import com.github.simonalong.BaseTest;
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

    @Test
    public void dbTimeToLongTest(){
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        Assert.assertEquals(Long.class, TimeDateConverter.dbTimeToLong(sqlDate).getClass());

        java.sql.Time sqlTime = new java.sql.Time(System.currentTimeMillis());
        Assert.assertEquals(Long.class, TimeDateConverter.dbTimeToLong(sqlTime).getClass());

        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
        Assert.assertEquals(Long.class, TimeDateConverter.dbTimeToLong(sqlTimestamp).getClass());

        java.util.Date utilDate = new java.util.Date(System.currentTimeMillis());
        Assert.assertEquals(Date.class, TimeDateConverter.dbTimeToLong(utilDate).getClass());

        String data = "123";
        Assert.assertEquals(String.class, TimeDateConverter.dbTimeToLong(data).getClass());
    }
}
