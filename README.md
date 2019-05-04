# Neo：Orm框架

Neo是一个基于JDBC开发的，采用Hikaricp作为连接池的Orm框架。开发源头，源于几个原因，一个是之前几个公司采用的都是各自单独开发的Orm框架，这些框架不是开源，如果想自己使用，很多时候不方便，也不合适。另外一个是mybatis确实感觉不是很好用，把很多简单的东西设计的很复杂。最后是由于自己有很多想法，比如sql的规范落入到框架中、sql耗时统计和sql优化监控等等很多特性，且在之前接触的一些Orm框架中都没有。因此就有想法设计一个符合自己想法的Orm框架，下面的一些设计和各种特性有有借鉴之前接触到的一些优秀思想，也有在秉承着大道至简的原则进行的设计，框架刚起步，希望有兴趣的同学，一起添砖加瓦，共同成长。
<a name="997c7a5d"></a>
# 功能
<a name="e806a413"></a>
## 一、数据库连接
<a name="fd782c08"></a>
## 二、基本功能
<a name="59ee2101"></a>
### 1.增加
<a name="bc040c4c"></a>
#### a.自增属性支持
<a name="7b286754"></a>
### 2.删除
<a name="88750289"></a>
### 3.修改
<a name="84bfa9a0"></a>
### 4.查询
<a name="7f660bc9"></a>
#### a.单行数据查询
<a name="5570ea7c"></a>
#### b.多行数据查询
<a name="b677ee25"></a>
#### c.分页数据查询
<a name="2a235952"></a>
#### d.个数的查询
<a name="ea95b824"></a>
#### e.单个数据查询
<a name="2139917e"></a>
#### f.单数据列表查询
<a name="20483a0d"></a>
#### g.直接执行sql
<a name="6c7762cf"></a>
## 三、结构信息
<a name="f8ada07a"></a>
### 1.表信息
<a name="47061b39"></a>
### 2.列信息
<a name="e46328b9"></a>
### 3.索引信息
<a name="c21b6bee"></a>
### 4.表创建的sql
<a name="6425b7e7"></a>
## 四、批量功能
<a name="151c1be6"></a>
### 1.批量插入
<a name="c0d24f84"></a>
### 2.批量更新
<a name="a07fb1fd"></a>
## 五、NeoMap类
<a name="944a6e56"></a>
### 1.NeoMap和JavaBean转换
<a name="c624aec5"></a>
### 2.其他功能
<a name="3a8096d7"></a>
#### 1.NeoMap获取固定列
<a name="7dac5747"></a>
#### 2.NeoMap列添加前缀
<a name="1d39a657"></a>
#### 3.NeoMap列转换
<a name="7e508a87"></a>
## 六、命名转换
<a name="22b2d12e"></a>
## 七、单机事务
<a name="eae7edbd"></a>
### 1.事务只读性
<a name="8ff349b1"></a>
### 2.事务隔离性
<a name="f0a53052"></a>
## 八、sql监控
<a name="1bdde129"></a>
### 1.sql耗时监控
<a name="fb978020"></a>
### 2.sql规范化落地监控
<a name="586bd541"></a>
### 3.sql优化解析监控
<a name="7cdb75b1"></a>
## 九、主从
<a name="d2b13881"></a>
## 十、join
<a name="7b1e8b49"></a>
### 两表join
<a name="b52f20a1"></a>
### 多表join
<a name="1.join"></a>
#### 1.join
<a name="7a05c43c"></a>
#### 2.left join
<a name="1cf100b3"></a>
#### 3.right join
<a name="7640060b"></a>
#### 4.inner join
<a name="e08c6d2c"></a>
#### 5.outer join
<a name="db39b484"></a>
#### 6.left join except inner
<a name="01de8649"></a>
#### 7.right join except inner
<a name="c65a04a1"></a>
#### 8.outer join except inner
<a name="d1e50d05"></a>
## 十一、实体代码生成器
<a name="c2762ca4"></a>
### 1.基本实体
<a name="186b049c"></a>
### 2.枚举类型
<a name="e0a6349e"></a>
## 十二、sql特殊处理
<a name="e3769034"></a>
### 1.sql 模糊查询
<a name="eb7b789a"></a>
### 2.sql 大小比较查询
<a name="7e1cfb45"></a>
## 十三、分布式
<a name="ec6732a4"></a>
### 1.全局id
### 2.分布式锁
<a name="6f56500e"></a>
## 十四、动态分库分表   
<a name="d2027c8d"></a>
### 1.水平分表
<a name="5e814c20"></a>
### 2.垂直分表
<a name="ebe15985"></a>
### 3.分库
<a name="2bee72e3"></a>
## 十五、分布式事务
<a name="dae0266e"></a>
## 十六、多数据源
<a name="1.mysql"></a>
### 1.mysql
<a name="2.sqlLite"></a>
### 2.sqlLite
<a name="3.PostGresql"></a>
### 3.PostGresql


