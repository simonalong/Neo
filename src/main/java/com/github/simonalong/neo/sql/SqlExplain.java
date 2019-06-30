package com.github.simonalong.neo.sql;

import com.github.simonalong.neo.Neo;
import com.github.simonalong.neo.NeoMap;
import com.github.simonalong.neo.util.EncryptUtil;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 该类主要用于对对应的sql进行解析，用于分析使用
 * @author zhouzhenyong
 * @since 2019/3/22 下午8:26
 */
@Slf4j
public final class SqlExplain {

    private static final String PRE_LOG = "[Neo-explain]";
    private static final String ALL = "all";
    private static final String INDEX = "index";
    /**
     * sql的explain的类型
     */
    private Map<String, Explain> explainMap = new ConcurrentHashMap<>(64, 0.9f);

    private static SqlExplain instance = new SqlExplain();
    private SqlExplain(){}

    public static SqlExplain getInstance(){
        return instance;
    }

    /**
     * 专门用于对sql进行explain解析
     * <p>
     * @param neo Neo 对象
     * @param sql 待解析的sql
     * @param parameters sql的参数用于sql的执行用
     */
    public void explain(Neo neo, String sql, List<Object> parameters){
        Explain explain = explainMap.computeIfAbsent(EncryptUtil.SHA256(sql), k-> exeSqlExplain(neo, sql, parameters));

        if (null != explain) {
            String type = explain.getType().toLowerCase();
            if (ALL.equals(type)) {
                log.warn(PRE_LOG + " [sql走了全表扫描] [sql => " + sql + " ]");
            } else if (INDEX.equals(type)) {
                log.info(PRE_LOG + " [sql走了全索引扫描] [sql => " + sql + " ]");
            } else {
                log.debug(PRE_LOG + " [sql索引类型：" + type + "] [sql => " + sql + " ]");
            }
        }
    }

    /**
     * 执行并获取sql的explain
     * <p>
     * @return 返回sql的explain中的解析数据
     */
    private Explain exeSqlExplain(Neo neo, String sql, List<Object> parameters){
        Boolean oldMonitorFlag = neo.getMonitorFlag();
        Boolean standardFlag = neo.getStandardFlag();

        // 这里调用sql禁止监控和规范
        neo.setMonitorFlag(false);
        neo.setStandardFlag(false);

        Explain explain = null;
        List<List<NeoMap>> explainList = neo.execute("explain " + sql, parameters.toArray());
        if (null != explainList && !explainList.isEmpty()) {
            List<NeoMap> mExplain = explainList.get(0);
            if (null != mExplain && !mExplain.isEmpty()) {
                explain = Explain.parse(mExplain.get(0));
            }
        }

        // 恢复旧的标示
        neo.setMonitorFlag(oldMonitorFlag);
        neo.setStandardFlag(standardFlag);
        return explain;
    }

    /**
     * sql执行评测类
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    static class Explain{

        /**
         * select 查询的序列号，包含一组可以重复的数字，表示查询中执行sql语句的顺序。一般有三种情况：
         *  第一种：id全部相同，sql的执行顺序是由上至下；
         *  第二种：id全部不同，sql的执行顺序是根据id大的优先执行；
         *  第三种：id既存在相同，又存在不同的。先根据id大的优先执行，再根据相同id从上至下的执行。
         */
        private Integer id;
        /**
         * select 查询的类型，主要是用于区别普通查询，联合查询，嵌套的复杂查询
         *  SIMPLE：简单的select 查询，查询中不包含子查询或者union
         *  PRIMARY：查询中若包含任何复杂的子查询，最外层查询则被标记为primary
         *  SUBQUERY：在select或where 列表中包含了子查询
         *  DERIVED：在from列表中包含的子查询被标记为derived（衍生）MySQL会递归执行这些子查询，把结果放在临时表里。
         *  UNION：若第二个select出现在union之后，则被标记为union，若union包含在from子句的子查询中，外层select将被标记为：derived
         *  UNION RESULT：从union表获取结果的select
         */
        private String selectType;
        /**
         * 表名
         */
        private String table;
        /**
         * 表所使用的分区，如果要统计十年公司订单的金额，可以把数据分为十个区，每一年代表一个区。这样可以大大的提高查询效率。
         */
        private String partitions;
        /**
         * 这是一个非常重要的参数，连接类型，常见的有：all , index , range , ref , eq_ref , const , system , null 八个级别。
         *  性能从最优到最差的排序：system > const > eq_ref > ref > range > index > all
         *  我们这里约束要至少达到range级别或者最好能达到ref，all进行告警，index进行Info打印，其他的进行debug打印
         *  all：（full table scan）全表扫描无疑是最差，若是百万千万级数据量，全表扫描会非常慢。
         *  index：（full index scan）全索引文件扫描比all好很多，毕竟从索引树中找数据，比从全表中找数据要快。
         *  range：只检索给定范围的行，使用索引来匹配行。范围缩小了，当然比全表扫描和全索引文件扫描要快。sql语句中一般会有between，in，>，< 等查询。
         *  ref：非唯一性索引扫描，本质上也是一种索引访问，返回所有匹配某个单独值的行。比如查询公司所有属于研发团队的同事，匹配的结果是多个并非唯一值。
         *  eq_ref：唯一性索引扫描，对于每个索引键，表中有一条记录与之匹配。比如查询公司的CEO，匹配的结果只可能是一条记录，
         *  const：表示通过索引一次就可以找到，const用于比较primary key 或者unique索引。因为只匹配一行数据，所以很快，若将主键至于where列表中，MySQL就能将该查询转换为一个常量。
         *  system：表只有一条记录（等于系统表），这是const类型的特列，平时不会出现，了解即可
         */
        private String type;
        /**
         * 显示查询语句可能用到的索引(一个或多个或为null)，不一定被查询实际使用。仅供参考使用。
         */
        private String possibleKeys;
        /**
         * 显示查询语句实际使用的索引。若为null，则表示没有使用索引。
         */
        private String key;
        /**
         * 显示索引中使用的字节数，可通过key_len计算查询中使用的索引长度。在不损失精确性的情况下索引长度越短越好。
         * key_len 显示的值为索引字段的最可能长度，并非实际使用长度，即key_len是根据表定义计算而得，并不是通过表内检索出的。
         */
        private String keyLen;
        /**
         * 显示索引的哪一列或常量被用于查找索引列上的值。
         */
        private String ref;
        /**
         * 根据表统计信息及索引选用情况，大致估算出找到所需的记录所需要读取的行数，值越大越不好。
         */
        private BigInteger rows;
        /**
         * 一个百分比的值，和rows 列的值一起使用，可以估计出查询执行计划(QEP)中的前一个表的结果集，从而确定join操作的循环次数。小表驱动大表，减轻连接的次数。
         */
        private Double filtered;
        /**
         * Using filesort： 说明MySQL会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取。MySQL中无法利用索引完成的排序操作称为“文件排序” 。出现这个就要立刻优化sql。
         *  Using temporary： 使用了临时表保存中间结果，MySQL在对查询结果排序时使用临时表。常见于排序 order by 和 分组查询 group by。 出现这个更要立刻优化sql。
         *  Using index： 表示相应的select 操作中使用了覆盖索引（Covering index），避免访问了表的数据行，效果不错！如果同时出现Using where，表明索引被用来执行索引键值的查找。
         *  如果没有同时出现Using where，表示索引用来读取数据而非执行查找动作。覆盖索引（Covering Index） ：也叫索引覆盖，就是select 的数据列只用从索引中就能够取得，不必读取数据行，
         *  MySQL可以利用索引返回select 列表中的字段，而不必根据索引再次读取数据文件。
         *  Using index condition： 在5.6版本后加入的新特性，优化器会在索引存在的情况下，通过符合RANGE范围的条数 和 总数的比例来选择是使用索引还是进行全表遍历。
         *  Using where： 表明使用了where 过滤
         *  Using join buffer： 表明使用了连接缓存
         *  impossible where： where 语句的值总是false，不可用，不能用来获取任何元素
         *  distinct： 优化distinct操作，在找到第一匹配的元组后即停止找同样值的动作。
         */
        private String extra;

        private Explain(){}

        static Explain parse(NeoMap neoMap) {
            if (NeoMap.isEmpty(neoMap)) {
                return null;
            }
            return new Explain()
                .setId((Integer)(neoMap.get("neoMap")))
                .setSelectType((String) neoMap.get("select_type"))
                .setTable((String) neoMap.get("table"))
                .setPartitions((String) neoMap.get("partitions"))
                .setType((String) neoMap.get("type"))
                .setPossibleKeys((String) neoMap.get("possible_keys"))
                .setKey((String) neoMap.get("key"))
                .setKeyLen((String) neoMap.get("key_len"))
                .setRef((String) neoMap.get("ref"))
                .setRows((BigInteger) neoMap.get("rows"))
                .setFiltered((Double) neoMap.get("filtered"))
                .setExtra((String) neoMap.get("extra"));
        }
    }
}
