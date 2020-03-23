package com.simonalong.neo.sql;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoBaseTest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:28
 */
public class SqlBuilderTest extends NeoBaseTest {

    public SqlBuilderTest() throws SQLException {}

    @Test
    public void inTest(){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        dataList.add(4);

        // ('1','2','3','4')
        show(SqlBuilder.buildIn(dataList));
    }

    @Test
    public void buildWhereTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // where `group` =  ? and `name` =  ?
        show(SqlBuilder.buildWhere(searchMap));
    }

    @Test
    public void buildWhereConditionTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // `group` =  ? and `name` =  ?
        show(SqlBuilder.buildWhereCondition(searchMap));
    }

    @Test
    public void buildWhereTest2(){
        NeoMap searchMap = NeoMap.of();
        //
        show(SqlBuilder.buildWhere(searchMap));
    }

    @Test
    public void buildConditionTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // `group` =  ? and `name` =  ?
        show(SqlBuilder.buildWhereCondition(searchMap));
    }

    @Test
    public void buildConditionTest2(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // table1.`group` =  ? and table1.`name` =  ?
        show(searchMap);
        show(SqlBuilder.buildWhereCondition(searchMap));
    }

//    @Test
//    public void buildJoinHeadTest1(){
//        String table1 = "table1";
//        String table2 = "table2";
//        Columns columns = Columns.of().table(table1, "c1", "c11").table(table2, "c2", "c22");
//        // select table1.`c11`, table1.`c1`, table2.`c22`, table2.`c2`
//        show(SqlBuilder.buildJoinHead(neo, columns));
//    }
//
//    @Test
//    public void buildJoinHeadTest2(){
//        String table1 = "table1";
//        String columnName = "c1";
//        // select table1.c1
//        show(SqlBuilder.buildJoinHead(table1, columnName));
//    }

//    @Test
//    public void buildJoinTailTest(){
//        NeoMap neoMap = NeoMap.of("name", "ok");
//        String sqlCondition = "table2.id is null";
//        //  where (table2.id is null) and `table1.name` = 'ok' order by sort
//        show(SqlBuilder.buildJoinTail(sqlCondition, neoMap));
//    }

    @Test
    public void mapToTableFilterTest1(){
        NeoMap neoMap = NeoMap.of("table1.name", "haode", "table1.group", "ok");
        // {table1.`group`=ok, table1.`name`=haode}
        show(SqlBuilder.toDbField(neoMap));
    }

    @Test
    public void mapToTableFilterTest2(){
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        // {`group`=ok, `name`=haode}
        show(SqlBuilder.toDbField(neoMap));
    }

    @Test
    public void buildValueListTest1(){
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        // {`group`=ok, `name`=haode}
        show(SqlBuilder.buildValueList(neoMap));
    }

    @Test
    public void buildValueListTest2(){
        NeoMap neoMap = NeoMap.of("name", "like haode", "age", ">12");
        // {`group`=ok, `name`=haode}
        show(SqlBuilder.buildValueList(neoMap));
    }

    @Test
    public void buildConditionMetaTest1(){
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        // [`group` =  ?, `name` =  ?]
        show(SqlBuilder.buildConditionMeta(neoMap));
    }

    @Test
    public void buildConditionMetaTest2() {
        NeoMap neoMap = NeoMap.of("name", "like haode", "group", "ok", "age", ">12");
        // [`age` > ?, `group` =  ?, `name` like 'haode%']
        show(SqlBuilder.buildConditionMeta(neoMap));
    }

    /**
     * 测试 buildOrderBy 该函数
     *
     * <p>
     *     该函数只识别order by的内容
     */
    @Test
    public void buildOrderByTest1() {
        NeoMap neoMap = NeoMap.of("name", "nana", "order by", "age desc, user asc");
        //   order by `age` desc, `user` asc
        show(SqlBuilder.buildOrderBy(neoMap));
    }

}
