package com.simonalong.neo.map.table;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.exception.NeoMapChgException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

/**
 * @author shizi
 * @since 2020/3/22 下午3:37
 */
public class TableMapTest extends BaseTest {

    /**
     * 测试of
     */
    @Test
    public void testOf() {
        TableMap tableMap = TableMap.of("table1", "name", "nana", "age", 12);
        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12));
    }

    /**
     * 测试from
     */
    @Test
    public void testFrom1() {
        TableMapEntity entity = new TableMapEntity();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.from("table1", entity);

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12, "user_name", "user"));
    }

    /**
     * 测试from：实体有注解@Table
     */
    @Test
    public void testFrom2() {
        TableMapEntity2 entity = new TableMapEntity2();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.from(entity);

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12, "user_name", "user"));
    }

    /**
     * 测试from：自定义转换规则
     */
    @Test
    public void testFrom3() {
        TableMapEntity entity = new TableMapEntity();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.from("table1", entity, NeoMap.NamingChg.UNDERLINE);

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12, "user_name", "user"));
    }

    /**
     * 测试from：包含指定的列
     */
    @Test
    public void testFrom4() {
        TableMapEntity entity = new TableMapEntity();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.from("table1", entity, "name", "age");

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12));
    }

    /**
     * 测试fromInclude：同from
     */
    @Test
    public void testFromInclude() {
        TableMapEntity entity = new TableMapEntity();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.fromInclude("table1", entity, "name", "age");

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("name", "nana", "age", 12));
    }

    /**
     * 测试 fromExclude
     */
    @Test
    public void testFromExclude() {
        TableMapEntity entity = new TableMapEntity();
        entity.setName("nana");
        entity.setAge(12);
        entity.setUserName("user");
        TableMap tableMap = TableMap.fromExclude("table1", entity, "name", "age");

        Assert.assertEquals(tableMap.get("table1"), NeoMap.of("user_name", "user"));
    }

    /**
     * 测试：fromArray 和 asArray
     */
    @Test
    public void testFromArray1() {
        List<TableMapEntity> entityList = new ArrayList<>();
        entityList.add(new TableMapEntity().setName("nana1").setAge(11));
        entityList.add(new TableMapEntity().setName("nana2").setAge(12));

        List<TableMap> resultList = TableMap.fromArray("table1", entityList);

        List<TableMapEntity> expect = TableMap.asArray(TableMapEntity.class, "table1", resultList);
        Assert.assertEquals(expect, entityList);
    }

    /**
     * 测试：fromArray：只转换指定的列
     */
    @Test
    public void testFromArray2() {
        List<TableMapEntity> entityList = new ArrayList<>();
        entityList.add(new TableMapEntity().setName("nana1").setAge(11));
        entityList.add(new TableMapEntity().setName("nana2").setAge(12));

        List<TableMap> resultList = TableMap.fromArray("table1", entityList, Columns.of("age"));

        List<TableMapEntity> expect = new ArrayList<>();
        expect.add(new TableMapEntity().setAge(11));
        expect.add(new TableMapEntity().setAge(12));
        Assert.assertEquals(expect, TableMap.asArray(TableMapEntity.class, "table1", resultList));
    }

    /**
     * 测试：fromArray：只转换指定的列
     */
    @Test
    public void testFromArray3() {
        List<TableMapEntity> entityList = new ArrayList<>();
        entityList.add(new TableMapEntity().setUserName("user1").setAge(11));
        entityList.add(new TableMapEntity().setUserName("user2").setAge(12));

        //[{"table1":{"user_name":"user1","age":11}},{"table1":{"user_name":"user2","age":12}}]
        List<TableMap> resultList = TableMap.fromArray("table1", entityList, NeoMap.NamingChg.UNDERLINE);

        List<TableMapEntity> expect = new ArrayList<>();
        expect.add(new TableMapEntity().setUserName("user1").setAge(11));
        expect.add(new TableMapEntity().setUserName("user2").setAge(12));
        Assert.assertEquals(expect, TableMap.asArray(TableMapEntity.class, "table1", NeoMap.NamingChg.UNDERLINE, resultList));
    }

    /**
     * 测试：fromArray：只转换指定的列
     */
    @Test
    public void testFromArray4() {
        List<TableMapEntity> entityList = new ArrayList<>();
        entityList.add(new TableMapEntity().setUserName("user1").setAge(11));
        entityList.add(new TableMapEntity().setUserName("user2").setAge(12));

        // [{"table1":{"user_name":"user1"}},{"table1":{"user_name":"user2"}}]
        List<TableMap> resultList = TableMap.fromArray("table1", entityList, NeoMap.NamingChg.UNDERLINE, Columns.of("userName"));

        List<TableMapEntity> expect = new ArrayList<>();
        expect.add(new TableMapEntity().setUserName("user1"));
        expect.add(new TableMapEntity().setUserName("user2"));
        Assert.assertEquals(expect, TableMap.asArray(TableMapEntity.class, "table1", NeoMap.NamingChg.UNDERLINE, resultList));
    }

    /**
     * 测试：fromArray：只转换指定的列
     */
    @Test
    public void testFromArray5() {
        List<TableMapEntity> entityList = new ArrayList<>();
        entityList.add(new TableMapEntity().setUserName("user1").setAge(11));
        entityList.add(new TableMapEntity().setUserName("user2").setAge(12));

        // [{"table1":{"user_name":"user1","age":11}},{"table1":{"user_name":"user2","age":12}}]
        List<TableMap> resultList = TableMap.fromArray("table1", entityList, NeoMap.NamingChg.UNDERLINE);

        List<TableMapEntity> expect = new ArrayList<>();
        expect.add(new TableMapEntity().setUserName("user1").setAge(11));
        expect.add(new TableMapEntity().setUserName("user2").setAge(12));
        Assert.assertEquals(expect, TableMap.asArray(TableMapEntity.class, "table1", NeoMap.NamingChg.UNDERLINE, resultList));
    }

    /**
     * 测试：getNeoMap
     */
    @Test
    public void testGetNeoMap() {
        TableMap tableMap = TableMap.of();
        tableMap.put("table1", NeoMap.of("name", "nana", "age", 12));
        tableMap.put("table2", NeoMap.of("name", "nana", "age", 12));

        Assert.assertEquals(NeoMap.of("name", "nana", "age", 12), tableMap.getNeoMap("table1"));
    }

    /**
     * 随机获取测试
     */
    @Test
    public void testGetFirst() {
        TableMap tableMap = TableMap.of();
        tableMap.put("table1", NeoMap.of("name", "nana", "age", 11));

        Assert.assertEquals(NeoMap.of("name", "nana", "age", 11), tableMap.getFirst());
    }

    /**
     * getValueNeoMap
     */
    @Test
    public void testGetValueNeoMap() {
        TableMap tableMap = TableMap.of();
        TableMapInnerEntity innerEntity = new TableMapInnerEntity();
        innerEntity.setName("inner1");
        innerEntity.setDataAddressPath("path");
        tableMap.put("table1", new TableMapEntity().setUserName("user1").setAge(11).setInnerEntity(innerEntity));

        Assert.assertEquals(NeoMap.of("name", "inner1", "data_address_path", "path"), tableMap.getValueNeoMap("table1", "inner_entity"));
    }

    /**
     * put测试：
     */
    @Test(expected = NeoMapChgException.class)
    public void testPut1() {
        TableMap tableMap = TableMap.of();
        tableMap.put("a", 1);
        tableMap.put("b", 2);
    }

    /**
     * put测试：
     */
    @Test
    public void testPut2() {
        TableMap tableMap = TableMap.of();
        tableMap.put("a", new TableMapEntity().setName("name1"));

        Assert.assertEquals(NeoMap.of("name", "name1"), tableMap.get("a"));
    }

    @Test
    public void testToNeoMapList() {
        TableMap tableMap = TableMap.of();
        tableMap.put("a", NeoMap.of("k1", "v1", "k2", "v2"));
        tableMap.put("b", NeoMap.of("k21", "v21", "k22", "v22"));

        List<NeoMap> result = tableMap.toNeoMapList();
        List<NeoMap> expect = new ArrayList<>();
        expect.add(NeoMap.of("k1", "v1", "k2", "v2"));
        expect.add(NeoMap.of("k21", "v21", "k22", "v22"));

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as
     */
    @Test
    public void testAs1() {
        TableMap tableMap = TableMap.of();
        tableMap.put(DEFAULT_TABLE, "name", "nana");
        tableMap.put(DEFAULT_TABLE, "age", 12);
        tableMap.put(DEFAULT_TABLE, "user_name", "user");

        // {"table1":{"age":12,"name":"nana","userName":"user"}}
        TableMapEntityAs result = tableMap.as(TableMapEntityAs.class);

        TableMapEntityAs expect = new TableMapEntityAs();
        expect.setName("nana");
        expect.setAge(12);
        expect.setUserName("user");

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as：包含指定的转换规则
     */
    @Test
    public void testAs2() {
        TableMap tableMap = TableMap.of();
        tableMap.put(DEFAULT_TABLE, "name", "nana");
        tableMap.put(DEFAULT_TABLE, "age", 12);
        tableMap.put(DEFAULT_TABLE, "user_name", "user");

        // {"table1":{"age":12,"name":"nana","userName":"user"}}
        TableMapEntityAs result = tableMap.as(TableMapEntityAs.class, NeoMap.NamingChg.UNDERLINE);

        TableMapEntityAs expect = new TableMapEntityAs();
        expect.setName("nana");
        expect.setAge(12);
        expect.setUserName("user");

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as：指定对应的表名
     */
    @Test
    public void testAs3() {
        TableMap tableMap = TableMap.of();
        tableMap.put("table11", "name", "nana");
        tableMap.put("table11", "age", 12);
        tableMap.put("table11", "user_name", "user");

        // {"table1":{"age":12,"name":"nana","userName":"user"}}
        TableMapEntityAs result = tableMap.as(TableMapEntityAs.class, "table11", NeoMap.NamingChg.UNDERLINE);

        TableMapEntityAs expect = new TableMapEntityAs();
        expect.setName("nana");
        expect.setAge(12);
        expect.setUserName("user");

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as：测试转换到多表
     * <p>
     * 其中表名的优先级为：tableName > @Column > @Table
     */
    @Test
    public void testAs4_1() {
        TableMap tableMap = TableMap.of();
        tableMap.put("table1", "name", "nana");
        tableMap.put("table2", "age", 12);
        tableMap.put("table3", "user_name", "user");

        TableMapEntityAs2 result = tableMap.as(TableMapEntityAs2.class);

        TableMapEntityAs2 expect = new TableMapEntityAs2();
        expect.setName("nana");
        expect.setAge(12);
        expect.setUserName("user");

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as：测试转换到多表
     * <p>
     * 其中表名的优先级为：tableName > @Column > @Table
     */
    @Test
    public void testAs4_2() {
        TableMap tableMap = TableMap.of();
        tableMap.put("table1", "name", "nana");
        tableMap.put("table2", "age", 12);
        tableMap.put("table3", "userName", "user");

        TableMapEntityAs2 result = tableMap.as(TableMapEntityAs2.class, "table2");

        TableMapEntityAs2 expect = new TableMapEntityAs2();
        expect.setAge(12);

        Assert.assertEquals(expect, result);
    }

    @Test
    public void testAs4_3() {
        TableMapEntityAs2 result = TableMap.of("table2", "age", 12).as(TableMapEntityAs2.class);
        TableMapEntityAs2 expect = new TableMapEntityAs2().setAge(12);
        Assert.assertEquals(expect, result);
    }

    /**
     * 测试as：测试@Column的列和转换规则优先级
     * <p>
     * Column > NamingChg
     */
    @Test
    public void testAs5() {
        TableMap tableMap = TableMap.of();
        tableMap.put(DEFAULT_TABLE, "name", "nana");
        tableMap.put(DEFAULT_TABLE, "user_user", "user");

        TableMapEntityAs3 result = tableMap.as(TableMapEntityAs3.class, NeoMap.NamingChg.UNDERLINE);

        TableMapEntityAs3 expect = new TableMapEntityAs3();
        expect.setName("nana");
        expect.setUserName("user");

        Assert.assertEquals(expect, result);
    }

    /**
     * 测试 asArray
     */
    @Test
    public void testAsArray1() {
        List<TableMap> tableMapList = new ArrayList<>();
        tableMapList.add(TableMap.of("table1", "name", "nana1", "age", 11));
        tableMapList.add(TableMap.of("table1", "name", "nana2", "age", 12));
        tableMapList.add(TableMap.of("table1", "name", "nana3", "age", 13));

        List<TableMapAsArrayEntity> dataList = TableMap.asArray(TableMapAsArrayEntity.class, tableMapList);

        List<TableMapAsArrayEntity> expect = new ArrayList<>();
        expect.add(new TableMapAsArrayEntity().setName("nana1").setAge(11));
        expect.add(new TableMapAsArrayEntity().setName("nana2").setAge(12));
        expect.add(new TableMapAsArrayEntity().setName("nana3").setAge(13));

        Assert.assertEquals(expect, dataList);
    }

    /**
     * 测试 asArray：表名进行覆盖@Table中的表名注解
     */
    @Test
    public void testAsArray2() {
        List<TableMap> tableMapList = new ArrayList<>();
        tableMapList.add(TableMap.of("table12", "name", "nana1", "age", 11));
        tableMapList.add(TableMap.of("table12", "name", "nana2", "age", 12));
        tableMapList.add(TableMap.of("table12", "name", "nana3", "age", 13));

        List<TableMapAsArrayEntity> dataList = TableMap.asArray(TableMapAsArrayEntity.class, "table12", tableMapList);

        List<TableMapAsArrayEntity> expect = new ArrayList<>();
        expect.add(new TableMapAsArrayEntity().setName("nana1").setAge(11));
        expect.add(new TableMapAsArrayEntity().setName("nana2").setAge(12));
        expect.add(new TableMapAsArrayEntity().setName("nana3").setAge(13));

        Assert.assertEquals(expect, dataList);
    }

    /**
     * 测试 asArray：多表字段的实体
     * （复杂的多表数据合并为一个实体）
     */
    @Test
    public void testAsArray3() {
        List<TableMap> tableMapList = new ArrayList<>();
        tableMapList.add(TableMap.of("table1", "k1", "nana1", "age1", 11, "user_name", "user1"));
        tableMapList.add(TableMap.of("table2", "k2", "nana2", "age2", 12, "user_name", "user2"));
        tableMapList.add(TableMap.of("table3", "k3", "nana3", "age3", 13, "user_name", "user3"));


        List<TableMapAsArrayEntity3_1> dataList1 = TableMap.asArray(TableMapAsArrayEntity3_1.class, tableMapList);

        List<TableMapAsArrayEntity3_1> expect = new ArrayList<>();
        expect.add(new TableMapAsArrayEntity3_1().setK1("nana1").setUserName("user1"));
        expect.add(new TableMapAsArrayEntity3_1());
        expect.add(new TableMapAsArrayEntity3_1());

        Assert.assertEquals(expect, dataList1);
    }

    @Test
    public void testAssign1() {
        TableMap tableMap = TableMap.of("table1", "k1", "name", "v1", 12);
        NeoMap data = tableMap.assign("table1", "k1");

        Assert.assertEquals(NeoMap.of("k1", "name"), data);
    }

    @Test
    public void testAssign2() {
        TableMap tableMap = TableMap.of("table1", "k1", "name", "v1", 12);
        NeoMap data = tableMap.assign("table1", Columns.of("k1"));

        Assert.assertEquals(NeoMap.of("k1", "name"), data);
    }

    @Test
    public void testAssignExcept() {
        TableMap tableMap = TableMap.of("table1", "k1", "name", "v1", 12);
        NeoMap data = tableMap.assignExcept("table1", Columns.of("v1"));

        Assert.assertEquals(NeoMap.of("k1", "name"), data);
    }
}

