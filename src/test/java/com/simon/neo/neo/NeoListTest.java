package com.simon.neo.neo;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.entity.DemoEntity;
import com.simon.neo.sql.SqlBuilder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午5:56
 */
public class NeoListTest extends NeoBaseTest{

    public NeoListTest() throws SQLException {}

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList1(){
        show(neo.exeList("select * from neo_table1 where `group`=?", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList2(){
        show(neo.exeList("select * from %s where `group`=?", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList3(){
        show(neo.exeList("select * from %s where `group`=? order by `age` desc", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList4(){
        show(neo.exeList(DemoEntity.class, "select * from %s where `group`=?", "neo_table1", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList5(){
        neo.setExplainFlag(true);
        show(neo.exeList(DemoEntity.class, "select * from %s ", TABLE_NAME));
        show(neo.exeList(DemoEntity.class, "select * from %s ", TABLE_NAME));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList6(){
        neo.setExplainFlag(true);
        List<Integer> idList = Arrays.asList(310, 311);
        // select * from neo_table1 where id in ('310','311')
        show(neo.exeList("select * from %s where id in %s", TABLE_NAME, SqlBuilder.in(idList)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?
     */
    @Test
    @SneakyThrows
    public void testList1(){
        show(neo.list(TABLE_NAME, NeoMap.of("group", "nihao1")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  order by `age`
     */
    @Test
    @SneakyThrows
    public void testList2() {
        show(neo.list(TABLE_NAME, NeoMap.of("group", "ok", "order by", "age")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testList3(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.list(TABLE_NAME, search));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  order by `group`
     */
    @Test
    @SneakyThrows
    public void testList4(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.list(TABLE_NAME, search));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testList5(){
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1")));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' order by group desc
     */
    @Test
    @SneakyThrows
    public void testList6(){
        show(neo.list(TABLE_NAME, Columns.of("age", "name"), NeoMap.of("group", "nihao1", "order by", "age desc")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1'
     */
    @Test
    @SneakyThrows
    public void testList7(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), search));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testList8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), search));
    }

    /**
     * 查询大小匹配的查询
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `name` < 'name' limit 1
     */
    @Test
    @SneakyThrows
    public void testList9(){
        // select `group`, `name` from neo_table1 where `name` < ? ], {params => [name]
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "< name")));
        // select `group`, `name` from neo_table1 where `name` < ? ], {params => ['name']
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "< 'name'")));
        // select `group`, `name` from neo_table1 where `name` <= ? ], {params => [name']
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "<= name'")));
        // select `group`, `name` from neo_table1 where `name` > ? ], {params => ['name']
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "> 'name'")));
        // select `group`, `name` from neo_table1 where `name` >= ? ], {params => ['name']
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", ">= 'name'")));
    }

    /**
     * 查询大小匹配的查询
     * 条件通过NeoMap设置
     * 相当于：select `group`, `name` from neo_table1 where `group` like 'group%'
     */
    @Test
    @SneakyThrows
    public void testList10(){
        // select `group`, `name` from neo_table1 where `group` like 'group%'
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "like group")));
    }

    @Test
    public void test(){
        show(neo.execute("select `group`, `name` from neo_table1 where `name` < ? ", "'name'"));
    }

    /**
     * 测试order by
     */
    @Test
    public void testOrderBy1(){
        // select `name` from neo_table1 where `group` =  ? order by `name` desc
        show(neo.list(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name desc")));
    }

    @Test
    public void testOrderBy2(){
        // select `name` from neo_table1 where `group` =  ? order by `name` desc, `group` asc  limit 1
        show(neo.list(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name desc, group asc")));
    }

    @Test
    public void testOrderBy3(){
        // select `name` from neo_table1 where `group` =  ? order by `name`, `group` desc, `id` asc  limit 1
        show(neo.list(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "g", "order by", "name, group desc, id asc")));
    }
}
