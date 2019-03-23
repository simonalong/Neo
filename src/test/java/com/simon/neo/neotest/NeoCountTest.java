package com.simon.neo.neotest;


import com.simon.neo.NeoMap;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoCountTest extends NeoBaseTest{

    public NeoCountTest() throws SQLException {}

    /**
     * 查询表的总数
     */
    @Test
    @SneakyThrows
    public void testCount1(){
        show(neo.exeCount("select count(1) from %s where `group`=?", TABLE_NAME, "nihao1"));
    }

    /**
     * 查询表的总数
     */
    @Test
    @SneakyThrows
    public void testCount2(){
        show(neo.exeCount("select count(1) from neo_table1 where `group`=?", "nihao1"));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount3(){
        show(neo.count(TABLE_NAME));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount4(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.count(TABLE_NAME, search));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount5(){
        show(neo.count(TABLE_NAME, NeoMap.of("group", "nihao1")));
    }
}
