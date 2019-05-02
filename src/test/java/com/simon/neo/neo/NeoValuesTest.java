package com.simon.neo.neo;

import com.simon.neo.NeoMap;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoValuesTest extends NeoBaseTest {

    public NeoValuesTest() throws SQLException {}

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue1(){
//        show(neo.exeValues("select `name` from neo_table1 where `group`=?", "nihao1"));
        show(neo.exeValues("select `age` from neo_table1 where `group`=?", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue2(){
        show(neo.exeValues("select `group` from %s where `group`=?", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeValue3(){
        show(neo.exeValues(Long.class, "select `id` from %s where `group`=? order by name desc", "neo_table1", "nihao1"));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `id` from neo_table1 where `group`='group2' limit 1
     */
    @Test
    @SneakyThrows
    public void testValues1(){
        show(neo.values(TABLE_NAME, "group", NeoMap.of("name", "name")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     * 注意，其中的类型必须为指定的这么几种类型：主要是跟数据库对应的几种类型
     */
    @Test
    @SneakyThrows
    public void testValues2(){
//        show(neo.values(TABLE_NAME, String.class, "group", NeoMap.of("group", "group2")));
        show(neo.values(Integer.class, TABLE_NAME, "age", NeoMap.of("name", "name")));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     */
    @Test
    @SneakyThrows
    public void testValues3(){
        show(neo.values(TABLE_NAME, "age", NeoMap.of("group", "group2"), "order by age desc"));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     */
    @Test
    @SneakyThrows
    public void testValues4(){
        show(neo.values(Integer.class, TABLE_NAME, "age", NeoMap.of("group", "group2"), "order by age desc"));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValues5(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.values(TABLE_NAME, "age", search));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValues6(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.values(String.class, TABLE_NAME, "name", search));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValues7(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.values(TABLE_NAME, "name", search, "order by `group`"));
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValues8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.values(String.class, TABLE_NAME, "group", search, "order by 'group'"));
    }
}