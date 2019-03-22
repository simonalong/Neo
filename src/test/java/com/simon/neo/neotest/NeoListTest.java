package com.simon.neo.neotest;

import com.simon.neo.Columns;
import com.simon.neo.NeoColumn;
import com.simon.neo.NeoMap;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
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
        show(neo.exeList("select * from tina_test where `group`=?", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList2(){
        show(neo.exeList("select * from %s where `group`=?", "tina_test", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList3(){
        show(neo.exeList("select * from %s where `group`=? order by `age` desc", "tina_test", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList4(){
        show(neo.exeList(DemoEntity.class, "select * from %s where `group`=?", "tina_test", "nihao1"));
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList5(){
        neo.setExplainFlag(true);
        show(neo.exeList(DemoEntity.class, "select * from %s ", "tina_test"));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from tina_test where `group` = 'ok'
     */
    @Test
    @SneakyThrows
    public void testList1(){
        show(neo.list(TABLE_NAME, NeoMap.of("group", "nihao1")));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from tina_test where `group` = 'ok' order by 'group'
     */
    @Test
    @SneakyThrows
    public void testList2(){
        show(neo.list(TABLE_NAME, NeoMap.of("group", "nihao1"), "order by `age`"));
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
     * select * from tina_test where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testList4(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.list(TABLE_NAME, search, "order by `group`"));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from tina_test where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testList5(){
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1")));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from tina_test where `group` = 'ok' order by group desc
     */
    @Test
    @SneakyThrows
    public void testList6(){
        show(neo.list(TABLE_NAME, Columns.of("age", "name"), NeoMap.of("group", "nihao1"), "order by `age` desc"));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from tina_test where `group` = 'group1'
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
     * 相当于：select `group`,`name` from tina_test where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testList8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.list(TABLE_NAME, Columns.of("group", "name"), search, "order by `age` desc"));
    }
}
