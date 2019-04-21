package com.simon.neo.neotable;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoPage;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午9:11
 */
public class NeoTableTest extends BaseNeoTableTest {

    public NeoTableTest() throws SQLException {}

    @Test
    public void testInsert(){
        show(tinaTest.insert(NeoMap.of("group", "table1")));
    }

    @Test
    public void testDelete(){
        show(tinaTest.delete(NeoMap.of("group", "table1")));
    }

    @Test
    public void testUpdate(){
        show(tinaTest.update(NeoMap.of("group", "tableChg"), NeoMap.of("group", "table1")));
    }

    @Test
    public void testOne(){
        show(tinaTest.delete(NeoMap.of("group", "table1")));
    }

    @Test
    public void one(){
        show(tinaTest.one(NeoMap.of("group", "tableChg")));
    }

    @Test
    public void list1(){
        show(tinaTest.list(NeoMap.of("group", "con1")));
    }

    @Test
    public void list2(){
        show(tinaTest.list(Columns.of("group"), NeoMap.of("group", "con1")));
    }

    @Test
    public void value(){
        show(tinaTest.value("group", NeoMap.of("group", "con1")));
    }

    @Test
    public void value1(){
        show(tinaTest.value(Integer.class, "age", NeoMap.of("group", "ok2")));
    }

    @Test
    public void values(){
        show(tinaTest.values(Integer.class, "age", NeoMap.of("group", "ok2")));
    }

    @Test
    public void page(){
        show(tinaTest.page(Columns.of("group", "name"), NeoMap.of("group", "ok2"), 0, 10));
    }

    @Test
    public void page2(){
        show(tinaTest.page(Columns.of("group"), NeoMap.of("group", "ok2"), NeoPage.of(1, 10)));
    }

    @Test
    public void count(){
        show(tinaTest.count());
    }

    @Test
    public void count1(){
        show(tinaTest.count(NeoMap.of("group", "ok2")));
    }

    @Test
    public void getCreateTable(){
        show(tinaTest.getTableCreate());
    }

    /**
     * select neo_table2.`group` from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc limit 1
     */
    @Test
    public void joinTest() {
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(tinaTest.join(otherTableName).on("group", "group")
            .one(Columns.of("group"), tailSql));
    }

    /**
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc limit 1
     */
    @Test
    public void leftJoinOneTest1() {
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        // {group1=test3, group2=test3}
        show(tinaTest.leftJoin(otherTableName).on("group", "group")
            .one(Columns.of("group as group1"), Columns.of("group as group2"), NeoMap.of(), tailSql));
    }

    /**
     * select neo_table2.`group` from neo_table2 right join neo_table1 on neo_table2.`group`=neo_table1.`group`   limit 1
     */
    @Test
    public void leftJoinOneTest2() {
        String otherTableName = "neo_table1";
        show(tinaTest.rightJoin(otherTableName).on("group", "group")
            .one(Columns.of("group"), NeoMap.of()));
    }

    /**
     * select neo_table2.`group` from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc
     */
    @Test
    public void leftJoinListTest() {
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(tinaTest.innerJoin(otherTableName).on("group", "group")
            .list(Columns.of("group"), NeoMap.of(), tailSql));
    }

    /**
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc
     */
    @Test
    public void rightJoinOneTest1() {
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(tinaTest.leftJoin(otherTableName).on("group", "group")
            .list(Columns.of("group as group1"), Columns.of("group as group2"), NeoMap.of(), tailSql));
    }

    /**
     * select neo_table2.`group` as group1, neo_table1.`group` as group1 from neo_table2 right join neo_table1 on neo_table2.`group`=neo_table1.`group`
     */
    @Test
    public void rightJoinOneTest2() {
        String otherTableName = "neo_table1";
        show(tinaTest.rightJoin(otherTableName).on("group", "group")
            .list(Columns.of("group as group1"), Columns.of("group as group1")));
    }

    /**
     * select neo_table1.group from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`
     */
    @Test
    public void rightJoinOneTest3() {
        String otherTableName = "neo_table1";
        show(tinaTest.innerJoin(otherTableName).on("group", "group")
            .values(otherTableName, "group"));
    }

    /**
     * select neo_table2.`group` from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc limit 1
     */
    @Test
    public void innerJoinTest() {
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(tinaTest.innerJoin(otherTableName).on("group", "group")
            .one(Columns.of("group"), tailSql));
    }

    /**
     * 请注意：mysql不支持 outer join
     *
     * select neo_table2.`group` from neo_table2 outer join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table2.id is nullneo_table2.id is null)  limit 1
     */
    @Test
    public void outerInnerTest() {
        String otherTableName = "neo_table1";
        show(tinaTest.outerJoinExceptInner(otherTableName).on("group", "group")
            .one(Columns.of("group")));
    }

    /**
     * select neo_table2.`group` from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table1.id is null)
     */
    @Test
    public void leftJoinExceptInnerListTest1() {
        String otherTableName = "neo_table1";
        show(tinaTest.leftJoinExceptInner(otherTableName).on("group", "group")
            .list(Columns.of("group")));
    }

    /**
     * select neo_table1.`group` from neo_table2 right join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table2.id is null)
     */
    @Test
    public void rightJoinExceptInnerListTest2() {
        String otherTableName = "neo_table1";
        show(tinaTest.rightJoinExceptInner(otherTableName).on("group", "group")
            .list(Columns.of(), Columns.of("group")));
    }

    /**
     * 请注意：mysql不支持 outer join
     */
    @Test
    public void outerJoinExceptInnerTest() {
        String otherTableName = "neo_table1";
        show(tinaTest.outerJoinExceptInner(otherTableName).on("group", "group")
            .one(Columns.of("group")));
    }
}
