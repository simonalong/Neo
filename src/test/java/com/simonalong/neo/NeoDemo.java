package com.simonalong.neo;

import com.simonalong.neo.table.NeoPage;
import com.simonalong.neo.table.NeoTable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * 快速入门实例
 *
 * @author zhouzhenyong
 * @since 2019-08-17 14:14
 */
public class NeoDemo {

    @Test
    public void testDemo1() {
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "neo_test";
        String password = "neo@Test123";
        String tableName = "neo_table1";
        // 连接
        Neo neo = Neo.connect(url, user, password);

        // 插入
        NeoMap data = neo.insert(tableName, NeoMap.of("group", "value"));

        data.put("group", "value1");

        // 更新
        neo.update(tableName, data);

        // 删除
        neo.delete(tableName, data);

        // 查询一行
        neo.one(tableName, data);

        // 查询多行
        neo.list(tableName, data);

        // 查询指定列的一个值
        neo.value(tableName, "group", data);

        // 查询指定列的多个值
        neo.values(tableName, "group", data);

        // 查询分页
        neo.page(tableName, data, NeoPage.of(1, 20));

        // 执行sql
        neo.execute("select * from %s where group =?", tableName, "group1");

        // 事务
        neo.tx(()->{
            neo.update(tableName, NeoMap.of("id", 12, "group", "value1"));
            neo.one(tableName, 12);
            neo.update("neo_table2", NeoMap.of("column2", 12));
        });

        // 批量
        List<NeoMap> list = new ArrayList<>();
        list.add(NeoMap.of("group", "v1"));
        list.add(NeoMap.of("group", "v2"));
        list.add(NeoMap.of("group", "v3"));
        list.add(NeoMap.of("group", "v4"));
        neo.batchInsert(tableName, list);
    }

    /**
     * 指定表的话，就更简单
     */
    @Test
    public void testDemo2() {
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "neo_test";
        String password = "neo@Test123";
        String tableName = "neo_table1";
        // 连接
        Neo neo = Neo.connect(url, user, password);
        NeoTable table = neo.getTable(tableName);

        // 插入
        NeoMap data = table.insert(NeoMap.of("group", "value"));

        data.put("group", "value1");

        // 更新
        table.update(data);

        // 删除
        table.delete(data);

        // 查询一行
        table.one(data);

        // 查询多行
        table.list(data);

        // 查询指定列的一个值
        table.value("group", data);

        // 查询指定列的多个值
        table.values("group", data);

        // 查询分页
        table.page(data, NeoPage.of(1, 20));

        // 批量
        List<NeoMap> list = new ArrayList<>();
        list.add(NeoMap.of("group", "v1"));
        list.add(NeoMap.of("group", "v2"));
        list.add(NeoMap.of("group", "v3"));
        list.add(NeoMap.of("group", "v4"));
        table.batchInsert(list);
    }
}
