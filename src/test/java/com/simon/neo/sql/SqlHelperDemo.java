package com.simon.neo.sql;

import com.simon.neo.BaseTest;
import com.simon.neo.NeoMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:28
 */
public class SqlHelperDemo extends BaseTest {

    @Test
    public void inTest(){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        dataList.add(4);

        // ('1','2','3','4')
        show(SqlHelper.in(dataList));
    }

    @Test
    public void buildWhereTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // where `group` =  ? and `name` =  ?
        show(SqlHelper.buildWhere(searchMap));
    }

    @Test
    public void buildWhereTest2(){
        NeoMap searchMap = NeoMap.of();
        // 
        show(SqlHelper.buildWhere(searchMap));
    }

    @Test
    public void buildConditionTest(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // `group` =  ? and `name` =  ?
        show(SqlHelper.buildCondition(searchMap));
    }

    @Test
    public void buildWhereWithValueTest(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        //  where `group` = 'group1' and `name` = 'name1'
        show(SqlHelper.buildWhereWithValue(searchMap));
    }

    @Test
    public void buildConditionWithValueTest(){
        NeoMap searchMap = NeoMap.of("group", 1, "name", "name1");
        // `group` = 1 and `name` = 'name1'
        show(SqlHelper.buildConditionWithValue(searchMap));
    }
}
