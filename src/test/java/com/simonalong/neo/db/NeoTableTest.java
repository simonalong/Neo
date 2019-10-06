package com.simonalong.neo.db;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午9:11
 */
public class NeoTableTest extends BaseNeoTableTest {

    static NeoTable tinaTest;
    public NeoTableTest() throws SQLException {}

    @Before
    public void before(){
        tinaTest = neo.asTable("neo_table2");
    }

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
    public void list3(){
        show(tinaTest.list(Columns.of("group"), NeoMap.of("group", "con1", "order by", "name desc")));
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
        show(tinaTest.page(Columns.of("group", "name"), NeoMap.of("group", "ok2"), NeoPage.of(1, 10)));
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
        show(tinaTest.join(otherTableName).on("group", "group")
            .one(Columns.of("group")));
    }

    /**
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc limit 1
     */
    @Test
    public void leftJoinOneTest1() {
        String otherTableName = "neo_table1";
        // {group1=test3, group2=test3}
        show(tinaTest.leftJoin(otherTableName).on("group", "group")
            .one(Columns.of().table("neo_table1", "group as group1").table("neo_table2", "group as group2"),
                NeoMap.of("order by", "sort desc")));
    }

    /**
     * select neo_table2.`group` from neo_table2 right join neo_table1 on neo_table2.`group`=neo_table1.`group`   limit 1
     */
    @Test
    public void leftJoinOneTest2() {
        String otherTableName = "neo_table1";
        show(tinaTest.rightJoin(otherTableName).on("group", "group")
            .one(Columns.of().table(tinaTest.getTableName(), "group")));
    }


    /**
     * select neo_table1.`group`
     * from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`
     * order by `sort` desc limit 1
     */
    @Test
    public void leftJoinValueTest() {
        String otherTableName = "neo_table1";
        show(tinaTest.innerJoin(otherTableName).on("group", "group")
            .value(String.class, otherTableName, "group", NeoMap.of("order by", "sort desc")));
    }

    /**
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  order by sort desc
     */
    @Test
    public void rightJoinOneTest1() {
        String otherTableName = "neo_table3";
        show(tinaTest.leftJoin(otherTableName).on("group", "group")
            .list(Columns.of().table(tinaTest.getTableName(), "group as group1").table(otherTableName, "group as group2"),
                NeoMap.of().table(otherTableName, "order by", "sort desc")));
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
     * select neo_table1.`group`
     * from neo_table2 inner join neo_table1 on neo_table2.`group`=neo_table1.`group`
     * order by `sort` desc limit 1
     */
    @Test
    public void innerJoinTest() {
        String otherTableName = "neo_table1";
        show(tinaTest.innerJoin(otherTableName).on("group", "group")
            .one(Columns.of().table(otherTableName, "group"), NeoMap.of("order by", "sort desc")));
    }
//
//    /**
//     * 请注意：mysql不支持 outer join
//     *
//     * select neo_table2.`group` from neo_table2 outer join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table2.id is nullneo_table2.id is null)  limit 1
//     */
//    @Test
//    public void outerInnerTest() {
//        String otherTableName = "neo_table1";
//        show(tinaTest.outerJoinExceptInner(otherTableName).on("group", "group")
//            .one(Columns.of("group")));
//    }
//
    /**
     * select neo_table2.`group` from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table1.id is null)
     */
    @Test
    public void leftJoinExceptInnerListTest1() {
        String otherTableName = "neo_table1";
        show(tinaTest.leftJoinExceptInner(otherTableName).on("group", "group")
            .list(Columns.of().table(otherTableName, "group")));
    }

//
//    /**
//     * 请注意：mysql不支持 outer join
//     */
//    @Test
//    public void outerJoinExceptInnerTest() {
//        String otherTableName = "neo_table1";
//        show(tinaTest.outerJoinExceptInner(otherTableName).on("group", "group")
//            .one(Columns.of("group")));
//    }
}
