package com.simonalong.neo.operate;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.express.Express;
import org.junit.Assert;
import org.junit.Test;

import static com.simonalong.neo.express.BaseOperate.*;

/**
 * @author shizi
 * @since 2020/8/29 11:13 上午
 */
public class NeoExpress extends NeoBaseTest {

    /**
     * 测试and所有形式
     * where `name` = ? and `group` = ? and `age` = ?
     */
    @Test
    public void antTest() {
        Express neoExpress;
        String sql;

        //--------------------- 采用 and 函数的（函数and不带括号） ---------------------
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().and("name", 1, "group", "test", "age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ?";
//        neoExpress = new Express().and("name", 1);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().and("name", 1).and("group", "test").and("age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//
//        //--------------------- 采用 em 函数的 ---------------------
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().em("name", 1).and("group", "test").and("age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().em("name", 1).and("group", "test", "age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//
//        //--------------------- 采用 And 类的（类And生成的带括号） ---------------------
//        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
//        neoExpress = new Express(And("name", 1, "group", "test", "age", 3));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and (`group` = ?) and (`age` = ?)";
//        neoExpress = new Express().em("name", 1).and(And("group", "test"), And("age", 3));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and (`group` = ? and `age` = ?)";
//        neoExpress = new Express("name", 1, And("group", "test", "age", 3));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//
////        //--------------------- 采用 NeoMap参数(注意NeoMap的加入顺序没有保障) ---------------------
//        NeoMap searchMap = NeoMap.of();
//        // 设置有序，否则没有顺序性
//        searchMap.openSorted();
//        searchMap.put("name", 1);
//        searchMap.put("group", "test");
//        searchMap.put("age", 3);
//
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express(searchMap);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().and(searchMap);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        // 注意：这里如果采用，支持默认为And的形式
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express().em(searchMap);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//
//        //--------------------- 采用 NeoMap多参数 ---------------------
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        neoExpress = new Express(NeoMap.of("name", 1), NeoMap.of("group", "test"), NeoMap.of("age", 3));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and `group` = ? and `age` = ?";
//        NeoMap s1 = NeoMap.of("name", 1).openSorted();
//        NeoMap s2 = NeoMap.of("group", "test", "age", 3).openSorted();
//        neoExpress = new Express(s1, s2);
//        Assert.assertEquals(sql, neoExpress.toSql());

        //--------------------- 采用 NeoQueue ---------------------
        NeoQueue queue = NeoQueue.of();
        queue.addLast(Or("name", 1, "age", 1));
        queue.addLast(AndEm("group", 1));
        sql = " where (`name` = ? or `age` = ?) and `group` = ?";
        neoExpress = new Express(queue);
        Assert.assertEquals(sql, neoExpress.toSql());

        // todo
    }

    /**
     * 测试 or
     * where `name` = ? or `group` = ? or `age` = ?
     */
    @Test
    public void orTest() {
        Express neoExpress;
        String sql;

        //--------------------- 采用 or 函数的（函数or生成的不带括号） ---------------------
        sql = " where `name` = ? or `group` = ? or `age` = ?";
        neoExpress = new Express().or("name", 1, "group", "test", "age", 3);
        Assert.assertEquals(sql, neoExpress.toSql());

        sql = " where `name` = ? or `group` = ? or `age` = ?";
        neoExpress = new Express().or("name", 1).or("group", "test").or("age", 3);
        Assert.assertEquals(sql, neoExpress.toSql());


        //--------------------- 采用 em 函数的 ---------------------
//        sql = " where `name` = ? or `group` = ? or `age` = ?";
//        neoExpress = new Express().em("name", 1).or("group", "test").or("age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? or `group` = ? or `age` = ?";
//        neoExpress = new Express().em("name", 1).or("group", "test", "age", 3);
//        Assert.assertEquals(sql, neoExpress.toSql());


        //--------------------- 采用 Or 类的（类Or生成的sql是带括号的） ---------------------
        sql = " where `name` = ? or (`group` = ? or `age` = ?)";
        neoExpress = new Express("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, neoExpress.toSql());

//        sql = " where `name` = ? or (`group` = ?) or (`age` = ?)";
//        neoExpress = new Express().em("name", 1, Or("group", "test"), Or("age", 3));
//        Assert.assertEquals(sql, neoExpress.toSql());


        //--------------------- 采用 NeoMap参数 ---------------------
        NeoMap searchMap = NeoMap.of().openSorted();
        searchMap.put("name", 1);
        searchMap.put("group", "test");
        searchMap.put("age", 3);

        // 由于NeoMap的key是不重复的，这里就没有对or进行支持，所以请采用NeoQueue做更灵活的配置
        sql = " where `name` = ? and `group` = ? and `age` = ?";
        neoExpress = new Express().or(searchMap);
        Assert.assertEquals(sql, neoExpress.toSql());

//        sql = " where (`name` = ? and `group` = ? and `age` = ?)";
//        neoExpress = new Express().em(Or(searchMap));
//        Assert.assertEquals(sql, neoExpress.toSql());
    }

    /**
     * 测试 or 和 and 结合的部分
     * where `name` = ? and (`group` = ? or `age` = ?)
     */
    @Test
    public void andOrTest() {
        Express neoExpress;
        String sql;

        sql = " where `name` = ? and (`group` = ? or `age` = ?)";
        neoExpress = new Express().and("name", 1, Or("group", "test", "age", 3));
        Assert.assertEquals(sql, neoExpress.toSql());

        // 对于内部已经有的Or会在遇到外部的and时候会处理掉
//        sql = " where `name` = ? and (`group` = ? or `age` = ?)";
////        neoExpress = new Express().em("name", 1).and(Or("group", "test", "age", 3));
////        neoExpress = new Express().em("name", 1).or("group", "test", "age", 3);
//        neoExpress = new Express().em("name", 1).and(AndEm(Or("group", "test", "age", 3)));
//        Assert.assertEquals(sql, neoExpress.toSql());

        // todo 明天继续调整这里的部分
//
//        sql = " where `name` = ? and ((`group` = ?) or (`age` = ?))";
//        neoExpress = new Express().and("name", 1, And(Or("group", "test"), Or("age", 3)));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        sql = " where `name` = ? and ((`group` = ?) or (`age` = ?))";
//        neoExpress = new Express().em("name", 1).and(And(Or("group", "test"), Or("age", 3)));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        // 其中Op是空串（但是带括号）
//        sql = " where `name` = ? and ((`group` = ?) or (`age` = ?))";
//        neoExpress = new Express().em("name", 1).and(Em("group", "test", Or("age", 3)));
//        Assert.assertEquals(sql, neoExpress.toSql());
//
//        // 其中Op是空串（但是带括号）
//        sql = " where `name` = ? and ((`group` = ?) or (`age` = ?))";
//        neoExpress = new Express().em("name", 1).and(Em(Or("group", "test"), Or("age", 3)));
//        Assert.assertEquals(sql, neoExpress.toSql());
    }
//
//
//    /**
//     * 其他比较符号测试：等于
//     * 也是默认的操作符
//     * where `name` != 12 and `age` = ?
//     */
//    @Test
//    public void equalTest() {
        //sql = " where `name` = 'tt' and `age` = ?
//        Express express1 = new Express().and(Equal("name", "tt")).and(Equal("age", 3));
//
//        Express express2 = new Express().and("name", 12, "age", 3);
//    }
//
//
//    /**
//     * 其他比较符号测试：不等于
//     * where `name` != 12 and `age` = ?
//     */
//    @Test
//    public void notEqualTest() {
//        Express express1 = new Express().and(NotEqual("name", 12)).and("age", 3);
//
//        Express express2 = new Express().and(NotEqual("name", 12), "age", 3);
//    }
//
//    /**
//     * 其他比较符号测试：大于、小于
//     * where `name` = 'chou' and `age` > 3 and `age` < 50
//     */
//    @Test
//    public void compareTest() {
//        Express express2 = new Express().and("name", "chou", GreaterThan("age", 3), LessThan("age", 50));
//    }
//
//    /**
//     * 其他比较符号测试：大于等于、小于等于
//     * where `name` = 'chou' and `age` > 3 and `age` < 50
//     */
//    @Test
//    public void compareEqualTest() {
//        Express express2 = new Express().and("name", "chou", GreaterEqual("age", 3), LessEqual("age", 50));
//    }
//
//    /**
//     * 其他符号测试：like
//     * where `name` like '%chou' and `age` = ?
//     */
//    @Test
//    public void likeTest() {
//        Express express;
//        express = new Express().and(Like("name", "chou", "%"), "age", 3);
//
//        express = new Express().and(Like("name", "%", "chou"), "age", 3);
//
//        express = new Express().and(Like("name", "%", "chou", "%"), "age", 3);
//    }
//
//    /**
//     * 其他符号测试：not like
//     * where `name` like '%chou' and `age` = ?
//     */
//    @Test
//    public void notLikeTest() {
//        Express express;
//        express= new Express().and(NotLike("name", "chou", "%"), "age", 3);
//
//        express = new Express().and(NotLike("name", "%", "chou"), "age", 3);
//
//        express = new Express().and(NotLike("name", "%", "chou", "%"), "age", 3);
//    }
//
//    /**
//     * 其他符号测试：in
//     * where id in (12,32,43,43)
//     */
//    @Test
//    public void inTest() {
//        Express express;
//
//        List<Long> dataList = new ArrayList<>();
//        dataList.add(12L);
//        dataList.add(13L);
//        dataList.add(14L);
//        express = new Express().and(In("id", dataList));
//    }
//
//
//    /**
//     * 其他符号测试：not in
//     * where not id in (12,32,43,43)
//     */
//    @Test
//    public void notInTest() {
//        Express express;
//
//        List<Long> dataList = new ArrayList<>();
//        dataList.add(12L);
//        dataList.add(13L);
//        dataList.add(14L);
//        express = new Express().and(NotIn("id", dataList));
//    }
//
//    /**
//     * 其他符号测试：is null
//     * where `name` is null
//     */
//    @Test
//    public void isNullTest() {
//        Express express;
//
//        List<Long> dataList = new ArrayList<>();
//        dataList.add(12L);
//        dataList.add(13L);
//        dataList.add(14L);
//        express = new Express().and(IsNull("id"));
//    }
//
//    /**
//     * 其他符号测试：is null
//     * where `name` is null
//     */
//    @Test
//    public void isNotNullTest() {
//        Express express;
//
//        List<Long> dataList = new ArrayList<>();
//        dataList.add(12L);
//        dataList.add(13L);
//        dataList.add(14L);
//        express = new Express().em(IsNotNull("id"));
//    }
//
//    /**
//     * 其他符号测试：group by
//     * where `name` = 'test' group by 'group'
//     */
//    @Test
//    public void groupByTest() {
//        Express express;
//
//        express = new Express().and("name", "test").em(GroupBy("group"));
//    }
//
//    /**
//     * 其他符号测试：order by
//     * where `name` = 'test' order by 'create_time'
//     */
//    @Test
//    public void orderByTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where `name` = 'test' order by 'create_time'
//        express = new Express().and("name", "test").em(OrderBy("create_time"));
//        express = new Express().and("name", "test").em(OrderBy("create_time", "asc"));
//        express = new Express().and("name", "test").em(OrderBy("create_time", "desc"));
//    }
//
//    /**
//     * where字符测试
//     * <p>
//     *    在没有一些条件的时候，where字段不再存在
//     *
//     *  where `name` = 'test' order by 'create_time'
//     */
//    @Test
//    public void whereTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where `name` = 'test' order by `create_time`
//        express = new Express().and("name", "test").em(OrderBy("create_time"));
//        // order by 'create_time'
//        express = new Express().em(OrderBy("create_time"));
//
        //sql = " where `name` = 'test' order by `create_time` asc
//        express = new Express().and("name", "test").em(OrderBy("create_time", "asc"));
//
        //sql = " where `name` = 'test' order by `create_time` desc
//        express = new Express().and("name", "test").em(OrderBy("create_time", "desc"));
//    }
//
//    /**
//     * 符号测试：exist
//     * where exist(xxxxx)
//     */
//    @Test
//    public void orderByTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where exists(select id from xxx)
//        express = new Express().and(Exists("select id from xxx"));
//    }
//
//    /**
//     * 符号测试：not exist
//     * where exist(xxxxx)
//     */
//    @Test
//    public void orderByTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where not exists(select id from xxx)
//        express = new Express().and(NotExists("select id from xxx"));
//    }
//
//    /**
//     * 符号测试：not exist
//     * where `age` between 12 and 60
//     */
//    @Test
//    public void betweenTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where not exists(select id from xxx)
//        express = new Express().and(BetweenAnd("age", 12, 60));
//    }
//
//    /**
//     * 符号测试：not exist
//     * where `age` not between 12 and 60
//     */
//    @Test
//    public void notBetweenTest() {
//        Express express;
//
//        // 默认为升序
        //sql = " where `age` not between 12 and 60
//        express = new Express().and(NotBetweenAnd("age", 12, 60));
//    }
}
