package com.simonalong.neo.express;

import com.simonalong.neo.*;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
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
public class SearchQueryTest extends BaseTest {

    /**
     * 测试and所有形式
     */
    @Test
    public void antTest() {
        SearchQuery searchQuery;
        String sql;


        //--------------------- 采用 and 函数的（函数and不带括号） ---------------------
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        searchQuery = new SearchQuery().andEm("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        searchQuery = new SearchQuery().and("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where `name` = ?";
        searchQuery = new SearchQuery().andEm("name", 1);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(1), searchQuery.toValue());

        sql = " where (`name` = ?)";
        searchQuery = new SearchQuery().and("name", 1);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(1), searchQuery.toValue());

        sql = " where (`name` = ?) and (`group` = ?) and (`age` = ?)";
        searchQuery = new SearchQuery().and("name", 1).and("group", "test").and("age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 数据为空，则字段默认隐藏 ---------------------
        sql = " where `name` = ?";
        searchQuery = new SearchQuery().andEm("name", 1, "group", null);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(1), searchQuery.toValue());

        sql = " where `name` = ?";
        searchQuery = new SearchQuery().andEm("name", 1, "group", "");
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(1), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().andEm("name", null);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());


        //--------------------- 采用 em 函数的 ---------------------
        sql = " where (`name` = ?) and (`group` = ?) and (`age` = ?)";
        searchQuery = new SearchQuery().and("name", 1).and("group", "test").and("age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ?) and (`group` = ? and `age` = ?)";
        searchQuery = new SearchQuery().and("name", 1).and("group", "test", "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 采用 And 类的（类And生成的带括号） ---------------------
        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        searchQuery = new SearchQuery(And("name", 1, "group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ?) and ((`group` = ?) and (`age` = ?))";
        searchQuery = new SearchQuery().and("name", 1).and(And("group", "test"), And("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where `name` = ? and (`group` = ? and `age` = ?)";
        searchQuery = new SearchQuery("name", 1, And("group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 采用 NeoMap参数(注意NeoMap的加入顺序没有保障) ---------------------
        NeoMap searchMap = NeoMap.of();
        // 设置有序，否则没有顺序性
        searchMap.openSorted();
        searchMap.put("name", 1);
        searchMap.put("group", "test");
        searchMap.put("age", 3);

        sql = " where `name` = ? and `group` = ? and `age` = ?";
        searchQuery = new SearchQuery(searchMap);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where `name` = ? and `group` = ? and `age` = ?";
        searchQuery = new SearchQuery().andEm(searchMap);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        searchQuery = new SearchQuery().and(searchMap);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 采用 NeoMap多参数 ---------------------
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        searchQuery = new SearchQuery(NeoMap.of("name", 1), NeoMap.of("group", "test"), NeoMap.of("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        // 请开启openSort，否则对应的值可能是无序
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        NeoMap s1 = NeoMap.of("name", 1).openSorted();
        NeoMap s2 = NeoMap.of("group", "test", "age", 3).openSorted();
        searchQuery = new SearchQuery(s1, s2);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        //--------------------- 采用 NeoQueue ---------------------
        NeoQueue<Operate> queue = NeoQueue.of();
        queue.addLast(Or("name", 1, "age", 1));
        queue.addLast(AndEm("group", 1));
        sql = " where (`name` = ? or `age` = ?) and `group` = ?";
        searchQuery = new SearchQuery(queue);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, 1, 1), searchQuery.toValue());
    }

    /**
     * 测试 or
     * where `name` = ? or `group` = ? or `age` = ?
     */
    @Test
    public void orTest() {
        SearchQuery searchQuery;
        String sql;

        //--------------------- 采用 or 函数的（函数or生成的不带括号） ---------------------
        sql = " where (`name` = ? or `group` = ? or `age` = ?)";
        searchQuery = new SearchQuery().or("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ?) or (`group` = ?) or (`age` = ?)";
        searchQuery = new SearchQuery().or("name", 1).or("group", "test").or("age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 采用 Or 类的（类Or生成的sql是带括号的） ---------------------
        sql = " where `name` = ? and (`group` = ?)";
        searchQuery = new SearchQuery("name", 1, Or("group", "test"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test"), searchQuery.toValue());

        sql = " where `name` = ? and (`group` = ? or `age` = ?)";
        searchQuery = new SearchQuery("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());


        //--------------------- 采用 NeoMap参数 ---------------------
        NeoMap searchMap = NeoMap.of().openSorted();
        searchMap.put("name", 1);
        searchMap.put("group", "test");
        searchMap.put("age", 3);

        // 由于NeoMap的key是不重复的，这里就没有对or进行支持，所以请采用NeoQueue做更灵活的配置
        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
        searchQuery = new SearchQuery().or(searchMap);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());
    }

    /**
     * 测试 or 和 and 结合的部分
     * where `name` = ? and (`group` = ? or `age` = ?)
     */
    @Test
    public void andOrTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where (`name` = ? and (`group` = ? or `age` = ?))";
        searchQuery = new SearchQuery().and("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        // 对于内部已经有的Or会在遇到外部的and时候会处理掉
        sql = " where `name` = ? and ((`group` = ? or `age` = ?))";
        searchQuery = new SearchQuery().andEm("name", 1).and(Or("group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ? and ((`group` = ?) and (`age` = ?)))";
        searchQuery = new SearchQuery().and("name", 1, And(Or("group", "test"), Or("age", 3)));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where (`name` = ? and ((`group` = ? or `age` = ?)))";
        searchQuery = new SearchQuery().and("name", 1, And(Or("group", "test", "age", 3)));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());

        sql = " where `name` = ? and ((`group` = ? or `age` = ?))";
        searchQuery = new SearchQuery().andEm("name", 1).and(Or("group", "test", "age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(1, "test", 3), searchQuery.toValue());
    }

    /**
     * 关系操作符：{@code 等于（=）、大于（>）、小于（<）、大于等于（>=）、小于等于（<=）、不等于（!=）}
     * 也是默认的操作符
     */
    @Test
    public void relationTest() {
        SearchQuery searchQuery;
        String sql;

        // 等于
        sql = " where (`name` = ? and `age` = ?)";
        searchQuery = new SearchQuery().and(Equal("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());

        // 不等于
        sql = " where (`name` != ? and `age` = ?)";
        searchQuery = new SearchQuery().and(NotEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());

        // 大于
        sql = " where (`name` > ? and `age` = ?)";
        searchQuery = new SearchQuery().and(GreaterThan("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());

        // 大于等于
        sql = " where (`name` >= ? and `age` = ?)";
        searchQuery = new SearchQuery().and(GreaterEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());

        // 小于
        sql = " where (`name` < ? and `age` = ?)";
        searchQuery = new SearchQuery().and(LessThan("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());

        // 小于等于
        sql = " where (`name` <= ? and `age` = ?)";
        searchQuery = new SearchQuery().and(LessEqual("name", "tt"), Equal("age", 3));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList("tt", 3), searchQuery.toValue());
    }

    /**
     * 其他符号测试：like
     * where `name` like '%chou' and `age` = ?
     */
    @Test
    public void likeOrNotTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where (`name` like '%chou' and `age` = ?)";
        searchQuery = new SearchQuery().and(Like("name", "%chou"), "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(3), searchQuery.toValue());

        sql = " where (`name` not like '%chou' and `age` = ?)";
        searchQuery = new SearchQuery().and(NotLike("name", "%chou"), "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(3), searchQuery.toValue());

        sql = " where (`age` = ?)";
        searchQuery = new SearchQuery().and(Like("name", "%"), "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(3), searchQuery.toValue());

        sql = " where (`age` = ?)";
        searchQuery = new SearchQuery().and(Like("name", "null%"), "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(3), searchQuery.toValue());

        sql = " where (`age` = ?)";
        searchQuery = new SearchQuery().and(Like("name", null), "age", 3);
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(3), searchQuery.toValue());
    }

    /**
     * 其他符号测试：in
     * where id in (12,32,43,43)
     */
    @Test
    public void inOrNotTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where (`id` in ('12', '13', '14'))";
        List<Long> dataList = new ArrayList<>();
        dataList.add(12L);
        dataList.add(13L);
        dataList.add(14L);
        searchQuery = new SearchQuery().and(In("id", dataList));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " where (`id` not in ('12', '13', '14'))";
        dataList = new ArrayList<>();
        dataList.add(12L);
        dataList.add(13L);
        dataList.add(14L);
        searchQuery = new SearchQuery().and(NotIn("id", dataList));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 其他符号测试：is null
     * where `name` is null
     */
    @Test
    public void isNullOrNotTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where (`name` is null)";
        searchQuery = new SearchQuery().and(IsNull("name"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " where (`name` is not null)";
        searchQuery = new SearchQuery().and(IsNotNull("name"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().and(IsNotNull(null));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 其他符号测试：group by
     * where `name` = 'test' group by 'group'
     */
    @Test
    public void groupByTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where (`name` = ?) group by `group`";
        searchQuery = new SearchQuery().and("name", 12).append(GroupBy("group"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(12), searchQuery.toValue());

        // 测试group by 是否可以有where
        sql = " group by `group`";
        searchQuery = new SearchQuery().and("name", null).append(GroupBy("group"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 其他符号测试：order by
     */
    @Test
    public void orderByTest() {
        SearchQuery searchQuery;
        String sql;

        // 默认为升序
        sql = " order by `create_time`";
        searchQuery = new SearchQuery().append(OrderBy("create_time"));
        Assert.assertEquals(sql, searchQuery.toSql());

        // 设置升序降序字段
        sql = " order by `create_time` desc";
        searchQuery = new SearchQuery().append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());

        // order by 多个字段
        sql = " order by `create_time` desc, `id` asc, `group` desc";
        searchQuery = new SearchQuery().append(OrderBy("create_time", "desc", "id", "asc", "group", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());

        // order by 多个字段
        sql = " order by `create_time`, `id`, `group` desc";
        searchQuery = new SearchQuery().append(OrderBy("create_time", "id", "group", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());

        // order by 多个字段
        sql = " order by `create_time`, `id` desc, `group` desc";
        searchQuery = new SearchQuery().append(OrderBy("create_time", "id", "desc", "group", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());
    }

    @Test
    public void teststetst(){
        SearchQuery searchQuery;
        searchQuery = new SearchQuery().append(OrderBy("create_time", "id", "desc", "group", "asc"));
        show(searchQuery.getFirstOperateStr(NeoConstant.ORDER_BY));
        show(DevideMultiNeo.getColumnAndSortList(searchQuery));

        NeoMap dataMap = NeoMap.of();
        dataMap.put("order by", "id, name desc, group asc");
        show(DevideMultiNeo.getColumnAndSortList(dataMap));
    }

    /**
     * 其他符号测试：order by desc
     */
    @Test
    public void orderByDescTest1() {
        SearchQuery searchQuery;
        String sql;

        // 默认为升序
        sql = " where (`name` = ?) order by `create_time` asc";
        searchQuery = new SearchQuery().and("name", "test").append(OrderBy("create_time", "asc"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList("test"), searchQuery.toValue());

        sql = " where (`name` = ?) order by `create_time` desc";
        searchQuery = new SearchQuery().and("name", "test").append(OrderByDesc("create_time"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList("test"), searchQuery.toValue());

        sql = " where (`name` = ?) order by `create_time` desc";
        searchQuery = new SearchQuery().and("name", "test").append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList("test"), searchQuery.toValue());

        // 测试跟where关系
        sql = " order by `create_time` desc";
        searchQuery = new SearchQuery().and("name", null).append(OrderBy("create_time", "desc"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 符号测试：exists
     */
    @Test
    public void existsOrNotTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where exists (select id from xxx)";
        searchQuery = new SearchQuery().append(Exists("select id from xxx"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " where not exists (select id from xxx)";
        searchQuery = new SearchQuery().append(NotExists("select id from xxx"));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 符号测试：exists
     */
    @Test
    public void pageTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " limit 20 offset 0";
        searchQuery = new SearchQuery().append(Page(NeoPage.of(1, 20)));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " limit 20 offset 0";
        PageReq<Object> pageReq = new PageReq<>();
        pageReq.setCurrent(1);
        pageReq.setSize(20);
        searchQuery = new SearchQuery().append(Page(pageReq));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " limit 20 offset 0";
        searchQuery = new SearchQuery().append(Page(1, 20));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " where (`name` = ?) limit 20 offset 0";
        searchQuery = new SearchQuery().and("name", 12).append(Page(NeoPage.of(1, 20)));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(12), searchQuery.toValue());
    }

    /**
     * 符号测试：between and
     */
    @Test
    public void betweenOrNotTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " where `age` between ? and ?";
        searchQuery = new SearchQuery().append(BetweenAnd("age", 12, 60));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(12, 60), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().append(BetweenAnd("age", null, 60));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().append(BetweenAnd("age", null, null));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = " where `age` not between ? and ?";
        searchQuery = new SearchQuery().append(NotBetweenAnd("age", 12, 60));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Arrays.asList(12, 60), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().append(NotBetweenAnd("age", null, 60));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().append(NotBetweenAnd("age", null, null));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    /**
     * 符号测试：Em
     */
    @Test
    public void emTest() {
        SearchQuery searchQuery;
        String sql;

        sql = " match(`break_law_detail`) against (?)";
        searchQuery = new SearchQuery().append(Em("match(`break_law_detail`) against (?)", 12));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.singletonList(12), searchQuery.toValue());

        sql = "";
        searchQuery = new SearchQuery().append(Em("match(`break_law_detail`) against (?)", null));
        Assert.assertEquals(sql, searchQuery.toSql());
        Assert.assertEquals(Collections.emptyList(), searchQuery.toValue());
    }

    @Test
    public void appendTest() {
        SearchQuery searchQuery;
        String sql;

        // 普通拼接
        NeoMap dataMap = NeoMap.of();
        dataMap.put("a", 1);
        dataMap.put("b", 2);
        dataMap.put("c", 3);

        sql = " where (`a` = ? and `b` = ?) for update";
        searchQuery = new SearchQuery().and(dataMap.assign("a", "b")).append(" for update");
        Assert.assertEquals(sql, searchQuery.toSql());
        dataMap.clear();

        // 测试为空的拼接
        dataMap.setSupportValueNull(true);
        dataMap.put("a", null);
        dataMap.put("b", null);
        dataMap.put("c", 3);

        sql = " for update";
        searchQuery = new SearchQuery().and(dataMap.assign("a", "b")).append(" for update");
        Assert.assertEquals(sql, searchQuery.toSql());
    }
}
