# Neo：Orm框架

Neo是一个基于JDBC开发的，采用Hikaricp作为连接池的Orm框架。开发源头，源于几个原因，一个是之前几个公司采用的都是各自单独开发的Orm框架，这些框架不是开源，如果想自己使用，很多时候不方便，也不合适。另外一个是mybatis确实感觉不是很好用，把很多简单的东西设计的很复杂。最后是由于自己有很多想法，比如sql的规范落入到框架中、sql耗时统计和sql优化监控等等很多特性，且在之前接触的一些Orm框架中都没有。因此就有想法设计一个符合自己想法的Orm框架，下面的一些设计和各种特性有有借鉴之前接触到的一些优秀思想，也有在秉承着大道至简的原则进行的设计，框架刚起步，希望有兴趣的同学，一起添砖加瓦，共同成长。<br />下面介绍下框架的功能和一些用法，其中后面有（*）的部分是该框架亮点部分，也算特有部分。
<a name="SOWXF"></a>

# 目录：

* 一、[数据库连接](#数据库连接)
    * 1.[账号密码链接](#账号密码链接)
    * 2.[配置路径连接](#配置路径连接)
    * 3.[Datasource连接](#Datasource连接)
* ​二、[基本功能](#基本功能)
    * 1.[增加](#增加)
        * [参数详解](#参数详解)
        * [自增属性支持](#自增属性支持 )
    * 2.[删除](#删除)
    * 3.[修改](#修改)
    * 4.[查询](#查询)
        * a.[单行查询one](#单行查询one)
        * b.[多行查询list](#多行查询list)
        * c.[分页查询page](#分页查询page)
        * d.[个数查询count](#个数查询count)
        * e.[单值查询value](#单值查询value)
        * f.[单列多值查询values](#单列多值查询values)
        * f.[直接执行sql](#直接执行sql)
            * [直接执行](#直接执行)
            * [执行获取单行](#执行获取单行)
            * [执行获取多行](#执行获取多行)
            * [执行获取分页](#执行获取分页)
            * [执行获取个数](#执行获取个数)
            * [执行获取单个值](#执行获取单个值)
            * [执行获取单列多值](#执行获取单列多值)
    * 5.[其他查询](#其他查询)
        * [表单独查询](#表单独查询)
        * [In查询](#In查询)
* 三、[结构信息](#结构信息)
    * 1.[表信息](#表信息)
    * 2.[列信息](#列信息)
    * 2.[索引信息](#索引信息)
    * 2.[表创建的sql](#表创建的sql)
* 四、[批量功能](#批量功能)
    * 1.[批量插入](#批量功能)
    * 2.[批量更新](#批量更新)
* 五、[NeoMap类](#NeoMap类)
    * 1.[NeoMap初始化](#NeoMap初始化)
    * 2.[NeoMap和JavaBean转换](#NeoMap和JavaBean转换)
        * 1.[对象转换](#对象转换)
        * 2.[集合转换](#集合转换)
    * 3.[NeoMap和NeoMap转换](#NeoMap和NeoMap转换)
    * 4.[其他功能](#其他功能)
        * 1.[合并更多数据](#合并更多数据)
        * 2.[NeoMap获取固定列](#NeoMap获取固定列)
        * 3.[NeoMap列添加前缀](#NeoMap列添加前缀)
        * 4.[NeoMap列转换](#NeoMap列转换)
        * 5.[判空](#判空)
        * 6.[获取不同的列值类型](#获取不同的列值类型)
        * 7.[多表的列使用](#多表的列使用)
* 六、[命名转换](#命名转换)
    * 1.[转换风格](#转换风格)
    * 2.[NeoMap设置](#NeoMap设置)
* 七、[Columns类](#Columns类)
    * 1.[初始化](#初始化)
    * 2.[多表的处理](#多表的处理)
    * 3.[表所有列处理](#表所有列处理)
    * 4.[列别名处理](#列别名处理)
* 八、[单机事务](#单机事务)
    * 1.[事务只读](#事务只读)
    * 2.[事务隔离](#事务隔离)
* 九、[sql监控](#sql监控)
    * 1.[sql耗时监控](#sql耗时监控)
    * 2.[sql规范化监控](#sql规范化监控)
    * 3.[sql语句优化监控](#sql语句优化监控)
* 十、[主从](#主从)
    * 1.[mysql主从](#mysql主从)
* 十一、[join](#join)
    * [两表join](#两表join)
    * [多表join](#多表join)
    * [join类型](#join类型)
* 十二、[实体代码生成器](#实体代码生成器)
    * 1.[生成实体](#生成实体)
    * 2.[抽离公共枚举](#抽离公共枚举)
* 十三、[sql特殊处理](#sql特殊处理)
    * 1.[sql模糊查询](#sql模糊查询)
    * 1.[sql大小比较查询](#sql大小比较查询)
* 十四、[分布式](#分布式)
    * 1.[全局id](#全局id)
    * 2.[分布式锁](#分布式锁)
* 十五、[动态分库分表](#动态分库分表)
    * 1.[水平分表](#水平分表)
    * 2.[垂直分表](#垂直分表)
    * 3.[分库](#分库)
* 十六、[分布式事务](#分布式事务)
* 十七、[多数据源](#多数据源)
    * 1.[mysql](#mysql)
    * 2.[sqlLite](#sqlLite)
    * 3.[PostGresql](#PostGresql)

<h1 id="数据库连接">一、数据库连接：</h1>

针对数据库连接，这里支持多种连接方式
<a name="ZGBXc"></a>

<h3 id="账号密码链接">1.账号密码链接：</h3>

连接也是支持普通的密码账户连接

```java
String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
String user = "xxx";
String password = "xxx";
Neo neo = Neo.connect(url, user, password);
```

<a name="KQWul"></a>
<h3 id="配置路径连接">2.配置路径连接：</h3>
其中路径是class的配置文件路径

```
Neo neo = Neo.connect("/config/db.properties");
```
其中配置文件可以有如下这么几种：

```
dataSourceClassName=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
dataSource.user=xxx
dataSource.password=xxx
dataSource.databaseName=neo
dataSource.portNumber=3306
dataSource.serverName=127.0.0.1
```

```
jdbcUrl=jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false
dataSource.user=xxx
dataSource.password=xxx
```

```
jdbcUrl=jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false
username=xxx
password=xxx
autoCommit=true
poolName=Neo
maximumPoolSize=20
idleTimeout=180000
maxLifetime=1800000
connectionTimeout=30000
connectionTestQuery=select 1
minimumIdle=10
```

<a name="IffMd"></a>
<h3 id="Datasource连接">3.Datasource连接：</h3>
也支持Datasource的连接方式

```
Neo neo = Neo.connect(dataSource);
```

<a name="7WV5F"></a>
<h1 id="基本功能">二、基本功能</h1>

<a name="FXZXz"></a>
<h2 id="增加">1.增加</h2>
增加数据这里有如下这么几种方式

```
public NeoMap insert(String tableName, NeoMap valueMap) {}
public <T> T insert(String tableName, T entity) {}
public <T> T insert(String tableName, T entity, NamingChg naming) {}
```

<a name="mT4y2"></a>
<h4 id="参数详解">参数详解</h4>

- `tableName`：表名
- `valueMap`：是表中要插入的数据，其中`NeoMap`是自定义的`Map<String, Object>`结构，可实现多种常见的功能，详见下面的NeoMap介绍
- `NamingChg`：是实体`entity`的属性名字和`NeoMap`中`key`字段名字的相互映射，转换类型详见下面的介绍

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| valueMap | NeoMap | 待插入的数据 |
| entity | T | 待插入的数据，作为实体类型插入 |
| namingChg | NamingChg | 待插入的数据，通过该字符转换可以跟数据库字段对应上 |

**注意：**<br />其中`NeoMap`作为参数和`T entity`作为参数是一样的，下面的所有函数都是一样，`T entity`可以跟`NeoMap`相互转换，其中转换方式是`namingChg`，对于默认情况下，是`key`和`entity`的属性是完全对应的。
<a name="OzfgX"></a>
<h4 id="自增属性支持">自增属性支持</h4>

对于自增属性的支持，这里是默认支持，如果对应的表格的字段是主键且是自增字段，则插入之后，返回的值是含有生成的id的。<br />**比如：**

```sql
CREATE TABLE `neo_table1` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```
**测试插入：**

```java
@Test
@SneakyThrows
public void testInsert1(){
  NeoMap result = neo.insert(TABLE_NAME, NeoMap.of("group", "ok"));
  // {group=ok, id=14, name=}
  show(result);
}
```

<a name="Z6khJ"></a>
<h2 id="删除">2.删除</h2>
删除这里也有多种处理

```java
public Integer delete(String tableName, Long id) {}
public Integer delete(String tableName, NeoMap searchMap) {}
public <T> Integer delete(String tableName, T entity) {}
public <T> Integer delete(String tableName, T entity, NamingChg naming) {}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| id | Long | 表的主键列 |
| searchMap | NeoMap | 搜索条件 |
| namingChg | NamingChg | 待插入的数据，通过该字符转换可以跟数据库字段对应上 |

<a name="AT7RL"></a>
<h2 id="修改">3.修改</h2>
对数据库中的数据进行修改

```java
/**
 * 数据更新
 * @param tableName 表名
 * @param dataMap set的更新的数据
 * @param searchMap where后面的语句条件数据
 * @return 更新之后的返回值
 */
public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {}
public <T> T update(String tableName, T setEntity, NeoMap searchMap, NamingChg namingChg) {}
public <T> T update(String tableName, T setEntity, NeoMap searchMap) {}
public <T> T update(String tableName, T setEntity, T searchEntity) {}
public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {}
public <T> T update(String tableName, T entity, Columns columns, NamingChg namingChg) {}
public <T> T update(String tableName, T entity, Columns columns) {}

public NeoMap update(String tableName, NeoMap dataMap) {}
public <T> T update(String tableName, T entity) {}

public <T> T update(String tableName, T entity, Columns columns) {}
public <T> T update(String tableName, T entity, Columns columns, NamingChg namingChg) {}
```
`update`的重载函数有点多，但是终究只是对`update`对应的sql的简单拼装，其中对应的参数如下

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| dataMap | NeoMap | update待更新的数据 |
| setEntity | T | update待更新的数据，跟dataMap一样，只是这里是实体形式 |
| searchMap | NeoMap | update更新的条件，用于在where后面的子句中 |
| searchEntity | T | update更新的条件，用于在where后面的子句中，这里是实体形式 |
| columns | Columns | 指定的在dataMap数据的对应的列，以节省再输入的麻烦，可以为NeoMap指定列，也可以为entity实体指定属性 |
| namingChg | NamingChg | 待插入的数据，通过该字符转换可以跟数据库字段对应上 |

例子：

```java
@Test
@SneakyThrows
public void testUpdate1(){
  // update neo_table1 set `group`=? where `group` =  ? and `name` =  ?
  neo.update(TABLE_NAME, NeoMap.of("group", "ok2"), NeoMap.of("group", "group2", "name", "name"));
}

@Test
@SneakyThrows
public void testUpdate2(){
  // update neo_table1 set `group`=?, `name`=? where `name` =  ?
  show(neo.update(TABLE_NAME, NeoMap.of("group", "ok3", "name", "name"), Columns.of("name")));
}

@Test
@SneakyThrows
public void testUpdate3(){
  DemoEntity input = new DemoEntity();
  input.setGroup("group2");
  // update neo_table1 set `group`=? where `group` =  ? and `name` =  ?
  show(neo.update(TABLE_NAME, input, NeoMap.of("group", "group1", "name", "name")));
}

@Test
@SneakyThrows
public void testUpdate4(){
  DemoEntity search = new DemoEntity();
  search.setGroup("group1");

  DemoEntity data = new DemoEntity();
  data.setGroup("group2");
  // update neo_table1 set `group`=? where `group` =  ?
  show(neo.update(TABLE_NAME, data, search));
}

/**
     * 指定某个列作为查询条件
     */
@Test
@SneakyThrows
public void testUpdate5(){
  // update neo_table1 set `group`=?, `name`=? where `group` =  ?
  show(neo.update(TABLE_NAME, NeoMap.of("group", "group1", "name", "name2"), Columns.of("group")));
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
  // update neo_table1 set `group`=?, `name`=? where `name` =  ?
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
```

**注意：**<br />对于如下两个函数，可以看到没有指定搜索条件，对于这种，这里采用的是，如果dataMap和entity中含有主键，则默认会将该主键设置为后面的搜索条件，比如下面的例子

```java
public NeoMap update(String tableName, NeoMap dataMap) {}
public <T> T update(String tableName, T entity) {}
```

```java

/**
 * 不指定则，默认查找搜索条件中的主键列对应的值为搜索条件
 */
@Test
@SneakyThrows
public void testUpdate6(){
  // update neo_table1 set `group`=?, `id`=?, `name`=? where `id` =  ?
  show(neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group222", "name", "name2")));
}

/**
 * 指定某个列作为查询条件，默认查找搜索条件中的主键列对应的值为搜索条件
 */
@Test
@SneakyThrows
public void testUpdate7(){
  DemoEntity search = new DemoEntity();
  search.setId(281L);
  search.setGroup("group555");
  // update neo_table1 set `group`=?, `id`=? where `id` =  ?
  show(neo.update(TABLE_NAME, search));
}
```

<a name="ojz36"></a>
<h2 id="查询">4.查询</h2>
在查询这里，增加了这么几类函数（有部分是借鉴别的框架）：

- 单行查询one
- 多行查询list
- 分页查询page
- 个数查询count
- 单个value
- 单列多值values
- 直接执行sql
  - 直接执行
  - 执行获取单行
  - 执行获取多行
  - 执行获取个数
  - 执行获取单个值
  - 执行获取单列多值
<a name="IwMpH"></a>

<h3 id="单行查询one">a.单行查询one</h3>

```java
public NeoMap one(String tableName, NeoMap searchMap){}
public <T> T one(String tableName, T entity){}
public NeoMap one(String tableName, Columns columns, NeoMap searchMap){}
public <T> T one(String tableName, Columns columns, T entity){}
public NeoMap one(String tableName, NeoMap searchMap, String tailSql){}
public <T> T one(String tableName, T entity, String tailSql){}
public NeoMap one(String tableName, Columns columns, NeoMap searchMap, String tailSql) {}
public <T> T one(String tableName, Columns columns, T entity, String tailSql){}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| searchMap | NeoMap | where条件后面的搜索条件 |
| entity | T | where条件后面的搜索条件，实体形式 |
| columns | Columns | 返回结果中指定的列 |
| tailSql | String | sql后面的拼接语句，放在where的条件后面，比如order by xxx |

**注意：**<br />one执行的时候会自动在sql语句最后添加`limit 1`
<a name="mkT1Z"></a>

<h3 id="多行查询list">b.多行查询list</h3>

多行查询函数的参数跟单行查询的函数是相同的

```java
public List<NeoMap> list(String tableName, NeoMap searchMap){}
public <T> List<T> list(String tableName, T entity){}
public <T> List<T> list(String tableName, Columns columns, T entity){}
public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap){}
public List<NeoMap> list(String tableName, NeoMap searchMap, String tailSql){}
public <T> List<T> list(String tableName, T entity, String tailSql){}
public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap, String tailSql) {}
public <T> List<T> list(String tableName, Columns columns, T entity, String tailSql){}
```
参数同`one`
<a name="XziYV"></a>

<h3 id="分页查询page">c.分页查询page</h3>

```java

public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page){}
public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page){}

public <T> List<T> page(String tableName, T entity, Integer startIndex, Integer pageSize){}
public <T> List<T> page(String tableName, Columns columns, T entity, String tailSql, NeoPage page){}
public <T> List<T> page(String tableName, T entity, String tailSql, NeoPage page){}
public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page){}
public <T> List<T> page(String tableName, T entity, NeoPage page){}
public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, String tailSql, Integer startIndex, Integer pageSize) {}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| searchMap | NeoMap | where条件后面的搜索条件 |
| entity | T | where条件后面的搜索条件，实体形式 |
| columns | Columns | 返回结果中指定的列 |
| tailSql | String | sql后面的拼接语句，放在where的条件后面，比如order by xxx |
| page | NeoPage | 内部包含pageIndex, pageSize, pageNo |
| startIndex | Integer | limit 中的起始位置 |
| pageSize | Integer | limit 中的分页大小 |

<a name="Pyv0m"></a>

<h3 id="个数查询count">d.个数查询count</h3>

针对个数的查询有下面这个几个函数

```java
public Integer count(String tableName, NeoMap searchMap) {}
public Integer count(String tableName, Object entity) {}
public Integer count(String tableName) {}
```

<a name="nekuR"></a>

<h3 id="单值查询value">e.单值查询value</h3>

针对这个查询，其实就是查询某一行中的某个列的值

```java
public String value(String tableName, String field, Object entity) {}
public String value(String tableName, String field, NeoMap searchMap) {}
public String value(String tableName, String field, Object entity, String tailSql) {}
public String value(String tableName, String field, NeoMap searchMap, String tailSql){}
public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {}
public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {}
public <T> T value(Class<T> tClass, String tableName, String field, Object entity, String tailSql) {}
public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap, String tailSql) {}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tClass | Class | 返回值的Class类型 |
| tableName | String | 表名 |
| field | String | 要查询的列名 |
| entity | T | where条件后面的搜索条件，实体形式 |
| searchMap | NeoMap | where条件后面的搜索条件 |
| tailSql | String | sql后面的拼接语句，放在where的条件后面，比如order by xxx |

<a name="w9ebU"></a>

<h3 id="单列多值查询values">f.单列多值查询values</h3>

单列多值的函数为`values`，对应的参数和上面介绍的`value`相同
<a name="ieNE8"></a>

<h3 id="直接执行sql">g.直接执行sql</h3>

直接执行这里面除了直接执行数据之外，还可以执行获取单列，单值，多列，个数等
<a name="jkBKk"></a>

<h4 id="直接执行">直接执行</h4>

```java
public List<List<NeoMap>> execute(String sql, Object... parameters) {}
```


sql是包含java的String的格式化转换符和JDBC的占位符"?"的，比如

```java
neo.execute("update %s set `group`=?, `name`=%s where id = ?", TABLE_NAME, "group121", "'name123'", 121)
```


**占位符：**<br />java的转换符：
| 转换符 | 说明 | 示例i |
| --- | --- | --- |
| %s | 字符串类型 | "mingrisoft" |
| %c | 字符类型 | 'm' |
| %b | 布尔类型 | true |
| %d | 整数类型（十进制） | 99 |
| %x | 整数类型（十六进制） | FF |
| %o | 整数类型（八进制） | 77 |
| %f | 浮点类型 | 99.99 |
| %a | 十六进制浮点类型 | FF.35AE |
| %e | 指数类型 | 9.38e+5 |
| %g | 通用浮点类型（f和e类型中较短的） |   |
| %h | 散列码 |   |
| %% | 百分比类型 | ％ |
| %n | 换行符 |   |
| %tx | 日期与时间类型（x代表不同的日期与时间转换符 |   |

注意：<br />该execute不支持多语句执行，如果想执行多语句，则可以用事务方式，参考下面的tx，或者可以通过多结果集的方式，举例如下：

```java
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
```


<a name="cF726"></a>

<h4 id="执行获取单行">执行获取单行</h4>

执行单行其实就是在sql的最后添加limit 1，并返回唯一一个结果实体

```java
public NeoMap exeOne(String sql, Object... parameters) {}
public <T> T exeOne(Class<T> tClass, String sql, Object... parameters){}
```
<a name="JJOwD"></a>

<h4 id="执行获取多行">执行获取多行</h4>

```java
public List<NeoMap> exeList(String sql, Object... parameters) {}
public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters){}
```
<a name="rg4VA"></a>

<h4 id="执行获取分页">执行获取分页</h4>

```java
public List<NeoMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters){}
public List<NeoMap> exePage(String sql, NeoPage neoPage, Object... parameters){}
```
<a name="32o96"></a>

<h4 id="执行获取个数">执行获取个数</h4>

```java
public Integer exeCount(String sql, Object... parameters) {}
```
<a name="tU5Lh"></a>

<h4 id="执行获取单个值">执行获取单个值</h4>

```java
public String exeValue(String sql, Object... parameters){}
public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {}
```
<a name="GAzwT"></a>

<h4 id="执行获取单列多值">执行获取单列多值</h4>

```java
public List<String> exeValues(String sql, Object... parameters){}
public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {}
```

<a name="vpj88"></a>

<h2 id="其他查询">5.其他查询</h>

除了常见的查询之外，这里还提供了如下的查询：

<a name="74ZqM"></a>

<h3 id="其他查询">a.表单独查询(*)</h3>

前面所有的查询都是通过传入一个表名，进而查询一个表的对应的信息，这里可以先获取一个表信息，然后就不需要再传入表名了，获取表对象的函数为

```java
public NeoTable getTable(String tableName){}
```
获取NeoTable对象之后，这个类是包含Neo中的相关的数据查询的<br />比如：<br />单行查询one

```java
// 不指定列，则查询所有的列
public NeoMap one(NeoMap searchMap){}
public NeoMap one(Columns columns, NeoMap searchMap){}
public <T> T one(Columns columns, T entity){}
public NeoMap one(Columns columns, NeoMap searchMap, String tailSql) {}
```
其中所有的函数跟Neo的都是一样的唯一的区别就是这里少了一个表名参数<br />以及其他的：

- 增加insert
- 删除delete
- 修改update
- 多行查询list
- 分页查询page
- 个数查询count
- 单个查询value
- 单列查询values
- 批量插入batchInsert（下面会介绍）
- 批量更新batchUpdate（下面会介绍）
<a name="41ana"></a>

<h3 id="In查询">b.In查询</h3>

针对常见的in的查询，这里也提供了一个专门构造sql的类SqlBuilder，里面有一个构造in语句的方法in()

```java
public <T> String in(List<T> values) {}
```

用法示例：
```java
/**
 * 查询一行数据
 * 采用直接执行sql方式，设定返回实体类型
 */
@Test
@SneakyThrows
public void testExeList6(){
  neo.setExplainFlag(true);
  List<Integer> idList = Arrays.asList(310, 311);
  // select * from neo_table1 where id in ('310','311')
  show(neo.exeList("select * from %s where id in %s", TABLE_NAME, SqlBuilder.in(idList)));
}
```

<a name="AO5Ly"></a>

<h1 id="结构信息">三、结构信息</h1>

除了常见的查询之外，这里还提供了对于库的表结构的查询：
<a name="f8ada07a"></a>

<h3 id="表信息">1.表信息</h3>

查询库中表列表：

```java
public List<String> getAllTableNameList(){}
```

<a name="47061b39"></a>

<h3 id="列信息">2.列信息</h3>

表的所有列名：

```java
public Set<String> getColumnNameList(String tableName){}
public List<NeoColumn> getColumnList(String tableName){}
```

<a name="e46328b9"></a>

<h3 id="索引信息">3.索引信息</h3>

表的所有索引信息：

```java
public List<Index> getIndexList(String tableName){}
public List<String> getIndexNameList(String tableName){}
```

<a name="c21b6bee"></a>

<h3 id="表创建的sql">4.表创建的sql(*)</h3>

表的创建语句：

```java
/**
* 获取创建sql的语句
* {@code
* create table xxx{
*     id xxxx;
* } comment ='xxxx';
* }
*
* @param tableName 表名
* @return 表的创建语句
*/
public String getTableCreate(String tableName){
  return (String) (execute("show create table `" + tableName + "`").get(0).get(0).get("Create Table"));
}
```
该功能是一个很不错的功能，在进行动态化分表的时候，获取原表的创建语句，修改表名，然后再配合execute执行创建表语句，则可以创建新的表，即可以实现动态的分库分表（其中动态化是利用自己开发的分布式配置中心的配置热启动功能，该分布式配置中心请见[这里](https://www.yuque.com/simonalong/xiangmu/pxg5a8)）。
<a name="xrGjL"></a>

<h3 id="批量功能">四、批量功能</h3>

对于批量功能，这里提供了批量更新和批量插入
<a name="151c1be6"></a>

<h3 id="批量插入">1.批量插入</h3>

```java
public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {}
public <T> Integer batchInsertEntity(String tableName, List<T> dataList){}
public <T> Integer batchInsertEntity(String tableName, List<T> dataList, NamingChg namingChg){}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| dataMapList | List<NeoMap> | 要插入的数据 |
| dataList | List<T> | 要插入的数据，实体化列表形式 |
| namingChg | NamingChg | 实体跟NeoMap（或者叫跟DB）字段映射 |

<a name="c0d24f84"></a>

<h3 id="批量更新">2.批量更新</h3>

```java
public Integer batchUpdate(String tableName, List<NeoMap> dataList){}
public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns){}
public <T> Integer batchUpdateEntity(String tableName, List<T> dataList){}
public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns, NamingChg namingChg){}
public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns){}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| tableName | String | 表名 |
| dataMapList | List<NeoMap> | 要插入的数据 |
| dataList | List<T> | 要插入的数据，实体化列表形式 |
| namingChg | NamingChg | 实体跟NeoMap（或者叫跟DB）字段映射 |

提示：<br />上面的批量更新和批量插入，在NeoTable中也都是支持的，少了一个tableName参数，使用起来会更加方便
<a name="xR3E0"></a>

<h1 id="NeoMap类">五、NeoMap类</h1>

NeoMap类借鉴之前Orm框架中的一个Map类，除了借鉴一些功能和思想之外，这里又增加了一些其他的功能。该类是该框架中一个比较重要且最常见的类，在框架中NeoMap就是默认跟实体对象等价的，NeoMap是直接继承自Map<String, Object>的一个线程安全的Map类，内部采用有序的线程安全类ConcurrentSkipLinkedMap进行存储。该类除了具备基本的Map的一些功能外，还增加了很多额外的功能，基本功能不在介绍，这里主要介绍额外的功能，功能和用法列举下。
<a name="jlB3l"></a>

<h2 id="NeoMap初始化">1.NeoMap初始化</h2>

NeoMap初始化方式很多，使得Map的使用更加的方便

```java
// 根据key-value-key-value...这种初始化，key为String
public static NeoMap of(Object... kvs) {}

// 根据对象直接获取
public static NeoMap from(Object object) {}

// 根据对象转换获取，其中namingChg是将对象的属性转换为DB中的字段风格字段
public static NeoMap from(Object object, NamingChg namingChg) {}

// 指定对象中的某些属性放到NeoMap中
public static NeoMap from(Object object, Columns columns) {}

// 指定对象中的某些属性，同时也指定对象的属性名字和NeoMap的转换，其中NeoMap的key默认是跟DB中的字段对应
public static NeoMap from(Object object, Columns columns, NamingChg namingChg) {}

// 对象的转换，其中userDefineNaming是用于自定的对象属性和转换到NeoMap中的属性
public static NeoMap from(Object object, NeoMap userDefineNaming) {}

// 对象中指定某些属性转换，跟上面的参数columns一个效果
public static NeoMap fromInclude(Object object, String... fields) {}

// 对象中排除某些属性转换
public static NeoMap fromExclude(Object object, String... fields) {}

// 对象中指定某些属性，且排除某些属性转换
public static NeoMap from(Object object, NeoMap userNaming, List<String> inFieldList, List<String> exFieldList) {}

// 对象中指定某些属性，且排除某些属性转换，转换方式通过风格转换器指定
public static NeoMap from(Object object, NamingChg naming, List<String> inFieldList, List<String> exFieldList) {}

// 将其他NeoMap对应的key通过风格变换器转换过来
public static NeoMap fromMap(NeoMap sourceMap, NamingChg namingChg) {}
```

<a name="AHP1F"></a>

<h2 id="NeoMap和JavaBean转换">2.NeoMap和JavaBean转换</h2>

<a name="ZADGY"></a>

<h3 id="对象转换">1.对象转换</h3>

```java
// Neo转到指定的实体，默认风格转换器就是不转换
public <T> T as(Class<T> tClass) {}

// Neo转到指定的实体，但是要在指定的风格转换下
public <T> T as(Class<T> tClass, NamingChg naming) {}
```

<a name="XY2EP"></a>

<h3 id="集合转换">2.集合转换</h3>


```java
// List<NeoMap>转换到集合List<T>
public static <T> List<T> asArray(List<NeoMap> neoMaps, Class<T> tClass) {}

// List<NeoMap>根据指定的风格进行转换到集合List<T>
public static <T> List<T> asArray(List<NeoMap> neoMaps, Class<T> tClass, NamingChg namingChg) {}

// List<T> 转换到List<NeoMap>
public static <T> List<NeoMap> fromArray(List<T> dataList) {}

// List<T> 根据指定风格转换到List<NeoMap>
public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg) {}

// List<T> 指定一些列转换到List<NeoMap>
public static <T> List<NeoMap> fromArray(List<T> dataList, Columns columns) {}

// List<T> 根据指定风格将指定的列转换到List<NeoMap>
public static <T> List<NeoMap> fromArray(List<T> dataList, Columns columns, NamingChg namingChg) {}
```

<a name="wasZ4"></a>

<h3 id="NeoMap和NeoMap转换">3.NeoMap和NeoMap转换</h3>

这里转换我们只添加了key的风格转换，这里分两种，小驼峰风格到其他风格和其他风格到小驼峰风格

```java
// key风格从小驼峰到其他
public NeoMap keyChgFromSmallCamelTo(NamingChg namingChg){}

// key风格从其他到小驼峰
public NeoMap keyChgToSmallCamelFrom(NamingChg namingChg){}
```

<a name="TRL0m"></a>

<h2 id="其他功能">4.其他功能</h2>

<a name="nfTIP"></a>

<h3 id="合并更多数据">1.合并更多数据</h3>

```java
// 关联另外的NeoMap跟putAll一样
public NeoMap append(NeoMap neoMap) {}

// 增加数据，跟put一样，只是返回值是NeoMap，便于链式操作
public NeoMap append(String key, Object value) {}

// 跟putAll差不多，不过这里新增了命名转换器，用于key的转换
public NeoMap putAll(NeoMap sourceMap, NamingChg namingChg) {}
```

<a name="itpW6"></a>

<h3 id="NeoMap获取固定列">2.NeoMap获取固定列</h3>

```java
// 这是一个NeoMap只保留指定的列，并生成一个新的NeoMap
public NeoMap assign(Columns columns) {}
```

<a name="aDjIj"></a>

<h3 id="NeoMap列添加前缀">3.NeoMap列添加前缀</h3>

有些时候我们需要给NaoMap中的列添加一些前缀

```java
// 给所有的key添加前缀，比如给所有的列添加"x_"
public NeoMap setKeyPre(String preFix) {}
```

<a name="piU9k"></a>

<h3 id="NeoMap列转换">4.NeoMap列转换</h3>

key进行转换

```java
// 对NeoMap中的key进行转换，keys：oldKey-newKey-oldKey-newKey-...
public NeoMap keyConvert(String... keys) {}
```

<a name="WWfQm"></a>

<h3 id="判空">5.判空</h3>

判空这里可以对NeoMap判空也可以对集合判空

```java
// 集合判空
public static boolean isEmpty(Collection<NeoMap> neoMaps) {
	return neoMaps == null || neoMaps.isEmpty() || neoMaps.stream().allMatch(Map::isEmpty);
}

// NeoMap判空
public static boolean isEmpty(NeoMap neoMap) {
  return neoMap == null || neoMap.isEmpty();
}
```

<a name="NInFl"></a>

<h3 id="获取不同的列值类型">6.获取不同的列值类型</h3>

由于NeoMap的value是Object类型，因此获取值之后还是要进行转换，这里借鉴之前的Orm框架，对数据进行转换

```java
// 根据指定的类型获取对应的数据
public <T> T get(Class<T> tClass, String key){}

// 获取空则有默认值，下面所有的类型都有两个函数，这里的这个默认值后面不再写了
public <T> T get(Class<T> tClass, String key, T defaultValue){}

public Character getCharacter(String key){}
public String getStr(String key){}
public Boolean getBoolean(String key) {}
public Byte getByte(String key){}
public Short getShort(String key){}
public Integer getInteger(String key){}
public Long getLong(String key){}
public Double getDouble(String key){}
public Float getFloat(String key){}

// 获取集合对象，注意：对于原先对象为非tClass的List，如果可以转为tClass，那么这里也是支持的，下面有解释
public <T> List<T> getList(Class<T> tClass, String key){}
// 获取集合Set对象，同上
public <T> Set<T> getSet(Class<T> tClass, String key){}
// 将值获取为NeoMap，对于value为普通对象的也可以用该方法，会自动的转换为NeoMap
public NeoMap getNeoMap(String key){}
```

对于上面的获取，其中需要注意的是，对于有Class<T> tClass参数的函数，其中tClass可以不是原先存储的类型，只要原类型可以转换为的类型即可，如下例子

```java
/**
 * 其中原存储类型是String，但是按照Integer类型获取依然可以获取到
 */
@Test
public void getClassTest1(){
  NeoMap neoMap = NeoMap.of("a", "1", "b", "2");
  Integer result = neoMap.get(Integer.class, "a");
	Assert.assertTrue(result.equals(1));
}

/**
 * getNeoMap除了返回值可以为NeoMap之外，还可以是普通对象
 */
@Test
public void getNeoMapTest2(){
  DemoEntity demoEntity = new DemoEntity().setName("name").setId(12L).setUserName("user");

  NeoMap data = NeoMap.of("a", NeoMap.of("name", "name", "id", 12L, "userName", "user"));
  Assert.assertTrue(demoEntity.equals(data.get(DemoEntity.class, "a")));
}

/**
 * getNeoMap除了返回值可以为实体对象外，还可以是NeoMap
 */
@Test
public void getNeoMapTest3(){
  DemoEntity demoEntity = new DemoEntity().setName("name").setId(12L).setUserName("user");

  NeoMap data = NeoMap.of("a", demoEntity);

  NeoMap result = NeoMap.of("name", "name", "id", 12L, "userName", "user");
  Assert.assertTrue(result.equals(data.get(NeoMap.class, "a")));
}

/**
 * 对于枚举类型，原值为String也可以获取到枚举类型
 */
@Test
public void getEnumTest2(){
  NeoMap neoMap = NeoMap.of("enum", "A1");
  Assert.assertEquals(EnumEntity.A1, neoMap.get(EnumEntity.class, "enum"));
}
```
其中其他的几个跟Class有关的函数也是一样的，也是可以转换为对应的兼容类型的，比如：
> getList， getSet，getNeoMap

兼容类型：

| 原类型 | 可以兼容的类型 | 说明 |
| --- | --- | --- |
| NeoMap | 自定义类型 | 自定义类型里面属性只要包含NeoMap中的key相同即可 |
| 自定义类型 | NeoMap |  |
| 非String的基本类型 | String |  |
| String | 枚举类型 | 只要值和枚举中的值相同即可转换 |
| String | Boolean | 只有原类型的值为true或者false才行 |
| String | Character | 取String的第一个字符 |
| String | Byte, Short, Integer, Long, Float, Double | 字符串是数字字符才可转换 |
| 非集合，非枚举，非数组的普通类型 | NeoMap | 对应的就是getNeoMap()函数 |
| java.sql.Date,<br />java.sql.Time,<br />java.sql.TimeStamp, <br />java.utl.Date | Long | 对应函数：get(Long.class, String key)和getLong(String key) |

<a name="mraxd"></a>

<h3 id="多表的列使用">7.多表的列使用</h3>

在下面介绍的join中有种场景是多表关联，而多表关联，则需要用到多个表的搜索条件，那么多表的搜索条件应该如何使用，如果按照上面的通常情况下，那么就可能会像这样使用。

```java
NeoMap.of("table1.`name`", "a", "table1.`age`", 123, "table2.`group`", "g1", "table3.`name`", "k");
```
我们可以看到，在列和表更多的时候，写起来特别麻烦，而且里面很多key的字段有些都是相同的，那么因此就做了如下的设计，下面的效果与上面的完全一致

```java
NeoMap.table(table1).cs("name", "a", "age", 123)
            .and(table2).cs("group", "g1")
            .and(table3).cs("name", "k");
```

新增了静态函数和几个非静态函数，全部都支持链式写法

```java
public static NeoMap table(String tableName){}
public NeoMap cs(Object... kvs) {}
public NeoMap and(String tableName){}
```

<a name="6r6XH"></a>

<h1 id="命名转换">六、命名转换</h1>

针对数据库的命名字段风格和系统内部的命名风格不一样，这里增加了风格转换（有借鉴其他框架思想，具体实现不同）。其中风格转换主要就是基于NeoMap进行风格转换，NeoMap默认就是跟DB中的字段保持一致的，而转换到具体的实体中，则需要进行转换
<a name="lE9n5"></a>

<h2 id="转换风格">1.转换风格</h2>

项目中设定的风格为如下基本这些，而更多的转换，则可以将相互的风格再嵌套即可扩展更多

| 转换类型 | 风格1 | 风格2 |
| --- | --- | --- |
| DEFAULT | 风格不转换 |  |
| BIGCAMEL | 小驼峰（dataBaseUser） | 大驼峰（DataBaseUser） |
| UNDERLINE | 小驼峰（dataBaseUser） | 下划线（data_base_user） |
| PREUNDER | 小驼峰（dataBaseUser） | 前下划线（_data_base_user） |
| POSTUNDER | 小驼峰（dataBaseUser） | 后下划线（data_base_user_） |
| PREPOSTUNDER | 小驼峰（dataBaseUser） | 前后下划线（_data_base_user_） |
| MIDDLELINE | 小驼峰（dataBaseUser） | 中划线（data-base-user） |
| UPPERUNER | 小驼峰（dataBaseUser） | 大写下划线（DATA_BASE_USER） |
| UPPERMIDDLE | 小驼峰（dataBaseUser） | 大写中划线（DATA-BASE-USER） |

<a name="ifeWS"></a>

<h2 id="NeoMap设置">2.NeoMap设置</h2>

对于NeoMap和实体转换时候需要设定风格，可以每次都在函数中添加转换，也可以设定全局转换
<a name="39hoz"></a>

<h4 id="单独设置">单独设置</h4>

在实体entity作为参数的时候，一般情况下后面都会有一个参数是namingChg，这个参数是用于entity向NeoMap转换。<br />**注意：**<br />其中转换函数有两个：一个是风格1向风格2转换，一个是风格2向风格1转换，对于实体向NeoMap则是采用风格1向风格2转换函数，而NeoMap向entity转换也是用到风格1向风格2的函数，只是用该函数获取字段，然后对字段赋值而已，只有特殊情况下才会用到风格2向风格1的转换。这些都不需要使用者关心，只需要知道一下即可。
<a name="L2Phd"></a>

<h4 id="全局设置">全局设置</h4>

如果我们不想每次都那么麻烦的转换，则可以对NeoMap设置全局转换

```java
public static void setDefaultNamingChg(NamingChg namingChg) {}
```

**注意：**<br />一旦设置了，所有的NeoMap到实体的转换都是用这个转换方式了
<a name="5XOvN"></a>

<h1 id="Columns">七、Columns</h1>

在代码内部这个类比较常见，就单独讲解一下，该类主要是用于对列进行处理并将普通的字符转换为sql所需要的格式。<br />普通的列名经过这里处理之后是要变成这样的格式：

```
name -> `name`
table.name -> table.`name`
name as n -> `name` as n
name n -> `name` n
table.name as n -> table.`name` as n
```

<a name="doqyv"></a>

<h3 id="初始化">1.初始化</h3>

```java
public static Columns of(String... fields) {}
public static Columns of(List<Field> fieldList) {}

// 获取表tableName的所有列名
public static Columns from(Neo neo, String tableName) {}
// 获取某个类的所有列
public static Columns from(Class tClass) {}
// 根据类型转换获取类的属性转换
public static Columns from(Class tClass, NamingChg namingChg) {}

// 设置列的前缀，和cs(String... cs)结合使用，用于表示列的前缀
public static Columns table(String tableName){}
public static Columns table(String tableName, Neo neo){}
```

<a name="aA4HO"></a>

<h3 id="多表的处理">2.多表的处理</h3>

在使用多表的时候，我们可以直接使用of函数，将所有的列放进去，不过也可以使用下面的方式

```java
public static Columns table(String tableName){}
public static Columns table(String tableName, Neo neo){}

public Columns cs(String... cs){}
public Columns cs(Set<String> fieldSets) {}
```

```java
@Test
public void tableCsTest1() {
  // tableName.`c2`, tableName.`c3`, tableName.`c1`
  Assert.assertEquals(Columns.of("tableName.`c2`", "tableName.`c3`", "tableName.`c1`"),
                      Columns.table("tableName").cs("c1", "c2", "c3"));
}

/**
 * 多表的数据字段
 */
@Test
public void tableCsTest3() {
  String table1 = "table1";
  String table2 = "table2";
  String table3 = "table3";
  Columns columns = Columns.table(table1).cs("a1", "b1")
    .and(table2).cs("a2", "b2")
    .and(table3).cs("a3", "b3");

  // table3.`b3`, table1.`b1`, table1.`c1`, table1.`a1`, table2.`c2`, table2.`b2`, table2.`a2`, table3.`a3`
  show(columns.buildFields());
  Assert.assertEquals(Columns.of("table1.`a1`", "table1.`b1`", "table2.`a2`", "table2.`b2`", "table3.`a3`", "table3.`b3`"), columns);
}
```

<a name="SV7db"></a>

<h3 id="表所有列处理">3.表所有列处理</h3>

针对需要表的所有列的时候，平常是用"*"，但是sql规范中是不建议使用"*"，我们可能需要手写，而手写又太多了，那么我们这里增加这样的写法，在使用符号“*”的时候要传入库对象Neo，否则会报异常。

```java
/**
* 获取所有的列名 "*"
*/
@Test
public void allColumnTest1() {
  // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
  Columns columns = Columns.table("neo_table1", neo).cs("*");

  Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"),
                      columns);
}

/**
 * 获取所有的列名 "*"，有多余的列，则会覆盖
 */
@Test
public void allColumnTest2() {
  // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
  Columns columns = Columns.table("neo_table1", neo).cs("*", "group");
  Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"), columns);
}

/**
 * 获取所有的列名 "*"，如果有别名，则以别名为主
 */
@Test
public void allColumnTest3() {
  // neo_table1.`user_name`, neo_table1.`age`, neo_table1.`group` as g, neo_table1.`id`, neo_table1.`name`
  Columns columns = Columns.table("neo_table1", neo).cs("*", "group as g");
  Assert.assertEquals(Columns.of("neo_table1.`group` as g", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"), columns);
}
```

<a name="8jZmx"></a>

<h3 id="列别名处理">4.列别名处理</h3>

针对别名处理的时候，我们这里支持这么两种方式：as 方式和空格方式

```java
@Test
public void aliasTest1(){
  // `c1` s c11, `c2`, `c3`
  show(Columns.of("`c1` as c11", "`c2`", "`c3`"));
}

@Test
public void aliasTest2(){
  // `c1` c11, `c2`, `c3`
  show(Columns.of("c1 c11", "`c2`", "`c3`"));
}

@Test
public void aliasTest3(){
  // `c1`  c11, `c3`
  show(Columns.of("c1  c11", "`c1`", "`c3`").buildFields());
}

@Test
public void aliasTest4(){
  // table.`c1`  c11, `c3`
  show(Columns.of("table.c1  c11", "`c1`", "`c3`").buildFields());
}
```

别名中，我们会将有别名的列和对应的列认为是相同的列

<a name="rmc4l"></a>

<h1 id="单机事务">八、单机事务</h1>

针对单机事务，这里一些特性借鉴了spring的`@Transactional`注解，事务有很多特性：只读性，隔离性，传播性，我们这里只暴露只读和隔离，其中传播性默认为如果事务不存在，则创建，否则加入到已经存在的事务中。针对事务，这里增加这么个函数（函数名有借鉴）

```java
public void tx(Runnable runnable) {}
public <T> T tx(Supplier<T> supplier) {}
public <T> T tx(Boolean readOnly, Supplier<T> supplier) {}
public void tx(Boolean readOnly, Runnable runnable) {}
public void tx(TxIsolationEnum isolationEnum, Runnable runnable) {}
public <T> T tx(TxIsolationEnum isolationEnum, Supplier<T> supplier) {}
public void tx(TxIsolationEnum isolationEnum, Boolean readOnly, Runnable runnable) {}
public <T> T tx(TxIsolationEnum isolationEnum, Boolean readOnly, Supplier<T> supplier){}
```

| 参数 | 类型 | 详解 |
| --- | --- | --- |
| runnable | Runnable | 无返回值的执行 |
| supplier | Supplier<T> | 有返回值类型T的事务执行 |
| isolationEnum | TxIsolationEnum | 隔离级别枚举：读未提交，读提交，可重复读，序列化 |
| readOnly | Boolean | true：表示该事务是只读事务，false：表示为普通事务 |

<a name="eae7edbd"></a>

<h3 id="事务只读">1.事务只读</h3>

对于事务中有多个读操作的这种，我们可以对事务添加只读，那么为什么添加只读事务，网上说了很多是在一个事务中有多个读操作，为了前后读取的一致性，才添加只读事务的。我的理解不是这样，我觉得mysql事务的默认隔离级别是不可重复，其实就是在一个事务中，前后读取就是保持一致的。那么什么时候添加只读呢，由于在事务中，根据事务中的读写，增加了各种隔离级别，随着级别越来越高，则耗费的数据库资源也越多，很多都是根据写操作设置的，但是如果我们事务中只有读，则没有必要再耗费那么多资源，因此可以开启只读按钮，用于优化事务。<br />**示例：**

```java
/**
* 只读事务
*/
@Test
public void test5(){
  AtomicReference<List<String>> groupList = new AtomicReference<>();
  AtomicReference<List<String>> nameList = new AtomicReference<>();
  neo.tx(true, ()->{
    groupList.set(neo.values(TABLE_NAME, "group"));
    nameList.set(neo.values(TABLE_NAME, "name"));
  });

  // [12, group555]
  show(groupList);
  // [name333]
  show(nameList);
}
```

<a name="8ff349b1"></a>

<h3 id="事务隔离">2.事务隔离</h3>

事务的隔离级别，这里设置了枚举添加了自定义的枚举

```java
@Getter
@AllArgsConstructor
public enum TxIsolationEnum {

    /**
     * 一个常量，指示防止脏读;可以发生不可重复的读取和幻像读取
     */
    TX_R_U(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示可以进行脏读，不可重复读和幻像读
     */
    TX_R_C(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示禁止脏读和不可重复读;可以发生幻像读取
     */
    TX_R_R(Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 一个常量，表示防止脏读，不可重复读和幻像读
     */
    TX_SE(Connection.TRANSACTION_SERIALIZABLE);

    private int level;
}
```

**示例：**

```java
/**
 * 事务的隔离级别
 */
@Test
public void test6(){
  // {age=2, group=kk, id=11, name=name333}
  show(neo.tx(TxIsolationEnum.TX_R_R, ()->{
    neo.update(TABLE_NAME, NeoMap.of("group", "kk"), NeoMap.of("id", 11));
    return neo.one(TABLE_NAME, NeoMap.of("id", 11));
  }));
}
```

<a name="NNiEv"></a>

<h1 id="sql监控">九、sql监控（*）</h1>

该框架是支持对于手写sql的，而手写sql很多时候无法保证sql的性能和规范化，因此这里增加一层监控，用于对sql进行监控和规范化告警。
<a name="1bdde129"></a>
### 1.sql耗时监控
这个是默认开启的，比如每个sql都会打印如下的debug日志

```sql
[Neo-monitor] [耗时: 5毫秒] [sql => select * from neo_table1 where `group` =  ?  limit 1], {params => [ok] }
```
这里系统设置：默认打印debug日志，一旦超过3秒，则打印info日志，超过10秒，打印warn日志，超过1分钟，则打印error日志。
<a name="fb978020"></a>

<h3 id="sql规范化监控">2.sql规范化监控</h3>

默认开启<br />对于sql规范化参考自阿里巴巴的规范化手册中的sql部分和网上部分，然后对其中可以落地的进行的规范进行落地汇总。

| 名字 | 描述 | 告警类别 |
| --- | --- | --- |
| SELECT | 请不要使用select *，尽量使用具体的列 | warn |
| WHERE_NOT | where子句中，尽量不要使用，!=,<>,not,not in, not exists, not like | warn |
| LIKE | where子句中，like 这里的模糊请尽量不要用通配符%开头匹配，即like '%xxx' | warn |
| IN | where子句中有in操作，请谨慎使用，防止in中数据超过1千行 | info |
| UPDATE_NO_WHERE | update 更新语句中，没有where子句 | warn |

除了系统内置的之外，还可以在Neo类中自己添加额外的监控

```java
/**
* 添加自定义规范
*
* @param regex 正则表达式
* @param desc 命中之后的文本打印
* @param logType 日志级别
*/
public void addStandard(String regex, String desc, LogType logType){
  standard.addStandard(regex, desc, logType);
}
```

<a name="586bd541"></a>

<h3 id="sql语句优化监控">3.sql语句优化监控</h3>

sql语句监控是利用的mysql的校验特性，即在每个sql前面添加explain关键字，在sql执行前先执行一次explain（我们的所有sql都是含有占位符的，执行一次之后，就将该sql保存起来，后面每次执行只打印第一次的测量结果），然后根据测量的结果进行打印出来，目前打印告警有三种：

- sql走了全表扫描：warn日志
- sql走了全索引扫描：info日志
- sql其他索引类型：debug日志

explain打印的字段如下，我们只关注type<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/126182/1557157392018-41f10b8e-7f15-4030-9c51-f12e8ed0ff01.png#align=left&display=inline&height=67&name=image.png&originHeight=126&originWidth=1382&size=22297&status=done&width=733)<br />其中主要衡量对象是type，这是一个非常重要的参数，连接类型，常见的有：all , index , range , ref , eq_ref , const , system , null 八个级别。性能从最优到最差的排序：system > const > eq_ref > ref > range > index > all，我们这里约束要至少达到range级别或者最好能达到ref，all进行告警，index进行Info打印，其他的进行debug打印。

| type | 说明 |
| --- | --- |
| all | （full table scan）全表扫描无疑是最差，若是百万千万级数据量，全表扫描会非常 |
| index | （full index scan）全索引文件扫描比all好很多，毕竟从索引树中找数据，比从全表中找数据 |
| range | 只检索给定范围的行，使用索引来匹配行。范围缩小了，当然比全表扫描和全索引文件扫描要快。sql语句中一般会有between，in，>，< 等查询。 |
| ref | 非唯一性索引扫描，本质上也是一种索引访问，返回所有匹配某个单独值的行。比如查询公司所有属于研发团队的同事，匹配的结果是多个并非唯一值。 |
| eq_ref | 唯一性索引扫描，对于每个索引键，表中有一条记录与之匹配。比如查询公司的CEO，匹配的结果只可能是一条记录 |
| const | 表示通过索引一次就可以找到，const用于比较primary key 或者unique索引。因为只匹配一行数据，所以很快，若将主键至于where列表中，MySQL就能将该查询转换为一个常量 |
| system | 表只有一条记录（等于系统表），这是const类型的特列，平时不会出现，了解即可 |

<a name="0oqZs"></a>
# 十、主从（待验证）

<h1 id="主从">十、主从</h1>

<a name="E8ubD"></a>

<h3 id="mysql主从">1.mysql主从</h3>

<a name="3gUjl"></a>

<h1 id="主从">十一、join</h1>

对于表join有如下这么几种类型<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/126182/1557391855029-fcd5edb6-1a0f-486d-b60f-06a5c5c76aa6.png#align=left&display=inline&height=587&name=image.png&originHeight=760&originWidth=966&size=485473&status=done&width=746)

根据以上对于join的处理，这里新增了一个NeoJoiner类用于链式的拼接sql处理。以下为类Neo中的函数

```java
public NeoJoiner join(String leftTableName, String rightTableName){}
public NeoJoiner leftJoin(String leftTableName, String rightTableName){}
public NeoJoiner rightJoin(String leftTableName, String rightTableName){}
public NeoJoiner innerJoin(String leftTableName, String rightTableName){}
public NeoJoiner outerJoin(String leftTableName, String rightTableName){}
public NeoJoiner leftJoinExceptInner(String leftTableName, String rightTableName){}
public NeoJoiner rightJoinExceptInner(String leftTableName, String rightTableName){}
public NeoJoiner outerJoinExceptInner(String leftTableName, String rightTableName){}
```
NeoJoiner中也有跟上面一样的一组函数，且有on函数

```java
public NeoJoiner on(String leftColumnName, String rightColumnName){}
```

此外还有对数据的查询查询处理（还有更多处理）：

```java
public NeoMap one(Columns columns, String tailSql, NeoMap... searchMapList){}
public NeoMap one(Columns columns, NeoMap... searchMapList){}

public List<NeoMap> list(Columns columns, String tailSql, NeoMap... searchMapList){}
public List<NeoMap> list(Columns columns, NeoMap... searchMapList){}

public List<NeoMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters){}
public List<NeoMap> exePage(String sql, NeoPage neoPage, Object... parameters){}

public Integer count(Columns columns, String tailSql, NeoMap... searchMapList){}

public <T> T value(Class<T> tClass, String tableName, String columnName, String tailSql, NeoMap... searchMapList){}
public String value(String tableName, String columnName){}

public List<String> values(String tableName, String columnName){}
```

<a name="7b1e8b49"></a>
### 两表join

<h3 id="两表join">两表join</h3>

基于以上的函数，我们这里简单列举 下简单的实例

```java
/**
 * join 采用的是innerJoin
 *
 * select neo_table1.`id` from neo_table1 
 * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id` 
 * order by sort desc limit 1
 */
@Test
public void joinOneTest1() {
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  String tailSql = "order by sort desc";
  show(neo.join(table1, table2).on("id", "n_id")
       .one(Columns.table(table1, "id"), tailSql));
}

/**
 * join 采用的是innerJoin
 *
 * select neo_table1.`group`
 * from neo_table1
 * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
 */
@Test
public void joinListTest1() {
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  // [{group=group1}, {group=group1}, {group=group2}, {group=group3}]
  show(neo.join(table1, table2).on("id", "n_id")
       .list(Columns.table(table1, "group")));
}

/**
 * 请注意，mysql 不支持 full join
 *
 * select neo_table2.`id`, neo_table1.`group` from neo_table2 
 * outer join neo_table1 on neo_table2.`n_id`=neo_table1.`id` 
 * order by sort desc
 */
@Test
public void outerJoinTest() {
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  String tailSql = "order by sort desc";
  // [group3, group1, group2]
  show(neo.outerJoin(table1, table2).on("id", "n_id")
       .values(table1, "group", tailSql));
}

/**
 * 测试多条件
 * 
 * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`  
 * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`   
 * where neo_table1.`group` = 'group1' and neo_table1.`id` = 11 and neo_table2.`group` = 'group1' order by sort desc limit 1
 */
@Test
public void joinOneTest9() {
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  String tailSql = "order by sort desc";
  // {group1=group3, id=13, name=name1, user_name=user_name1}
  show(neo.join(table1, table2).on("id", "n_id")
       .one(Columns.table(table1, "*"), tailSql, NeoMap.table(table1).cs("group", "group1", "id", 11).and(table2).cs("group", "group1")));
}

/**
 * join的分页查询
 *
 * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
 * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
 * order by sort desc 
 * limit 0, 12
 */
@Test
public void pageTest(){
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  String tailSql = "order by sort desc";
  show(neo.join(table1, table2).on("id", "n_id")
       .page(Columns.table(table1, "*"), tailSql, NeoPage.of(1, 12)));
}
```

<a name="b52f20a1"></a>

<h3 id="多表join">多表join</h3>

其实多表和两表是一样的，只是在on之后又多拼接了一个，举例如下

```java
/**
 * 多表join
 *
 * select neo_table1.`group`, neo_table1.`id`, neo_table2.`name`
 * from neo_table1 right join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
 * right join neo_table3 on neo_table2.`name`=neo_table3.`name`    limit 1
 */
@Test
public void multiJoinTest() {
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  String table3 = "neo_table3";
  show(neo.rightJoin(table1, table2).on("id", "n_id")
       .rightJoin(table2, table3).on("name", "name")
       .one(Columns.table(table1, "id", "group").and(table2, "name")));
}
```
<a name="Mdn2a"></a>

<h3 id="join类型">join类型</h3>

前面简单列举了，下面我们列举下对应的一些join

- join（在这里等价于left join）
- left join
- right join
- inner join
- outer join
- left join except inner
- right join except inner
- outer join except inner（mysql 不支持）

```java
/**
 * left join except inner
 *
 * 该处理的时候会自动增加一个条件，就是设置右表的主键为null，其实就是取的一个左表减去和右表公共的部分
 *
 * select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
 * from neo_table1 left join neo_table2 on neo_table1.`name`=neo_table2.`name`
 * where (neo_table2.id is null)  limit 1
 */
@Test
public void leftJoinExceptInnerTest(){
  String table1 = "neo_table1";
  String table2 = "neo_table2";
  show(neo.leftJoinExceptInner(table1, table2).on("name", "name")
       .one(Columns.table(table1, "id", "name", "group").and(table2, "id", "name", "group"))
      );
}
```

<a name="pWtK7"></a>

<h1 id="实体代码生成器">实体代码生成器（*）</h1>

这里借鉴mybatis的实体生成器想法，根据jdbc中数据库字段和java类的映射，来生成对应的实体，我们这里有别于mybatis，对一些枚举类型做了特殊处理，对于公共的一些枚举类型，这里及进行抽离了出来。我们首先看下怎么生成实体。

```java
@Test
public void test1(){
  EntityCodeGen codeGen = new EntityCodeGen()
    // 数据库信息
		.setDb("neo_test", "neo@Test123", "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
    // 设置项目路径
    .setProjectPath("/Users/xxx/xxx/Neo")
    // 设置实体生成的包路径
    .setEntityPath("com.simon.neo.entity")
    // 设置表前缀过滤
    .setPreFix("neo_")
    // 设置要排除的表
    //.setExcludes("xx_test")
    // 设置只要的表
    .setIncludes("neo_table3", "neo_table4")
    // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
    .setFieldNamingChg(NamingChg.UNDERLINE);

  // 代码生成
  codeGen.generate();
}
```

<a name="c2762ca4"></a>

<h1 id="生成实体">1.生成实体</h1>

根据上面的配置即可在对应的位置生成对应的实体结构。比如包含所有字段的表如下

```sql
CREATE TABLE `xx_test5` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女',
  `biginta` bigint(20) NOT NULL,
  `binarya` binary(1) NOT NULL,
  `bit2` bit(1) NOT NULL,
  `blob2` blob NOT NULL,
  `boolean2` tinyint(1) NOT NULL,
  `char1` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `datetime1` datetime NOT NULL,
  `date2` date NOT NULL,
  `decimal1` decimal(10,0) NOT NULL,
  `double1` double NOT NULL,
  `enum1` enum('a','b') COLLATE utf8_unicode_ci NOT NULL,
  `float1` float NOT NULL,
  `geometry` geometry NOT NULL,
  `int2` int(11) NOT NULL,
  `linestring` linestring NOT NULL,
  `longblob` longblob NOT NULL,
  `longtext` longtext COLLATE utf8_unicode_ci NOT NULL,
  `medinumblob` mediumblob NOT NULL,
  `medinumint` mediumint(9) NOT NULL,
  `mediumtext` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `multilinestring` multilinestring NOT NULL,
  `multipoint` multipoint NOT NULL,
  `mutipolygon` multipolygon NOT NULL,
  `point` point NOT NULL,
  `polygon` polygon NOT NULL,
  `smallint` smallint(6) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  `time` time NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tinyblob` tinyblob NOT NULL,
  `tinyint` tinyint(4) NOT NULL,
  `tinytext` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `text1` text COLLATE utf8_unicode_ci NOT NULL,
  `text1123` text COLLATE utf8_unicode_ci NOT NULL,
  `time1` time NOT NULL,
  `timestamp1` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `tinyblob1` tinyblob NOT NULL,
  `tinyint1` tinyint(4) NOT NULL,
  `tinytext1` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `year2` year(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='配置项';
```

生成的结构

```java
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 配置项
 * @author robot
 */
@Data
public class Test5DO {

    private Long id;

    /**
     * 数据来源组，外键关联lk_config_group
     */
    private String group;

    /**
     * 任务name
     */
    private String name;

    /**
     * 修改人名字
     */
    private String userName;

    /**
     * 性别：Y=男；N=女
     */
    private String gander;
    private Long biginta;
    private byte[] binarya;
    private Boolean bit2;
    private byte[] blob2;
    private Boolean boolean2;
    private String char1;
    private Timestamp datetime1;
    private Date date2;
    private BigDecimal decimal1;
    private Double double1;
    private String enum1;
    private Float float1;
    private byte[] geometry;
    private Integer int2;
    private byte[] linestring;
    private byte[] longblob;
    private String longtext;
    private byte[] medinumblob;
    private Integer medinumint;
    private String mediumtext;
    private byte[] multilinestring;
    private byte[] multipoint;
    private byte[] mutipolygon;
    private byte[] point;
    private byte[] polygon;
    private Integer smallint;
    private String text;
    private Time time;
    private Timestamp timestamp;
    private byte[] tinyblob;
    private Integer tinyint;
    private String tinytext;
    private String text1;
    private String text1123;
    private Time time1;
    private Timestamp timestamp1;
    private byte[] tinyblob1;
    private Integer tinyint1;
    private String tinytext1;
    private Date year2;
}
```

<a name="186b049c"></a>

<h2 id="抽离公共枚举">2.抽离公共枚举</h2>

对于表中有枚举类型的话，则会先看是否已经有对应的枚举类了，如果有，则不生成，如果有同名的枚举类，但是枚举类型又不同，则会生成内部的枚举类。<br />表1：（关注其中的枚举类型）

```sql
CREATE TABLE `neo_table3` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `n_id` int(11) unsigned NOT NULL,
  `sort` int(11) NOT NULL,
  `enum1` enum('A','B') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型：A=成功；B=失败',
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```
表2：（关注其中的枚举类型）

```sql
CREATE TABLE `neo_table4` (
  `id` int(11) unsigned NOT NULL,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `n_id` int(11) unsigned NOT NULL,
  `sort` int(11) NOT NULL,
  `enum1` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型：Y=成功；N=失败',
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```
两个表都是有对应的枚举类型，枚举类型名字相同，则在进行生成的时候，两个一起的话，则先生成的对应的枚举会是公共的，而后者是内部的，如下：

```java
/**
 * 类型
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum Enum1 {

    /**
     * 失败
     */
    B("B"),
    /**
     * 成功
     */
    A("A"),
;

    private String value;
}
```

```java
/**
 * @author robot
 */
@Data
public class Table4DO {

    private Long id;

    /**
     * 数据来源组，外键关联lk_config_group
     */
    private String group;

    /**
     * 任务name
     */
    private String name;

    /**
     * 修改人名字
     */
    private String userName;
    private Integer age;
    private Long nId;
    private Integer sort;

    /**
     * 类型：Y=成功；N=失败
     */
    private String enum1;

    /**
    * 类型
    */
    @Getter
    @AllArgsConstructor
    public enum Enum1 {
        /**
        * 成功
        */
        Y("Y"),
        /**
        * 失败
        */
        N("N"),
        ;
        private String value;
    }
}
```

**注意：**<br />其中对于枚举类型的注释，这里采用解析字段的remark字段，格式为：
> 描述:A=xx;B=xxx

其中描述和后面分隔采用冒号，中文的":"和英文"："的均可，类型之间用分号"；"和";"都可以
<a name="IE1is"></a>

<h1 id="sql特殊处理">十三、sql特殊处理（*）</h1>

对于sql里面增加了一些特殊处理，模糊搜索和大小比较。
<a name="e3769034"></a>

<h3 id="sql模糊查询">1.sql模糊查询</h3>

在值中前面添加"like "即可，比如

```java
/**
 * 查询大小匹配的查询
 * 条件通过NeoMap设置
 * 相当于：select `group`, `name` from neo_table1 where `group` like 'group%'
 */
@Test
@SneakyThrows
public void testList10(){
  // select `group`, `name` from neo_table1 where `group` like 'group%'
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "like group")));
}
```

<a name="eb7b789a"></a>

<h3 id="sql大小比较查询">2.sql大小比较查询</h3>

在值中前面添加比较符号即可，比如

```java
/**
 * 查询大小匹配的查询
 * 条件通过NeoMap设置
 * 相当于：select `group`,`name` from neo_table1 where `name` < 'name' limit 1
 */
@Test
@SneakyThrows
public void testList9(){
  // select `group`, `name` from neo_table1 where `name` < ? ], {params => [name]
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "< name")));
  // select `group`, `name` from neo_table1 where `name` < ? ], {params => ['name']
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "< 'name'")));
  // select `group`, `name` from neo_table1 where `name` <= ? ], {params => [name']
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "<= name'")));
  // select `group`, `name` from neo_table1 where `name` > ? ], {params => ['name']
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", "> 'name'")));
  // select `group`, `name` from neo_table1 where `name` >= ? ], {params => ['name']
  show(neo.list(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("name", ">= 'name'")));
}
```

<a name="MwiYy"></a>

<h1 id="分布式">十四、分布式</h1>

<a name="ec6732a4"></a>

<h3 id="全局id">1.全局id</h3>

对于分布式的全局id，这里并没有采用常见的雪花算法，而是在当前版本用到了全部的64big位，数据持久化部分是基于数据库，内存部分采用双buffer的方式进行数据刷新。对外仅提供两个参数进行buffer刷新速度和buffer大小调整。<br />默认情况下是不启用全局id的，如果要启用全局，则需要先进行开启，并设置buffer刷新比率和大小。开启后，则会在对应的库中创建一个全局id表：neo_id_generator。

```java
/**
 * 开启全局id生成器，默认buffer大小是10000，刷新比率是0.2
 */
public void openUidGenerator(){}
public void openUidGenerator(Integer stepSize, Float refreshRatio){}
```
| type | 说明 |
| --- | --- |
| stepSize | 双buffer，其中每一个内存buffer的大小 |
| refreshRatio | 刷新二级buffer的比率，在第一级buffer达到这个比率之后，就派一个线程异步获取数据，填充第二buffer |

<a name="flupG"></a>

<h4 id="表结构">表结构</h4>

```sql
CREATE TABLE `neo_id_generator` (
  `id` int(11) NOT NULL,
  `uuid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

<a name="IN6DT"></a>

<h4 id="用法">用法</h4>

直接调用如下diamante即可获取分布式全局唯一id

```java
public Long getUid() {}
```

<a name="YaeCo"></a>

<h3 id="用法">2.分布式锁（待开发）</h3>

<a name="oE9mJ"></a>

<h1 id="动态分库分表">十五、动态分库分表（待开发）</h1>

<a name="d2027c8d"></a>
<h3 id="水平分表">1.水平分表</h3>

<a name="5e814c20"></a>

<h3 id="垂直分表">2.垂直分表</h3>

<a name="ebe15985"></a>

<h3 id="分库">3.分库</h3>

<a name="35FtJ"></a>

<h1 id="分布式事务">十六、分布式事务（待开发）</h1>

<a name="F4ZX5"></a>

<h1 id="多数据源">十七、多数据源（待开发）</h1>

<a name="1.mysql"></a>

<h3 id="分库">1.mysql</h3>

<a name="2.sqlLite"></a>

<h3 id="分库">2.sqlLite</h3>

<a name="3.PostGresql"></a>

<h3 id="分库">3.PostGresql</h3>



