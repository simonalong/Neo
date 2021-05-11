# Neo：Orm框架

Neo是一个基于JDBC开发的至简化框架，采用ActiveRecord的思想进行设计，名字源于《黑客帝国》男主角名字，为连接虚拟与现实的救世主，取名Neo寓意为链接数据和逻辑。源头来自于之前接触的一些不错的Orm框架，也有来自于业内的对于至简化编码影响，也有对于CQRS思想的借鉴。

## 使用文档
[Neo文档介绍](https://simons.gitbook.io/neo/)
[最新Neo文档介绍](https://www.yuque.com/simonalong/neo)

## 快速入门
该框架秉承大道至简理念，采用一个Neo对象对应一个DataSource方式，然后这个Neo对象拥有对表的各种操作。

### maven引入
当前已经发布到maven中央仓库，直接使用即可，目前最低版本0.3.0，不同版本的api差距不小，建议使用最新版本。目前版本还未脱离实验阶段，还未到达稳定版，如果有什么问题请及时反馈
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>Neo</artifactId>
  <version>${latest.release.version}</version>
</dependency>
```

### 快速入门
一个DB对应的一个对象Neo，操作表，则填入对应的表名即可
```sql
CREATE TABLE `neo_table1` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `sl` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```
示例：
```java
public void testDemo1() {
    String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    String user = "neo_test";
    String password = "neo@Test123";
    String tableName = "neo_table1";
    // 连接
    Neo neo = Neo.connect(url, user, password);

    // 插入
    NeoMap data = neo.insert(tableName, NeoMap.of("group", "value", "name", "name1"));

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
    
    // 分页个数
    table.count(tableName, data);

    // 执行sql
    neo.execute("select * from %s where `group` =?", tableName, "group1");

    // 事务
    neo.tx(()->{
        neo.update(tableName, NeoMap.of("id", 12, "group", "value1"));
        neo.one(tableName, 12);
        neo.update("neo_table2", NeoMap.of("name", 12));
    });

    // 批量插入
    List<NeoMap> list = new ArrayList<>();
    list.add(NeoMap.of("group", "group1", "name", "name1", "user_name", "user_name1"));
    list.add(NeoMap.of("group", "group2", "name", "name2", "user_name", "user_name2"));
    list.add(NeoMap.of("group", "group3", "name", "name3", "user_name", "user_name3"));
    neo.batchInsert(tableName, list);
    
    // 批量更新，group是条件
    neo.batchUpdate(tableName, list, Columns.of("group"));
}
```

指定表的话，就更简单，一个表对应一个对象NeoTable
```java
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
    
    // 分页个数
    table.count(data);

    // 批量插入
    List<NeoMap> list = new ArrayList<>();
    list.add(NeoMap.of("group", "group1", "name", "name1", "user_name", "user_name1"));
    list.add(NeoMap.of("group", "group2", "name", "name2", "user_name", "user_name2"));
    list.add(NeoMap.of("group", "group3", "name", "name3", "user_name", "user_name3"));
    table.batchInsert(list);
    
    // 批量更新，group是条件
    table.batchUpdate(list, Columns.of("group"));
}
```

其中批量更新部分，我们采用最新的方式可以在数据量巨大时候，性能非常好，如下
```sql
 update table_name a join
 (
 select 1 as `id`, 'holy' as `name`
 union
 select 2 as `id`, 'shit' as `name`
 ) b using(`id`)
 set a.`name`=b.`name`;
 }
```
表示更新表`table_name`，其中条件为`id`，对应修改的值为`name`

### 指定实体
上面我们对数据的操作全都是基于map，下面我们基于实体DO对数据库进行操作
```java
/**
 * 指定表的话，就更简单
 */
@Test
public void testDemo3() {
    String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    String user = "neo_test";
    String password = "neo@Test123";
    String tableName = "neo_table1";
    // 连接
    Neo neo = Neo.connect(url, user, password);
    NeoTable table = neo.getTable(tableName);

    // 实体数据
    DemoEntity3 entity = new DemoEntity3().setGroup("group1").setUsName("name1");

    // 插入
    DemoEntity3 result = table.insert(entity);

    result.setUsName("name2");

    // 更新
    table.update(result);

    // 删除
    table.delete(result);

    // 查询一行
    table.one(result);

    // 查询多行
    table.list(result);

    // 查询指定列的
    table.value("group", NeoMap.of("user_name", "name2"));

    // 查询指定列的多个值
    table.values("group", NeoMap.of("user_name", "name2"));

    // 查询分页，第一个参数是搜索条件
    table.page(NeoMap.of("user_name", "name2"), NeoPage.of(1, 20));
    
    // 分页个数
    table.count(data);

    // 批量插入
    List<DemoEntity3> list = new ArrayList<>();
    list.add(new DemoEntity().setGroup("group1").setName("name1").setUserName("userName1"));
    list.add(new DemoEntity().setGroup("group2").setName("name2").setUserName("userName2"));
    list.add(new DemoEntity().setGroup("group3").setName("name3").setUserName("userName3"));
    table.batchInsertEntity(list);
    
    // 批量更新
    List<DemoEntity3> updateList = new ArrayList<>();
    updateList.add(new DemoEntity().setId(1L).setGroup("group1").setName("name1").setUserName("userName1"));
    updateList.add(new DemoEntity().setId(2L).setGroup("group2").setName("name2").setUserName("userName2"));
    updateList.add(new DemoEntity().setId(3L).setGroup("group3").setName("name3").setUserName("userName3"));
    table.batchUpdateEntity(list, Columns.of("id"));
}
```


### 复杂查询
除了基本的查询外，对于复杂的条件查询，可以采用稍微复杂点的搜索方式，不过使用也还是很简单
version >= 0.6.0

```java
/**
 * 复杂表达式的搜索
 * <p>其中有默认的过滤原则
 */
@Test
public void oneTest() {
    NeoMap dataMap = NeoMap.of("group", "group_insert_express", "name", "name_insert_express");
    neo.insert(TABLE_NAME, dataMap);

    SearchQuery searchQuery;

    searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express");
    Assert.assertEquals(dataMap, neo.one(TABLE_NAME, searchQuery).assignExcept("id"));
}
```
除了默认的and，类SearchQuery还有更复杂的使用方式，类`Neo`支持SearchQuery作为更复杂的条件搜索
```java
NeoMap one(String tableName, Columns columns, SearchQuery searchQuery);
String value(String tableName, String field, SearchQuery searchQuery);
List<NeoMap> list(String tableName, Columns columns, SearchQuery searchQuery);
// ...等等可以NeoMap的api，都是对应的SearchQuery作为搜索条件
```
```java
new SearchQuery().append(BetweenAnd("age", 1, 4));
new SearchQuery().and(In("id", dataList));
new SearchQuery().and(Like("name", "%chou"));
new SearchQuery().and(IsNull("name"));
new SearchQuery().and("name", 12).append(GroupBy("group"));
new SearchQuery().append(OrderBy("create_time"));
new SearchQuery().append(Exists("select id from xxx"));
new SearchQuery().and(LessThan("name", "tt"));

// ...等等所有表达式都支持...
```

#### 提示
SearchQuery也可以用来生成sql进行拼接使用，这个时候使用就需要做另外的方式进行处理，举个真实案例如下
```java
public PageRsp<AlarmPeopleDO> getPageList(PageReq<PeopleQueryReq> pageReq) {
    PageRsp<AlarmPeopleDO> pageRsp = new PageRsp<>();
    PeopleQueryReq req = pageReq.getParam();
    if (null == req) {
        PageRsp<AlarmPeopleDO> rsp = new PageRsp<>();
        rsp.setTotalNum(0);
        rsp.setDataList(Collections.emptyList());
        return rsp;
    }

    Express searchQuery = new Express();
    searchQuery.and("people.`profile`", req.getProfile());
    searchQuery.and(BaseOperate.Like("people.`name`", "%" + req.getName() + "%"));
    searchQuery.and(BaseOperate.Like("people_group.`group`", "%" + req.getGroup() + "%"));
    searchQuery.append(BaseOperate.OrderByDesc("people_group.`update_time`"));

    String pageSql = "select distinct people.* from xxx_alarm_people as people left join xxx_alarm_people_group_rel as rel on people.`id` = rel.people_id left join xxx_alarm_people_group as people_group on rel.`group_id` = people_group.`id` %s";
    String countSql = "select count(distinct people.`id`) from xxx_alarm_people as people left join xxx_alarm_people_group_rel as rel on people.`id` = rel.people_id left join xxx_alarm_people_group as people_group on rel.`group_id` = people_group.`id` %s";

    List<Object> parameterList = new ArrayList<>();
    parameterList.add(searchQuery.toSql());
    // 注意，这里需要用到这里的value才行
    parameterList.add(searchQuery.toValue());

    pageRsp.setDataList(alarmDb.exePage(AlarmPeopleDO.class, pageSql, NeoPage.from(pageReq), parameterList.toArray()));
    pageRsp.setTotalNum(alarmDb.exeCount(countSql, parameterList.toArray()));
    return pageRsp;
}
```

#### 注意
生成一个Neo对象除了可以通过url、user和password，还可以通过DataSource方式
```java
// 连接
Neo neo = Neo.connect(datasource);
```

### 实体和DB字段映射
实体和DB中字段的映射，可以有三种方式进行配置<br/>
1.`NeoMap`全局配置：`NeoMap.setDefaultNamingChg(xxx)`<br/>
2.`NeoMap`实体单独配置：`neoMap.setNamingChg(xxx)`<br/>
3.通过注解`@Column`对应配置：每个属性上面添加`@Column`其中是DB中的属性名 <br/>

下面介绍`@Column`的用法，以后表字段修改时候，实体就不用修改，如果有属性上面没有添加注解，则默认按照类型`NamingChg.UNDERLINE`进行转换
```java
/**
 * @author robot
 */
@Data
@Accessors(chain = true)
public class NeoTable1DO {

    @Column("id")
    private Long id;

    /**
     * 数据来源组，外键关联lk_config_group
     */
    @Column("group")
    private String group;

    /**
     * 任务name
     */
    @Column("name")
    private String name;

    /**
     * 修改人名字
     */
    @Column("user_name")
    private String userName;
    @Column("age")
    private Integer age;
    @Column("sl")
    private Long sl;
    @Column("data_name")
    private String dataName;
}
```
sql字段
```sql
CREATE TABLE `neo_table1` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `sl` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

### 业务使用
也可以继承使用，针对业务接入，可以直接继承类`AbstractBizService`即可具备一个表的常见的所有功能，只需要实现如下两个方法即可
```java
public class BizServiceTest extends AbstractBizService {

    public BizServiceTest() throws SQLException {
    }

    @Override
    public DbSync getDb() {
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "neo_test";
        String password = "neo@Test123";
        return Neo.connect(url, user, password);
    }

    @Override
    public String getTableName() {
        return "neo_table1";
    }

    @Test
    public void testInsert() {
        TestEntity entity = new TestEntity()
            .setGroup("ok")
            .setUserName("me")
            .setName("hello");
        insert(entity);
    }
}
```

### 实体代码生成器
本框架也支持实体生成器，如下，即可生成如上所示的类`NeoTable1DO`就是如下生成的
```java
public class CodeGenTest {

    @Test
    public void test1(){
        EntityCodeGen codeGen = new EntityCodeGen()
            // 设置DB信息
            .setDb("neo_test", "neo@Test123", "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
            // 设置项目路径
            .setProjectPath("/Users/zhouzhenyong/project/private/Neo")
            // 设置实体生成的包路径
            .setEntityPath("com.simonalong.neo.entity")
            // 设置表前缀过滤
            // .setPreFix("t_")
            // 设置要排除的表
            //.setExcludes("xx_test")
            // 设置只要的表
            .setIncludes("neo_table1")
            // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
            .setFieldNamingChg(NamingChg.UNDERLINE);

        // 代码生成
        codeGen.generate();
    }
}
```

### 多表join

```java
@Test
public void testJoin1() {
    // 首先获取join的处理器，支持查询one，list, value, values, page, count
    NeoJoiner neoJoiner = neo.joiner();

    // 配置的列
    Columns columns = Columns.of(neo);
    columns.table("neo_table1", "age");
    // 配置所有列，可以为columns.table("neo_table2", "*")
    columns.table("neo_table2", "name", "group");

    // 配置多表join
    TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
    tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
    tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

    // 配置查询条件
    TableMap searchMap = TableMap.of();
    searchMap.put("neo_table1", "name", "nihao");
    searchMap.put("neo_table2", "group", "ok");

    // select
    // neo_table1.`age` as neo_table1_dom_age,
    // neo_table2.`group` as neo_table2_dom_group,
    // neo_table2.`name` as neo_table2_dom_name
    //
    // from
    // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
    // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
    //
    // where neo_table2.`group` =  ? and neo_table1.`name` =  ?

    // [ok, nihao]
    show(neoJoiner.one(columns, tableJoinOn, searchMap));
}
```

### 分布式ID生成器
我们这里也提供了分布式ID生成器方案，采用的是改进版雪花算法，彻底解决了雪花算法存在的常见问题（时间回拨问题，workerId回收问题），对于如何解决的，具体方案可见文档，也可见我的另外一个项目[Butterfly](https://github.com/SimonAlong/Butterfly)（Neo框架中的发号器方案是Butterfly中的一个使用选项）。

采用的是改进版雪花算法，不仅没有时间回拨问题，性能还比雪花算法还要高十几倍，普通机器QPS都可以达到1000w/s。

#### 使用
先建表，如果没有请创建
```sql
CREATE TABLE `neo_uuid_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `namespace` varchar(128) DEFAULT '' COMMENT '命名空间',
  `work_id` int(16) NOT NULL DEFAULT '0' COMMENT '工作id',
  `last_expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下次失效时间',
  `uid` varchar(128) DEFAULT '0' COMMENT '本次启动唯一id',
  `ip` varchar(20) NOT NULL DEFAULT '0' COMMENT 'ip',
  `process_id` varchar(128) NOT NULL DEFAULT '0' COMMENT '进程id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name_work` (`namespace`,`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

```java
@Test
public void generateTest1() {
    UuidGenerator generator = UuidGenerator.getInstance(neo);
    // 注册（声明）命令空间（在业务空间对应的集群，最多可以有8192台机器跑同一个业务，对大部分业务来说，足够了）
    generator.addNamespaces("test1", "test2");
    
    System.out.println(generator.getUUid("test1"));
}
```

### 主从
`>=v0.5.2`
主从功能支持：
- 读写分离：支持多主多从，主库写，从库读取
- 负载均衡（从库轮询选择，主库只使用最后激活的）
- 故障转移
    - 从库：（从库宕机，从其他从库中选择，从库全部宕机，则从可用的主库中选择一个）
    - 主库：（多主，其中一个写）主库宕机，则切换到另一个主库写
- 故障恢复：当主库或者从库之前断开，之后如果恢复正常，则框架自动重连，应用方无感知生效
```java
/**
 * 双主多从，记得先自己搭建好主从环境
 */
@Test
@SneakyThrows
public void testReplication() {
    String tableName = "neo_table1";

    String url1 = "jdbc:mysql://127.0.0.1:3307/demo1";
    String username1 = "root";
    String password1 = "";
    Neo master1 = Neo.connect(url1, username1, password1);

    String url2 = "jdbc:mysql://127.0.0.1:3407/demo1";
    String username2 = "root";
    String password2 = "";
    Neo master2 = Neo.connect(url2, username2, password2);

    String urlSlave1 = "jdbc:mysql://127.0.0.1:3308/demo1";
    String usernameSlave1 = "root";
    String passwordSlave1 = "";
    Neo slave1 = Neo.connect(urlSlave1, usernameSlave1, passwordSlave1);

    String urlSlave2 = "jdbc:mysql://127.0.0.1:3309/demo1";
    String usernameSlave2 = "root";
    String passwordSlave2 = "";
    Neo slave2 = Neo.connect(urlSlave2, usernameSlave2, passwordSlave2);

    String urlSlave3 = "jdbc:mysql://127.0.0.1:3408/demo1";
    String usernameSlave3 = "root";
    String passwordSlave3 = "";
    Neo slave3 = Neo.connect(urlSlave3, usernameSlave3, passwordSlave3);

    MasterSlaveNeo msNeo = new MasterSlaveNeo();
    msNeo.addMasterDb("master1", master1, true);
    msNeo.addMasterDb("master2", master2, false);
    msNeo.addSlaveDb("slave1", slave1);
    msNeo.addSlaveDb("slave2", slave2);
    msNeo.addSlaveDb("slave3", slave3);
    Random random = new Random();

    // 新增，走启用的主库master1
    msNeo.insert(tableName, NeoMap.of("name", "name" + index));
    // 查询，第一次使用slave1
    msNeo.one(tableName, NeoMap.of("name", "name" + index));
    // 查询，第一次使用slave2，后面依次循环
    msNeo.one(tableName, NeoMap.of("name", "name" + index));
}
```

### 分库分表
#### 分库（多库单表）
```java
/**
 * 测试分库
 */
@Test
public void devideDbTest() {
    UuidGenerator uuid = getUuidGenerator();
    uuid.addNamespaces("devideDb");
    List<Neo> neoList = getDevideDb(8);

    DevideNeo devideNeo = new DevideNeo();
    // 设置分库策略，对于自己框架的发号器，建议采用如下分库策略
    devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
    // 添加多库实例
    devideNeo.setDbList(neoList);
    // 设置分库及参数，其中表为对应的逻辑或者实际表名
    devideNeo.setDevideDb("neo_devide_table", "id");
    devideNeo.init();

    // 数据插入
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 100, "name", "name1"));
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 101, "name", "name2"));
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 102, "name", "name3"));

    // 查询所有库表
    DevideMultiNeo multiNeo = devideNeo.asDevideMultiNeo();
    System.out.println(multiNeo.list("neo_devide_table", NeoMap.of()));
}

private List<Neo> getDevideDb(Integer num) {
    List<Neo> neoList = new ArrayList<>();
    String url = "jdbc:mysql://localhost:3310/devide";
    String username = "root";
    String password = "";
    for (int i = 0; i < num; i++) {
        Neo db = Neo.connect(url + i, username, password);
        db.setExplainFlag(false);
        db.setMonitorFlag(false);
        neoList.add(db);
    }
    return neoList;
}

// 获取发号器实例
private UuidGenerator getUuidGenerator() {
    String url = "jdbc:mysql://localhost:3310/common";
    String username = "root";
    String password = "";
    return UuidGenerator.getInstance(Neo.connect(url, username, password));
}
```
解释：
其中类`DevideMultiNeo`跟`DevideNeo`的区别是，`DevideNeo`必须能够匹配到分库或者分表，如果匹配不到，则会走默认库和默认表（如果设置了的话）。而`DevideMultiNeo`实质上是分库分表中的多库多表处理，查询的时候，用于多库多表聚合查询，而`DevideNeo`的查询比如list这种，只有走到对应的分库查询而已
#### 分表（单库多表）
```java
/**
 * 测试分表，一个库中，有一个逻辑表，逻辑表对应多个实际的物理表
 */
@Test
public void devideTableTest() {
    UuidGenerator uuid = getUuidGenerator();
    uuid.addNamespaces("devideTable");

    String url = "jdbc:mysql://localhost:3310/devide_table";
    String username = "root";
    String password = "";

    DevideNeo devideNeo = new DevideNeo();
    // 对于一个库多个表的需要采用默认库
    devideNeo.setDefaultDb(Neo.connect(url, username, password));
    devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
    // 设置分库及参数，表示neo_devide_table0、neo_devide_table1、neo_devide_table2、...neo_devide_table7，一共8个表，分表采用列id来路由
    devideNeo.setDevideTable("neo_devide_table{0, 7}", "id");
    devideNeo.init();

    // 数据插入，自动根据字段走对应的路由
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideTable"), "user_id", 100, "name", "name1"));

    // 查询所有库表
    DevideMultiNeo multiNeo = devideNeo.asDevideMultiNeo();
    System.out.println(multiNeo.list("neo_devide_table", NeoMap.of()));
}
```
#### 分库分表（多库多表）
```java
/**
 * 测试分库分表
 * <p>
 * 4个分库以及每个库里面都有8个分表
 */
@Test
public void devideDbTableTest() {
    UuidGenerator uuid = getUuidGenerator();
    uuid.addNamespaces("devideDbTable", "userNamespace");

    List<Neo> neoList = getDevideDb();
    DevideNeo devideNeo = new DevideNeo();
    // 添加多库实例集合
    devideNeo.setDbList(neoList);
    // 设置分库分表的策略，对于我们自己的发号器方式，建议采用如下方式
    devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
    // 设置分库及参数，其中的表名为逻辑表的表名
    devideNeo.setDevideDb("neo_devide_table", "user_id");
    // 设置分表及参数
    devideNeo.setDevideTable("neo_devide_table{0, 7}", "id");
    // 初始化
    devideNeo.init();

    Long userId = uuid.getUUid("userNamespace");

    // 通过逻辑表明处理即可
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));
    devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));

    // 多库多表查询所有数据
    DevideMultiNeo devideMultiNeo = devideNeo.asDevideMultiNeo();
    System.out.println("one: == " + devideMultiNeo.one("neo_devide_table", NeoMap.of("id", 40901634569601024L)));
    System.out.println("list: == " + devideMultiNeo.list("neo_devide_table", NeoMap.of()));
    System.out.println("value: == " + devideMultiNeo.value("neo_devide_table", "user_id", NeoMap.of("id", 40901634569601024L)));
    System.out.println("values: == " + devideMultiNeo.values("neo_devide_table", "user_id", NeoMap.of()));
    System.out.println("page: == " + devideMultiNeo.page("neo_devide_table", NeoMap.of(), NeoPage.of(1, 10)));
    System.out.println("count: == " + devideMultiNeo.count("neo_devide_table"));
}
```
注意：
分库和分表的列不能为同一个，否则异常，因为如果同一个，则分表路由其实就无效了

### 异步
所有的api都有对应的异步api，列举其中几个接口api，接口太多，这里不再列举。其中线程池中的默认方式中，拒绝策略采用新的方式（重写了拒绝策略），即：如果线程池全部都满了，则任务阻塞在任务队列中
```java
    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<T> insertAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<T> insertAsync(String tableName, T object);


    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id);


    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, Executor executor);
...
```

### 多租户
在配置部分设置租户管理器

```java
// 设置租户信息
TenantHandler tenantHandler = new TenantHandler();
tenantHandler.setIncludeTables("*");
tenantHandler.setExcludeTables("neo_table1", "neo_table2");
tenantHandler.setColumnName("tenant_id");

neo.setTenantHandler(tenantHandler);
```
在应用的上下文部分，比如filter部分，设置租户id

```java
// 模拟租户1添加数据
TenantContextHolder.setTenantId("tenant_1");
```

然后其他地方就可以正常的使用了，示例，如下执行，不需要显示传入字段 tenant_id
```java
neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
```
目前内置支持的api都支持：insert、delete、update、one、page、getPage、list、count等

join，exe开头的几个函数不支持，因为这种sql比较复杂

#### 注意
对于在业务中使用了多线程的，在使用TenantContextHolder这个的时候，线程池必须用类`TtlExecutors`代理才行，示例：
```java
ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));
```
解释：
原因在于父线程和子线程的数据传递部分，这里采用的是阿里巴巴的TTl，也就是ITl（InheritableThreadLocal）的扩展版。ITL在线程池情况下由于线程复用会导致数据传递，而TTL能够在线程池情况下也能正常的在父子线程数据传递，采用的方式是线程池需要封装一层。


### 更多功能
- 数据库连接
- 基本功能
- DB异步
- 结构信息
- 批量功能
- 命名转换
- 事务
  - 单机事务
  - 分布式XA事务(待验证)
- sql监控
- 主从
- join
- 实体代码生成器
- 分布式id生成器
- 分库分表

## 技术咨询
目前咨询人数不多，暂时没有建群，有需求请添加微信
zhoumo187108
