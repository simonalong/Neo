package com.simon.neo.neotest;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午7:54
 */
public class NeoJoinTest extends NeoBaseTest {

    public NeoJoinTest() throws SQLException {}

    /**
     * join 采用的是innerJoin
     */
    @Test
    public void joinOneTest1() {
        String tableName = "neo_table1";
        String otherTableName = "neo_table2";
        String tailSql = "";
        show(neo.join(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    /**
     * join 采用的是innerJoin
     */
    @Test
    public void joinOneTest2() {
        String tableName = "neo_table1";
        String otherTableName = "neo_table2";
        String tailSql = "";
        show(neo.join(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    /**
     * join 采用的是innerJoin
     */
    @Test
    public void joinListTest() {
        String tableName = "neo_table1";
        String otherTableName = "neo_table2";
        show(neo.join(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of()));
    }

    /**
     * join 采用的是innerJoin
     */
    @Test
    public void joinListValueTest() {
        String tableName = "neo_table1";
        String otherTableName = "neo_table2";
        show(neo.join(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), NeoMap.of()));
    }

    /**
     * join 采用的是innerJoin
     */
    @Test
    public void joinListValuesTest() {
        String tableName = "neo_table1";
        String otherTableName = "neo_table2";
        String tailSql = "";
        show(neo.join(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void leftJoinListTest1() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.leftJoin(tableName, otherTableName).on("id", "otherId")
            .list(Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void leftJoinListTest2() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.rightJoin(tableName, otherTableName).on("id", "otherId")
            .list(Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void leftJoinListTest3() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.innerJoin(tableName, otherTableName).on("id", "otherId")
            .list(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void outerJoinTest() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.outerJoin(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void leftJoinExceptInnerTest() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.leftJoinExceptInner(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void rightJoinExceptInnerTest() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.rightJoinExceptInner(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), NeoMap.of(), tailSql));
    }

    @Test
    public void outerJoinExceptInnerTest() {
        String tableName = "";
        String otherTableName = "";
        String tailSql = "";
        show(neo.outerJoinExceptInner(tableName, otherTableName).on("id", "otherId")
            .one(Columns.of("group"), Columns.of("group"), NeoMap.of(), tailSql));
    }
}
