package com.simon.neo.neo;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoPage;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import lombok.ToString.Exclude;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午2:34
 */
public class NeoPageTest extends NeoBaseTest{

    public NeoPageTest() throws SQLException {}

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from neo_table1 where `group` = 'ok'
     */
    @Test
    @SneakyThrows
    public void testPage1(){
        show(neo.page(TABLE_NAME, NeoMap.of("group", "nihao1"), 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from neo_table1 where `group` = 'ok'
     */
    @Test
    @SneakyThrows
    public void testPage2(){
        show(neo.page(TABLE_NAME, NeoMap.of("group", "nihao1"), NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from neo_table1 where `group` = 'ok' order by 'group' limit 0, 20
     */
    @Test
    @SneakyThrows
    public void testPage3(){
        show(neo.page(TABLE_NAME, NeoMap.of("group", "nihao1"), "order by `age`", 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select * from neo_table1 where `group` = 'ok' order by 'group'
     */
    @Test
    @SneakyThrows
    public void testPage4(){
        show(neo.page(TABLE_NAME, NeoMap.of("group", "nihao1"), "order by `age`", NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testPage5(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.page(TABLE_NAME, search, 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testPage6(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.page(TABLE_NAME, search, NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * select * from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage7(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.page(TABLE_NAME, search, "order by `group`", 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * select * from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage8(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.page(TABLE_NAME, search, "order by `group`", NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage9(){
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1"), 0, 20));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage10(){
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "nihao1"), NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' order by group desc
     */
    @Test
    @SneakyThrows
    public void testPage11(){
        show(neo.page(TABLE_NAME, Columns.of("age", "name"), NeoMap.of("group", "nihao1"), "order by `age` desc", 0, 20));
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' order by group desc
     */
    @Test
    @SneakyThrows
    public void testPage12(){
        show(neo.page(TABLE_NAME, Columns.of("age", "name"), NeoMap.of("group", "nihao1"), "order by `age` desc", NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1'
     */
    @Test
    @SneakyThrows
    public void testPage13(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), search, 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1'
     */
    @Test
    @SneakyThrows
    public void testPage14(){
        DemoEntity search = new DemoEntity();
        search.setGroup("nihao1");
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), search, NeoPage.of(1, 20)));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage15(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), search, "order by `age` desc", 0, 20));
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testPage16(){
        DemoEntity search = new DemoEntity();
        search.setGroup("group2");
        show(neo.page(TABLE_NAME, Columns.of("group", "name"), search, "order by `age` desc", NeoPage.of(1, 20)));
    }

    /**
     * 普通的分页处理
     * select * from neo_table1 limit 0, 20
     */
    @Test
    public void testExePage1(){
        String table1 = "neo_table1";
        neo.exePage("select * from %s", 0, 20, table1);
    }

    /**
     * 对于sql中包含limit 的处理，这里其实跟list已经没有什么区别了，而且还多出了中间两个参数，不建议这样使用
     *
     * [select * from neo_table1 limit ?, ?], {params => [0, 12]}
     */
    @Test
    public void testExePage2(){
        String table1 = "neo_table1";
        neo.exePage("select * from %s limit ?, ?", 0, 20, table1, 0, 12);
    }

    /**
     * 添加参数的分页处理
     * [select * from neo_table1 where id < ? limit 12, 20], {params => [1000]}
     */
    @Test
    public void testExePage3(){
        String table1 = "neo_table1";
        neo.exePage("select * from %s where id < ?", 12, 20, table1, 1000);
    }

    /**
     * 指定分页的页面位置
     * [sql => select * from neo_table1 where id < ? limit 0, 20], {params => [1000] }
     */
    @Test
    public void testExePage4(){
        String table1 = "neo_table1";
        neo.exePage("select * from %s where id < ?", NeoPage.of(1, 20), table1, 1000);
    }
}
