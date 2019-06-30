package com.simonalong.neo.sql;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:54
 */
public class SqlStandTest {

    /**
     * 合法，但是会命中规范：select *
     */
    @Test
    public void testValid1(){
        String sql = "select * from neo_table1";

        SqlStandard sqlStandard = SqlStandard.getInstance();
        Assert.assertTrue(sqlStandard.valid(sql));
    }

    /**
     * 合法，而且没有命中规范
     */
    @Test
    public void testValid2(){
        String sql = "select `group`, `name` from neo_table1 where `group`='group'";

        SqlStandard sqlStandard = SqlStandard.getInstance();
        Assert.assertTrue(sqlStandard.valid(sql));
    }

    /**
     * 合法，且命中in
     */
    @Test
    public void testValid3(){
        String sql = "select `group`, `name` from neo_table1 where `name` in('name1', 'name2')";

        SqlStandard sqlStandard = SqlStandard.getInstance();
        Assert.assertTrue(sqlStandard.valid(sql));
    }

    /**
     * 合法，且命中in
     */
    @Test
    public void testValid4(){
        String sql = "select `group`, `name` from neo_table1 where `name` in ('name1', 'name2')";

        SqlStandard sqlStandard = SqlStandard.getInstance();
        Assert.assertTrue(sqlStandard.valid(sql));
    }

    /**
     * 合法，且命中not
     */
    @Test
    public void testValid5(){
        SqlStandard sqlStandard = SqlStandard.getInstance();

        String sql1 = "select `group`, `name` from neo_table1 where `name` not in ('name1', 'name2')";
        Assert.assertTrue(sqlStandard.valid(sql1));

        String sql2 = "select `group`, `name` from neo_table1 where `name` !='name1'";
        Assert.assertTrue(sqlStandard.valid(sql2));

        String sql3 = "select `group`, `name` from neo_table1 where `name` <>'name1'";
        Assert.assertTrue(sqlStandard.valid(sql3));
    }

    /**
     * 合法，且命中like
     */
    @Test
    public void testValid6(){
        SqlStandard sqlStandard = SqlStandard.getInstance();

        String sql1 = "select `group`, `name` from neo_table1 where `name` like '%xx'";
        Assert.assertTrue(sqlStandard.valid(sql1));
    }
}
