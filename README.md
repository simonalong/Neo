# Neo：Orm框架

Neo是一个基于JDBC开发的至简化框架，名字源于《黑客帝国》男主角名字，寓意为连接虚拟与现实。开发源头，源于之前接触的一些ORM框架，思想很不错但是没开源无法使用，而且自己也有很多想法因此设计了该框架。

## 使用文档
[Neo文档介绍](https://simons.gitbook.io/neo/)
[最新Neo文档介绍](https://www.yuque.com/simonalong/neo)

## 快速入门
该框架接入比较简单，采用一个对象对应一个DataSource，然后这个对象拥有对表的各种操作

### maven引入
当前已经发布到maven中央仓库，直接使用即可，目前最低版本0.3.0，不同版本的api差距不小，建议从0.4.0版本开始
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>Neo</artifactId>
  <version>${latest.release.version}</version>
</dependency>
```

### 快速入门
一个DB对应的一个对象Neo，操作表，则填入对应的表名即可
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
    neo.execute("select * from %s where `group` =?", tableName, "group1");

    // 事务
    neo.tx(()->{
        neo.update(tableName, NeoMap.of("id", 12, "group", "value1"));
        neo.one(tableName, 12);
        neo.update("neo_table2", NeoMap.of("name", 12));
    });

    // 批量
    List<NeoMap> list = new ArrayList<>();
    list.add(NeoMap.of("group", "v1"));
    list.add(NeoMap.of("group", "v2"));
    list.add(NeoMap.of("group", "v3"));
    list.add(NeoMap.of("group", "v4"));
    neo.batchInsert(tableName, list);
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

    // 批量
    List<NeoMap> list = new ArrayList<>();
    list.add(NeoMap.of("group", "v1"));
    list.add(NeoMap.of("group", "v2"));
    list.add(NeoMap.of("group", "v3"));
    list.add(NeoMap.of("group", "v4"));
    table.batchInsert(list);
}
```

#### 注意
生成一个Neo对象除了可以通过url、user和password，还可以通过DataSource方式
```java
// 连接
Neo neo = Neo.connect(datasource);
```

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

    // 批量
    List<DemoEntity3> list = new ArrayList<>();
    list.add(new DemoEntity3().setGroup("group1").setUsName("name1"));
    list.add(new DemoEntity3().setGroup("group2").setUsName("name2"));
    list.add(new DemoEntity3().setGroup("group3").setUsName("name3"));
    list.add(new DemoEntity3().setGroup("group4").setUsName("name4"));
    table.batchInsertEntity(list);
}
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

### 分布式ID生成器
我们这里也提供了分布式ID生成器方案，采用的是改进版雪花算法，彻底解决了雪花算法存在的常见问题（时间回拨问题，workerId回收问题），对于如何解决的，具体方案可见文档，也可见我的另外一个项目[Butterfly](https://github.com/SimonAlong/Butterfly)（Neo框架中的发号器方案是Butterfly中的一个使用选项）

#### 使用
先建表，如果没有请创建
```sql
CREATE TABLE `neo_uuid_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `namespace` varchar(128) DEFAULT '' COMMENT '命名空间',
  `work_id` int(16) NOT NULL DEFAULT '0' COMMENT '工作id',
  `last_expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下次失效时间',
  `uid` varchar(128) DEFAULT '0' COMMENT '本次启动唯一id',
  `ip` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ip',
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

### 更多功能
- 数据库连接
- 基本功能
- DB异步
- 结构信息
- 批量功能
- 命名转换
- 事务
  - 单机事务
  - 分布式XA事务（测试中）
- sql监控
- 主从
- join
- 实体代码生成器
- 分布式
    - 全局id
- 动态分库分表(待支持)
- 多数据源(待验证)

技术讨论群：
请先加WX，并注明来源<br/><br/>
<img src="https://cdn.nlark.com/yuque/0/2020/jpeg/126182/1585718526680-2962b152-e238-410a-9b96-78c4a859cb94.jpeg#align=left&display=inline&height=786&name=WechatIMG22058.jpeg&originHeight=786&originWidth=564&size=54481&status=done&style=none&width=564" width = "200" height = "300" div align=left />
