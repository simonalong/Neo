package com.github.simonalong.neo.sql;

import static com.github.simonalong.neo.sql.SqlBuilder.*;

import com.github.simonalong.neo.Columns;
import com.github.simonalong.neo.NeoMap;
import com.github.simonalong.neo.neo.NeoBaseTest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        show(in(dataList));
    }

    @Test
    public void buildWhereTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // where `group` =  ? and `name` =  ?
        show(buildWhere(searchMap));
    }

    @Test
    public void buildWhereTest2(){
        NeoMap searchMap = NeoMap.of();
        // 
        show(buildWhere(searchMap));
    }

    @Test
    public void buildConditionTest1(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        // `group` =  ? and `name` =  ?
        show(buildCondition(searchMap));
    }

    @Test
    public void buildConditionTest2(){
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1").setKeyPre("table1.");
        // table1.`group` =  ? and table1.`name` =  ?
        show(searchMap);
        show(buildCondition(searchMap));
    }

    @Test
    public void buildJoinHeadTest1(){
        String table1 = "table1";
        String table2 = "table2";
        Columns columns = Columns.table(table1).cs("c1", "c11").and(table2).cs("c2", "c22");
        // select table1.`c11`, table1.`c1`, table2.`c22`, table2.`c2`
        show(buildJoinHead(neo, columns));
    }

    @Test
    public void buildJoinHeadTest2(){
        String table1 = "table1";
        String columnName = "c1";
        // select table1.c1
        show(buildJoinHead(table1, columnName));
    }

    @Test
    public void buildJoinTailTest(){
        String table = "table1";
        NeoMap neoMap = NeoMap.of("name", "ok").setKeyPre(table+".");
        String sqlCondition = "table2.id is null";
        //  where (table2.id is null) and `table1.name` = 'ok' order by sort
        show(buildJoinTail(sqlCondition, neoMap));
    }

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
}
