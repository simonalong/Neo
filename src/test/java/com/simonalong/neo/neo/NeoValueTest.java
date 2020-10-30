package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * value 这里要查询的属性的值，可以跟DB中的字段一致，也可以不一致
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoValueTest extends NeoBaseTest {

    public NeoValueTest()  {}

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue1(){
        show(neo.exeValue("select `name` from neo_table1 where `group`=?", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue2(){
        show(neo.exeValue("select `age` from %s where `group`=?", TABLE_NAME, "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue3(){
        show(neo.exeValue(Integer.class, "select `age` from %s where `group`=? order by name desc", TABLE_NAME, "nana"));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `id` from neo_table1 where `group`='group2' limit 1
     */
    @Test
    @SneakyThrows
    public void testValue1(){
        show(neo.value(TABLE_NAME, "age", NeoMap.of("group", "ok", "user_name", "ee")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     * 注意，其中的类型必须为指定的这么几种类型：主要是跟数据库对应的几种类型
     */
    @Test
    @SneakyThrows
    public void testValue2(){
//        show(neo.value(TABLE_NAME, String.class, "group", NeoMap.of("group", "group2")));
        show(neo.value(TABLE_NAME, Integer.class, "age", NeoMap.of("group", "group2")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     */
    @Test
    @SneakyThrows
    public void testValue3(){
        show(neo.value(TABLE_NAME, "age", NeoMap.of("group", "group2", "order by", "age desc")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     */
    @Test
    @SneakyThrows
    public void testValue4(){
        show(neo.value(TABLE_NAME, Integer.class, "age", NeoMap.of("group", "group2", "order by", "age desc")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValue5(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group1");
        show(neo.value(TABLE_NAME, "group", search));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValue6(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.value(TABLE_NAME, String.class, "name", search));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValue7(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.value(TABLE_NAME, "name", search));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValue8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group1");
        show(neo.value(TABLE_NAME, String.class, "group", search));
    }

    /**
     * 测试order by
     */
    @Test
    public void testOrderBy1(){
        // select `name` from neo_table1 where `group` =  ? order by `name` desc  limit 1
        show(neo.value(TABLE_NAME, "name", NeoMap.of("group", "g", "order by", "name desc")));
    }

    @Test
    public void testOrderBy2(){
        // select `name` from neo_table1 where `group` =  ? order by `name` desc, `group` asc  limit 1
        show(neo.value(TABLE_NAME, "name", NeoMap.of("group", "g", "order by", "name desc, group asc")));
    }

    @Test
    public void testOrderBy3(){
        // select `name` from neo_table1 where `group` =  ? order by `name`, `group` desc, `id` asc  limit 1
        show(neo.value(TABLE_NAME, "name", NeoMap.of("group", "g", "order by", "name, group desc, id asc")));
    }
}
