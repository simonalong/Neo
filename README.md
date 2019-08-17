# Neo：Orm框架

Neo是一个基于JDBC开发的至简化框架。开发源头，源于几个原因，一个是之前几个公司采用的都是各自单独开发的Orm框架，这些框架不是开源，如果想自己使用，很多时候不方便，也不合适。另外一个主要是接触到的之前公司老大开发的那个框架思想很好，大道至简，对自己影响很大，该框架有很多思想借鉴之处。另一个是mybatis确实感觉不是很好用，把很多简单的东西设计的很复杂。最后是由于自己有很多想法，比如sql的规范落入到框架中、sql耗时统计和sql优化监控等等很多特性，且在之前接触的一些Orm框架中都没有。因此就有想法设计一个符合自己想法的Orm框架，下面的一些设计和各种特性有有借鉴之前接触到的一些优秀思想，也有在秉承着大道至简的原则进行的设计，框架刚起步，希望有兴趣的同学，一起添砖加瓦，共同成长。<br />下面介绍下框架的功能和一些用法，其中后面有（*）的部分是该框架亮点部分，也算特有部分。
<a name="SOWXF"></a>

#### 快速入门
当前已经发布到maven中央仓库，直接使用即可，最低版本0.3.0
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>Neo</artifactId>
  <version>0.3.0</version>
</dependency>
```

```java
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
```




#### 更多功能
- 数据库连接
- 基本功能
- DB异步
- 结构信息
- 批量功能
- 命名转换
- 单机事务
- sql监控
- 主从
- join
- 实体代码生成器
- 分布式
    - 全局id
    - 分布式锁
    - 分布式事务(待支持)
- 动态分库分表(待支持)
- 多数据源(待验证)

详细的用法请看这里
[Neo文档介绍](https://simons.gitbook.io/neo/)