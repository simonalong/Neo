package com.simon.neo;

import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:54
 */
public class SqlStandTest {

    @Test
    public void testValid(){
        String sql = "select * from tina_test";

        SqlStandard sqlStandard = SqlStandard.getInstance();
        System.out.println(sqlStandard.valid(sql));
    }
}
