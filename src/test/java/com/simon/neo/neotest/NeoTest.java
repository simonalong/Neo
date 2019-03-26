package com.simon.neo.neotest;

import com.alibaba.fastjson.JSON;
import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * 测试，其中待测试的表结构请见文件 /db/test.sql
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:47
 */
public class NeoTest extends NeoBaseTest{

    public NeoTest() throws SQLException {}

    /******************************插入******************************/
    @Test
    @SneakyThrows
    public void testInsert1(){
        NeoMap result = neo.insert(TABLE_NAME, NeoMap.of("group", "ok"));
        show(result);
    }

    @Test
    @SneakyThrows
    public void testInsert2(){
        DemoEntity result = neo.insert(TABLE_NAME, NeoMap.of("group", "ok", "name", "haode")).as(DemoEntity.class);
        show(result);
    }

    @Test
    @SneakyThrows
    public void testInsert3(){
        DemoEntity input = new DemoEntity();
        input.setGroup("group1");
        input.setName("name1");
        input.setUserName("user_name1");
        DemoEntity result = neo.insert(TABLE_NAME, input, NamingChg.UNDERLINE);
        show(result);
    }

    /******************************删除******************************/
    @Test
    @SneakyThrows
    public void testDelete1(){
        show(neo.delete(TABLE_NAME, NeoMap.of("group", "ok")));
    }

    @Test
    @SneakyThrows
    public void testDelete2(){
        DemoEntity input = new DemoEntity();
        input.setGroup("group1");
        input.setName("name1");
        input.setUserName("user_name1");
        show(neo.delete(TABLE_NAME, input, NamingChg.UNDERLINE));
    }

    @Test
    @SneakyThrows
    public void testDelete3(){
        show(neo.delete(TABLE_NAME, NeoMap.of("group", "group1")));
    }

    /******************************修改******************************/
    @Test
    @SneakyThrows
    public void testUpdate1(){
        show(neo.update(TABLE_NAME, NeoMap.of("group", "ok2"), NeoMap.of("group", "group2", "name", "name")));
    }

    @Test
    @SneakyThrows
    public void testUpdate2(){
        show(neo.update(TABLE_NAME, NeoMap.of("group", "ok3", "name", "name"), Columns.of("name")));
    }

    @Test
    @SneakyThrows
    public void testUpdate3(){
        DemoEntity input = new DemoEntity();
        input.setGroup("group2");
        show(neo.update(TABLE_NAME, input, NeoMap.of("group", "group1", "name", "name")));
    }

    @Test
    @SneakyThrows
    public void testUpdate4(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group1");

        DemoEntity data = new DemoEntity();
        data.setGroup("group2");
        show(neo.update(TABLE_NAME, data, search));
    }

    /**
     * 指定某个列作为查询条件
     */
    @Test
    @SneakyThrows
    public void testUpdate5(){
        show(neo.update(TABLE_NAME, NeoMap.of("group", "group1", "name", "name2"), Columns.of("group")));
    }

    /**
     * 指定某个列作为查询条件
     */
    @Test
    @SneakyThrows
    public void testUpdate6(){
        show(neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group222", "name", "name2")));
    }

    /******************************直接执行******************************/
    @Test
    public void testExecute1(){
        show(neo.execute("explain select * from neo_table1 where name ='name'"));
    }

    /**
     * 注意，转换符是直接将对应的输入转换到对应的位置
     */
    @Test
    public void testExecute2(){
        show(neo.execute("update %s set `group`=?, `name`=%s where id = ?", TABLE_NAME, "group121", "'name123'", 121));
        show(neo.execute("update %s set `group`=?, `name`=%s where id = ?", TABLE_NAME, "group121", "'name123'", 121));
    }

    @Test
    public void testExecute3(){
        show(neo.execute("update neo_table1 set `group`='group1', `name`='name1' where id = 122"));
    }

    @Test
    public void testExecute4(){
        show(neo.execute("select * from neo_table1"));
    }

    @Test
    public void testExecute4_2(){
        show(neo.execute("update neo_table1 set `group` = 'group1'"));
    }

    /**
     * 测试多结果集
     * CREATE PROCEDURE `pro`()
     * BEGIN
     *   explain select * from neo_table1;
     *   select * from neo_table1;
     * END
     */
    @Test
    public void testExecute5(){
        show(neo.execute("call pro()"));
    }

    @Test
    public void testExecute6(){
        NeoMap sql = neo.execute("show create table `xx_test5`").get(0).get(0);
        show("****");
        show(sql.get("Create Table"));
    }

    /****************************** 查询 ******************************/
    @Test
    public void getColumnNameListTest(){
        System.out.println(neo.getColumnNameList(TABLE_NAME));
    }

    @Test
    public void getColumnsTest(){
        System.out.println(JSON.toJSONString(neo.getColumnList(TABLE_NAME)));
    }

    @Test
    public void getIndexNameListTest(){
        System.out.println(neo.getIndexNameList(TABLE_NAME));
    }

    @Test
    public void getIndexListTest(){
        System.out.println(JSON.toJSONString(neo.getIndexList(TABLE_NAME)));
    }
}
