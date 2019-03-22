package com.simon.neo.neotest;

import com.alibaba.fastjson.JSON;
import com.simon.neo.NeoColumn;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * 测试，其中待测试的表结构如下
 * CREATE TABLE `tina_test` (
 *   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *   `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
 *   `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
 *   `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
        DemoEntity input = new DemoEntity();
        input.setGroup("group2");
        show(neo.update(TABLE_NAME, input, NeoMap.of("group", "group1", "name", "name")));
    }

    @Test
    @SneakyThrows
    public void testUpdate3(){
        DemoEntity input = new DemoEntity();
        input.setGroup("group2");
        show(neo.update(TABLE_NAME, NeoMap.of("group", "group1", "name", "name"), input));
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

    /******************************直接执行******************************/
    @Test
    public void testExecute1(){
        show(neo.execute("explain select * from tina_test where name ='name'"));
    }

    /**
     * 注意，转换符是直接将对应的输入转换到对应的位置
     */
    @Test
    public void testExecute2(){
        show(neo.execute("update %s set `group`=?, `name`=%s where id = ?", "tina_test", "group121", "'name123'", 121));
    }

    @Test
    public void testExecute3(){
        show(neo.execute("update tina_test set `group`='group1', `name`='name1' where id = 122"));
    }

    @Test
    public void testExecute4(){
        show(neo.execute("select * from tina_test"));
    }

    /**
     * 测试多结果集
     */
    @Test
    public void testExecute5(){
        show(neo.execute("call proc6()"));
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
