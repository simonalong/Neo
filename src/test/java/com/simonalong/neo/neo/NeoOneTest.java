package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import java.sql.SQLException;
import java.util.Date;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * 测试Neo.one这个函数
 *
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:47
 */
public class NeoOneTest extends NeoBaseTest{

    public NeoOneTest() throws SQLException {}

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeOne1(){
        show(neo.exeOne("select * from neo_table1 where `group`=?", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeOne2(){
        show(neo.exeOne("select * from %s where `group`=?", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeOne3(){
        show(neo.exeOne("select * from %s where `group`=? order by name desc", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeOne4(){
        show(neo.exeOne(DemoEntity.class, "select * from %s where `group`=?", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式随便执行sql
     */
    @Test
    @SneakyThrows
    public void testExeOne5(){
        show(neo.exeOne("explain select * from neo_table1 where name like '%s'", "na%"));
    }

//    @Test
//    public void testExeOne6(){
//        NeoMap search = NeoMap.of("group", "ok", "name", "haode");
//        show(neo.exeOne(DemoEntity.class, "select * from %s %s", "neo_table1", SqlBuilder.buildWhereWithValue(search)));
//    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  limit 1
     */
    @Test
    @SneakyThrows
    public void testOne1(){
        show(neo.one(TABLE_NAME, NeoMap.of("group", "ok")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  order by `group` limit 1
     */
    @Test
    @SneakyThrows
    public void testOne2(){
        show(neo.one(TABLE_NAME, NeoMap.of("group", "nihao1", "order by", "group")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testOne3(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.one(TABLE_NAME, search));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  order by `group` limit 1
     */
    @Test
    @SneakyThrows
    public void testOne4(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.one(TABLE_NAME, search));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testOne5(){
        show(neo.one(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1")));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' order by age desc limit 1
     */
    @Test
    @SneakyThrows
    public void testOne6(){
        show(neo.one(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1", "order by", "age desc")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testOne7(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.one(TABLE_NAME, Columns.of("group", "name"), search));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testOne8(){
        DemoEntity search = new DemoEntity().setGroup("group2");
        show(neo.one(TABLE_NAME, Columns.of("group", "name"), search));
    }

    /**
     * 测试order by
     */
    @Test
    public void testOrderBy1() {
        // select `name` from neo_table1 where `group` =  ? order by `name` desc  limit 1
        show(neo.one(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name desc")));
    }

    @Test
    public void testOrderBy2() {
        // select `name` from neo_table1 where `group` =  ? order by `name` desc, `group` asc  limit 1
        show(neo.one(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name desc, group asc")));
    }

    @Test
    public void testOrderBy3() {
        // select `name` from neo_table1 where `group` =  ? order by `name`, `group` desc, `id` asc  limit 1
        show(neo.one(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name, group desc, id asc")));
    }

    /**
     * 用于插入，其中主键多次插入有异常，可以忽略
     */
    @Test
    public void testIII() {
        Long time = new Date().getTime();
        NeoMap neoMap = neo.insert("neo_table4",
            NeoMap.of("id", 11000, "create_time", time, "time", time, "year", time, "date", time, "datetime", time));
        show(neoMap);
    }
}
