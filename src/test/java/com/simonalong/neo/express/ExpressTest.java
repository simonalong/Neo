package com.simonalong.neo.express;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoConstant;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.devide.DevideMultiNeo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.simonalong.neo.express.BaseOperate.*;

/**
 * @author shizi
 * @since 2020/8/29 11:13 上午
 */
public class ExpressTest extends NeoBaseTest {

    /**
     * 测试and所有形式
     */
    @Test
    public void antTest() {
        Express express;
        String sql;


        //--------------------- 采用 and 函数的（函数and不带括号） ---------------------
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        express = new Express().andEm("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        express = new Express().and("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where `name` = ?";
        express = new Express().andEm("name", 1);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(1), express.toValue());

        sql = " where (`name` = ?)";
        express = new Express().and("name", 1);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(1), express.toValue());

        sql = " where (`name` = ?) and (`group` = ?) and (`age` = ?)";
        express = new Express().and("name", 1).and("group", "test").and("age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 数据为空，则字段默认隐藏 ---------------------
        sql = " where `name` = ?";
        express = new Express().andEm("name", 1, "group", null);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(1), express.toValue());

        sql = " where `name` = ?";
        express = new Express().andEm("name", 1, "group", "");
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(1), express.toValue());

        sql = "";
        express = new Express().andEm("name", null);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());


        //--------------------- 采用 em 函数的 ---------------------
        sql = " where (`name` = ?) and (`group` = ?) and (`age` = ?)";
        express = new Express().and("name", 1).and("group", "test").and("age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ?) and (`group` = ? and `age` = ?)";
        express = new Express().and("name", 1).and("group", "test", "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 采用 And 类的（类And生成的带括号） ---------------------
        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        express = new Express(And("name", 1, "group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ?) and ((`group` = ?) and (`age` = ?))";
        express = new Express().and("name", 1).and(And("group", "test"), And("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where `name` = ? and (`group` = ? and `age` = ?)";
        express = new Express("name", 1, And("group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 采用 NeoMap参数(注意NeoMap的加入顺序没有保障) ---------------------
        NeoMap searchMap = NeoMap.of();
        // 设置有序，否则没有顺序性
        searchMap.openSorted();
        searchMap.put("name", 1);
        searchMap.put("group", "test");
        searchMap.put("age", 3);

        sql = " where `name` = ? and `group` = ? and `age` = ?";
        express = new Express(searchMap);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where `name` = ? and `group` = ? and `age` = ?";
        express = new Express().andEm(searchMap);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        express = new Express().and(searchMap);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 采用 NeoMap多参数 ---------------------
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        express = new Express(NeoMap.of("name", 1), NeoMap.of("group", "test"), NeoMap.of("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        // 请开启openSort，否则对应的值可能是无序
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        NeoMap s1 = NeoMap.of("name", 1).openSorted();
        NeoMap s2 = NeoMap.of("group", "test", "age", 3).openSorted();
        express = new Express(s1, s2);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        //--------------------- 采用 NeoQueue ---------------------
        NeoQueue<Operate> queue = NeoQueue.of();
        queue.addLast(Or("name", 1, "age", 1));
        queue.addLast(AndEm("group", 1));
        sql = " where (`name` = ? or `age` = ?) and `group` = ?";
        express = new Express(queue);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, 1, 1), express.toValue());
    }

    /**
     * 测试 or
     * where `name` = ? or `group` = ? or `age` = ?
     */
    @Test
    public void orTest() {
        Express express;
        String sql;

        //--------------------- 采用 or 函数的（函数or生成的不带括号） ---------------------
        sql = " where (`name` = ? or `group` = ? or `age` = ?)";
        express = new Express().or("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ?) or (`group` = ?) or (`age` = ?)";
        express = new Express().or("name", 1).or("group", "test").or("age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 采用 Or 类的（类Or生成的sql是带括号的） ---------------------
        sql = " where `name` = ? and (`group` = ?)";
        express = new Express("name", 1, Or("group", "test"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test"), express.toValue());

        sql = " where `name` = ? and (`group` = ? or `age` = ?)";
        express = new Express("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());


        //--------------------- 采用 NeoMap参数 ---------------------
        NeoMap searchMap = NeoMap.of().openSorted();
        searchMap.put("name", 1);
        searchMap.put("group", "test");
        searchMap.put("age", 3);

        // 由于NeoMap的key是不重复的，这里就没有对or进行支持，所以请采用NeoQueue做更灵活的配置
        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        express = new Express().or(searchMap);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());
    }

    /**
     * 测试 or 和 and 结合的部分
     * where `name` = ? and (`group` = ? or `age` = ?)
     */
    @Test
    public void andOrTest() {
        Express express;
        String sql;

        sql = " where (`name` = ? and (`group` = ? or `age` = ?))";
        express = new Express().and("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        // 对于内部已经有的Or会在遇到外部的and时候会处理掉
        sql = " where `name` = ? and ((`group` = ? or `age` = ?))";
        express = new Express().andEm("name", 1).and(Or("group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where (`name` = ? and ((`group` = ?) and (`age` = ?)))";
        express = new Express().and("name", 1, And(Or("group", "test"), Or("age", 3)));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());

        sql = " where `name` = ? and ((`group` = ? or `age` = ?))";
        express = new Express().andEm("name", 1).and(Or("group", "test", "age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), express.toValue());
    }


    /**
     * 关系操作符：{@code 等于（=）、大于（>）、小于（<）、大于等于（>=）、小于等于（<=）、不等于（!=）}
     * 也是默认的操作符
     */
    @Test
    public void relationTest() {
        Express express;
        String sql;

        // 等于
        sql = " where (`name` = ? and `age` = ?)";
        express = new Express().and(Equal("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());

        // 不等于
        sql = " where (`name` != ? and `age` = ?)";
        express = new Express().and(NotEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());

        // 大于
        sql = " where (`name` > ? and `age` = ?)";
        express = new Express().and(GreaterThan("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());

        // 大于等于
        sql = " where (`name` >= ? and `age` = ?)";
        express = new Express().and(GreaterEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());

        // 小于
        sql = " where (`name` < ? and `age` = ?)";
        express = new Express().and(LessThan("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());

        // 小于等于
        sql = " where (`name` <= ? and `age` = ?)";
        express = new Express().and(LessEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), express.toValue());
    }

    /**
     * 其他符号测试：like
     * where `name` like '%chou' and `age` = ?
     */
    @Test
    public void likeOrNotTest() {
        Express express;
        String sql;

        sql = " where (`name` like '%chou' and `age` = ?)";
        express = new Express().and(Like("name", "%chou"), "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(3), express.toValue());

        sql = " where (`name` not like '%chou' and `age` = ?)";
        express= new Express().and(NotLike("name", "%chou"), "age", 3);
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(3), express.toValue());
    }

    /**
     * 其他符号测试：in
     * where id in (12,32,43,43)
     */
    @Test
    public void inOrNotTest() {
        Express express;
        String sql;

        sql = " where (`id` in ('12', '13', '14'))";
        List<Long> dataList = new ArrayList<>();
        dataList.add(12L);
        dataList.add(13L);
        dataList.add(14L);
        express = new Express().and(In("id", dataList));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = " where (`id` not in ('12', '13', '14'))";
        dataList = new ArrayList<>();
        dataList.add(12L);
        dataList.add(13L);
        dataList.add(14L);
        express = new Express().and(NotIn("id", dataList));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 其他符号测试：is null
     * where `name` is null
     */
    @Test
    public void isNullOrNotTest() {
        Express express;
        String sql;

        sql = " where (`name` is null)";
        express = new Express().and(IsNull("name"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = " where (`name` is not null)";
        express = new Express().and(IsNotNull("name"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = "";
        express = new Express().and(IsNotNull(null));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 其他符号测试：group by
     * where `name` = 'test' group by 'group'
     */
    @Test
    public void groupByTest() {
        Express express;
        String sql;

        sql = " where (`name` = ?) group by `group`";
        express = new Express().and("name", 12).append(GroupBy("group"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(12), express.toValue());

        // 测试group by 是否可以有where
        sql = " group by `group`";
        express = new Express().and("name", null).append(GroupBy("group"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 其他符号测试：order by
     */
    @Test
    public void orderByTest() {
        Express express;
        String sql;

        // 默认为升序
        sql = " order by `create_time`";
        express = new Express().append(OrderBy("create_time"));
        Assert.assertEquals(sql, express.toSql());

        // 设置升序降序字段
        sql = " order by `create_time` desc";
        express = new Express().append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, express.toSql());

        // order by 多个字段
        sql = " order by `create_time` desc, `id` asc, `group` desc";
        express = new Express().append(OrderBy("create_time", "desc", "id", "asc", "group", "desc"));
        Assert.assertEquals(sql, express.toSql());

        // order by 多个字段
        sql = " order by `create_time`, `id`, `group` desc";
        express = new Express().append(OrderBy("create_time", "id", "group", "desc"));
        Assert.assertEquals(sql, express.toSql());

        // order by 多个字段
        sql = " order by `create_time`, `id` desc, `group` desc";
        express = new Express().append(OrderBy("create_time", "id", "desc", "group", "desc"));
        Assert.assertEquals(sql, express.toSql());
    }

    @Test
    public void teststetst(){
        Express express;
        express = new Express().append(OrderBy("create_time", "id", "desc", "group", "asc"));
        show(express.getFirstOperateStr(NeoConstant.ORDER_BY));
        show(DevideMultiNeo.getColumnAndSortList(express));

        NeoMap dataMap = NeoMap.of();
        dataMap.put("order by", "id, name desc, group asc");
        show(DevideMultiNeo.getColumnAndSortList(dataMap));
    }

    /**
     * 其他符号测试：order by desc
     */
    @Test
    public void orderByDescTest1() {
        Express express;
        String sql;

        // 默认为升序
        sql = " where (`name` = ?) order by `create_time` asc";
        express = new Express().and("name", "test").append(OrderBy("create_time", "asc"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList("test"), express.toValue());

        sql = " where (`name` = ?) order by `create_time` desc";
        express = new Express().and("name", "test").append(OrderByDesc("create_time"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList("test"), express.toValue());

        sql = " where (`name` = ?) order by `create_time` desc";
        express = new Express().and("name", "test").append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList("test"), express.toValue());

        // 测试跟where关系
        sql = " order by `create_time` desc";
        express = new Express().and("name", null).append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 符号测试：exists
     */
    @Test
    public void existsOrNotTest() {
        Express express;
        String sql;

        sql = " where exists (select id from xxx)";
        express = new Express().append(Exists("select id from xxx"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = " where not exists (select id from xxx)";
        express = new Express().append(NotExists("select id from xxx"));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 符号测试：between and
     */
    @Test
    public void betweenOrNotTest() {
        Express express;
        String sql;

        sql = " where `age` between ? and ?";
        express = new Express().append(BetweenAnd("age", 12, 60));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(12, 60), express.toValue());

        sql = "";
        express = new Express().append(BetweenAnd("age", null, 60));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = "";
        express = new Express().append(BetweenAnd("age", null, null));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = " where `age` not between ? and ?";
        express = new Express().append(NotBetweenAnd("age", 12, 60));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Arrays.asList(12, 60), express.toValue());

        sql = "";
        express = new Express().append(NotBetweenAnd("age", null, 60));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());

        sql = "";
        express = new Express().append(NotBetweenAnd("age", null, null));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    /**
     * 符号测试：Em
     */
    @Test
    public void emTest() {
        Express express;
        String sql;

        sql = " match(`break_law_detail`) against (?)";
        express = new Express().append(Em("match(`break_law_detail`) against (?)", 12));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.singletonList(12), express.toValue());

        sql = "";
        express = new Express().append(Em("match(`break_law_detail`) against (?)", null));
        Assert.assertEquals(sql, express.toSql());
        Assert.assertEquals(Collections.emptyList(), express.toValue());
    }

    @Test
    public void appendTest() {
        Express express;
        String sql;

        // 普通拼接
        NeoMap dataMap = NeoMap.of();
        dataMap.put("a", 1);
        dataMap.put("b", 2);
        dataMap.put("c", 3);

        sql = " where (`a` = ? and `b` = ?) for update";
        express = new Express().and(dataMap.assign("a", "b")).append(" for update");
        Assert.assertEquals(sql, express.toSql());
        dataMap.clear();

        // 测试为空的拼接
        dataMap.setSupportValueNull(true);
        dataMap.put("a", null);
        dataMap.put("b", null);
        dataMap.put("c", 3);

        sql = " for update";
        express = new Express().and(dataMap.assign("a", "b")).append(" for update");
        Assert.assertEquals(sql, express.toSql());
    }
}
