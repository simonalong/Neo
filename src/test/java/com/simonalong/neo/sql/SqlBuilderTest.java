package com.simonalong.neo.sql;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoBaseTest;
import java.util.ArrayList;
import java.util.List;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:28
 */
public class SqlBuilderTest extends NeoBaseTest {

    public SqlBuilderTest()  {}

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

    @Test
    public void buildValueListTest1(){
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        // {`group`=ok, `name`=haode}
        show(SqlBuilder.buildValueList(neoMap));
    }

    @Test
    public void buildValueListTest2(){
        NeoMap neoMap = NeoMap.of("name", "like haode#", "age", ">12");
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
}
