package com.simon.neo.neotest;

import com.alibaba.fastjson.JSON;
import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    /**
     * 指定某个列作为查询条件
     */
    @Test
    @SneakyThrows
    public void testUpdate7(){
        DemoEntity search = new DemoEntity();
        search.setId(281L);
        search.setGroup("group555");
        show(neo.update(TABLE_NAME, search));
    }

    /**
     * 指定某个列作为查询条件
     */
    @Test
    @SneakyThrows
    public void testUpdate8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group555");
        search.setName("name333");
        show(neo.update(TABLE_NAME, search, Columns.of("name")));
    }

    /**
     * 指定某个列作为查询条件
     */
    @Test
    @SneakyThrows
    public void testUpdate9(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group555");
        search.setName("name333");
        search.setUserName("userName2222");
        show(neo.update(TABLE_NAME, search, Columns.of("userName"), NamingChg.UNDERLINE));
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
        show(neo.getColumnNameList(TABLE_NAME));
    }

    @Test
    public void getColumnsTest(){
        show(JSON.toJSONString(neo.getColumnList(TABLE_NAME)));
    }

    @Test
    public void getIndexNameListTest(){
        show(neo.getIndexNameList(TABLE_NAME));
    }

    @Test
    public void getIndexListTest(){
        show(JSON.toJSONString(neo.getIndexList(TABLE_NAME)));
    }

    /****************************** 表的创建语句 ******************************/
    @Test
    public void getTableCreateTest(){
        // CREATE TABLE `xx_test5` (
        //  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
        //  `group` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
        //  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
        //  `user_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
        //  `gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女',
        //  `biginta` bigint(20) NOT NULL,
        //  `binarya` binary(1) NOT NULL,
        //  `bit2` bit(1) NOT NULL,
        //  `blob2` blob NOT NULL,
        //  `boolean2` tinyint(1) NOT NULL,
        //  `char1` char(1) COLLATE utf8_unicode_ci NOT NULL,
        //  `datetime1` datetime NOT NULL,
        //  `date2` date NOT NULL,
        //  `decimal1` decimal(10,0) NOT NULL,
        //  `double1` double NOT NULL,
        //  `enum1` enum('a','b') COLLATE utf8_unicode_ci NOT NULL,
        //  `float1` float NOT NULL,
        //  `geometry` geometry NOT NULL,
        //  `geometrycollection` geomcollection NOT NULL,
        //  `int2` int(11) NOT NULL,
        //  `json1` json NOT NULL,
        //  `linestring` linestring NOT NULL,
        //  `longblob` longblob NOT NULL,
        //  `longtext` longtext COLLATE utf8_unicode_ci NOT NULL,
        //  `medinumblob` mediumblob NOT NULL,
        //  `medinumint` mediumint(9) NOT NULL,
        //  `mediumtext` mediumtext COLLATE utf8_unicode_ci NOT NULL,
        //  `multilinestring` multilinestring NOT NULL,
        //  `multipoint` multipoint NOT NULL,
        //  `mutipolygon` multipolygon NOT NULL,
        //  `point` point NOT NULL,
        //  `polygon` polygon NOT NULL,
        //  `smallint` smallint(6) NOT NULL,
        //  `text` text COLLATE utf8_unicode_ci NOT NULL,
        //  `time` time NOT NULL,
        //  `timestamp` timestamp NOT NULL,
        //  `tinyblob` tinyblob NOT NULL,
        //  `tinyint` tinyint(4) NOT NULL,
        //  `tinytext` tinytext COLLATE utf8_unicode_ci NOT NULL,
        //  `text1` text COLLATE utf8_unicode_ci NOT NULL,
        //  `text1123` text COLLATE utf8_unicode_ci NOT NULL,
        //  `time1` time NOT NULL,
        //  `timestamp1` timestamp NOT NULL,
        //  `tinyblob1` tinyblob NOT NULL,
        //  `tinyint1` tinyint(4) NOT NULL,
        //  `tinytext1` tinytext COLLATE utf8_unicode_ci NOT NULL,
        //  `year2` year(4) NOT NULL,
        //  PRIMARY KEY (`id`)
        //) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='配置项'
        show(neo.getTableCreate("xx_test5"));
    }

    /**
     * 测试获取枚举的类型
     */
    @Test
    public void test23(){
        String sql = "`gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女'";

        String regex = "(?<= enum)\\((.*)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(sql);
        if (matcher.find()) {
            // 'Y','N'
            String enums = matcher.group(1);
            show(enums);
            List<String> dataList = Arrays.stream(enums.split(",")).map(c -> c.substring(1, c.length() - 1))
                .collect(Collectors.toList());
            // [Y, N]
            show(dataList);
        }
    }

    /****************************** 表的join ******************************/
    @Test
    public void test24(){

    }
}
