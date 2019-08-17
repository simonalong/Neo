package com.simonalong.neo.neo;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.entity.DemoEntity;
import com.simonalong.neo.entity.EnumEntity;
import com.simonalong.neo.entity.TestEntity;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:49
 */
public class NeoMapTest extends BaseTest {
    
    private static final String TABLE_NAME = "neo_table1";

    @Test
    public void testAppend(){
        NeoMap neoMap1 = NeoMap.of("a", 123);
        NeoMap neoMap2 = NeoMap.of("b", 123);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        show(data);
    }

    @Test
    public void testAppend2(){
        NeoMap neoMap1 = NeoMap.of("a", 111);
        NeoMap neoMap2 = NeoMap.of("a", 222);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        show(data);
    }

    /**
     * 默认情况下，属性名和map的key完全一致
     */
    @Test
    public void testAs1() {
        NeoMap map1 = NeoMap.of("user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        DemoEntity demo1 = map1.as(DemoEntity.class);
        // 只有id完全匹配
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=null)
        show(demo1);
    }

    /**
     * 可以指定全局命名转换规则，比如UNDERLINE，就是 dataBaseUser -> data_base_user，请注意，该设置，会对所有的NeoMap生效
     */
    @Test
    public void testAs2() {
        NeoMap.setDefaultNamingChg(NamingChg.UNDERLINE);
        NeoMap map2 = NeoMap.of("user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        DemoEntity demo2 = map2.as(DemoEntity.class);
        // 其中，user_name、id和data_base_name都能匹配上
        // DemoEntity(group=null, name=null, userName=name, id=123, dataBaseName=neo_table1)
        show(demo2);
    }

    /**
     * 如果不想使用全局，则可以通过设置本类的自己的命名转换，可以覆盖全局的
     */
    @Test
    public void testAs3() {
        NeoMap.setDefaultNamingChg(NamingChg.UNDERLINE);
        NeoMap map3 = NeoMap.of("_user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        // 设置本地命名转换，用于覆盖全局命名转换：dataBaseUser -> _data_base_user
        map3.setLocalNaming(NamingChg.PREUNDER);
        // 其中，只有_user_name能匹配上
        // DemoEntity(group=null, name=null, userName=name, id=null, dataBaseName=null)
        DemoEntity demo3 = map3.as(DemoEntity.class);
        show(demo3);
    }

    /**
     * 在as的时候单独设置命名转换
     */
    @Test
    public void testAs4() {
        NeoMap.setDefaultNamingChg(NamingChg.DEFAULT);
        NeoMap map4 = NeoMap.of("user_name", "name", "id", 123L, "data-db-name", TABLE_NAME);
        // dataBaseUser -> data-db-user
        DemoEntity demo4 = map4.as(DemoEntity.class, NamingChg.MIDDLELINE);
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=neo_table1)
        show(demo4);
    }

    /**
     * 类型不同时候的兼容测试
     *
     * 其中id为integer, 但是实体中为Long，通常情况下回报异常，但是现在做了兼容
     */
    @Test
    @SneakyThrows
    public void testAs5() {
        NeoMap.setDefaultNamingChg(NamingChg.DEFAULT);
        NeoMap map4 = NeoMap.of("user_name", "name", "id", 123, "data-db-name", TABLE_NAME);
        // dataBaseUser -> data-db-user
        DemoEntity demo4 = map4.as(DemoEntity.class, NamingChg.MIDDLELINE);
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=neo_table1)
        show(demo4.toString());
    }

    @Test
    @SneakyThrows
    public void testAs6() {
        Long a = 12L;
        NeoMap map4 = NeoMap.of("user_name", "name", "sl", a);
        // dataBaseUser -> data-db-user
        DemoEntity demo4 = map4.as(DemoEntity.class, NamingChg.MIDDLELINE);
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=neo_table1)
        show(demo4.toString());
    }

    // todo
    @Test
    @SneakyThrows
    public void testAs7() {
        Long time = new Date().getTime();
        NeoMap map4 = NeoMap.of("date", time, "sql_date", time, "time", time, "timestamp", time);
//        NeoMap map4 = NeoMap.of("a", 1);
        // sqlDate -> sql_date
        DemoEntity demo4 = map4.as(DemoEntity.class, NamingChg.UNDERLINE);
        // DemoEntity(group=null, name=null, userName=null, id=null, dataBaseName=null, a=1, sl=0, utilDate=null, sqlDate=null, time=null, timestamp=null)
        show(demo4.toString());
    }

    @Test
    public void testFrom1(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.from(demo);
        // NeoMap={dataBaseName=databasename, group=group1, id=212, name=name1, userName=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom2(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.fromInclude(demo, "group", "name");
        // NeoMap={group=group1, name=name1}
        show(neoMap);
    }

    @Test
    public void testFrom3(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.fromExclude(demo, "group", "name");
        // NeoMap={dataBaseName=databasename, id=212, userName=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom4(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, NamingChg.UNDERLINE);
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom5(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.fromInclude(demo, "userName");
        // {userName=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom6(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, Columns.of("userName"));
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom7(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, Columns.of("userName"), NamingChg.UNDERLINE);
        // {data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        show(neoMap);
    }

    @Test
    public void testFrom8(){
        NeoMap sourceMap = NeoMap.of("group", "group1", "userName", "userName1");

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.fromMap(sourceMap, NamingChg.UNDERLINE);
        // {group=group1, user_name=userName1}
        show(neoMap);
    }

    /**
     * 测试时间类型的转换
     */
    @Test
    public void testFrom9(){
        DemoEntity demoEntity = new DemoEntity()
            .setSqlDate(new java.sql.Date(new Date().getTime()))
            .setTime(new Time(new Date().getTime()))
            .setTimestamp(new Timestamp(new Date().getTime()))
            .setUtilDate(new Date());

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demoEntity, NamingChg.UNDERLINE);
        // {group=group1, user_name=userName1}
        show(neoMap);
    }

    /**
     * 用于自定义命名转换
     */
    @Test
    public void testUserDefineNaming(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 自定义转换规则
        NeoMap namingChg = NeoMap.of().append("group", "m_group")
            .append("name", "m_name")
            .append("id", "m_id")
            .append("dataBaseName", "m_data_base_name")
            .append("userName", "m_user_name");

        NeoMap neoMap = NeoMap.from(demo, namingChg);

        // NeoMap={m_data_base_name=databasename, m_group=group1, m_id=212, m_name=name1, m_user_name=userName1}
        show(neoMap);

        // 生成新的数据
        DemoEntity newDemo = neoMap.as(DemoEntity.class);
        // DemoEntity(group=group1, name=name1, userName=userName1, id=212, dataBaseName=databasename)
        show(newDemo);

        // 数据完全一直
        Assert.assertEquals(demo, newDemo);
    }

    @Test
    public void testAssign(){
        NeoMap neoMap1 = NeoMap.of("a", "1", "b", "2", "c", "3");
        NeoMap neoMap2 = NeoMap.of("a", "1", "c", "3");
        NeoMap neoMapResult = neoMap1.assign(Columns.of("a", "c"));

        Assert.assertEquals(neoMap2.toString(), neoMapResult.toString());
    }

    @Test
    public void setPreTest(){
        NeoMap neoMap = NeoMap.of("a", "ok", "b", "name");
        // {t1.a=ok, t1.b=name}
        show(neoMap.setKeyPre("t1."));
    }

    @Test
    public void keyConvertTest(){
        NeoMap neoMap = NeoMap.of("a", "ok", "b", "name");
        // {t1.a=ok, t1.b=name}
        show(neoMap.keyConvert("a", "a1", "b", "b1"));
    }

    @Test
    public void getBooleanTest(){
        NeoMap neoMap = NeoMap.of("flag", "true", "test", "asdf", "test2", 2321);
        show(neoMap.getBoolean("flag"));
        show(neoMap.getBoolean("test"));
        show(neoMap.getBoolean("test2"));
    }

    @Test
    public void getCharacterTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getCharacter("flag"));
        show(neoMap.getCharacter("test"));
        show(neoMap.getCharacter("test2"));
        show(neoMap.getCharacter("t"));
    }

    @Test
    public void getByteTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getByte("flag"));
        show(neoMap.getByte("test"));
        show(neoMap.getByte("test2"));
        show(neoMap.getByte("t"));
    }

    @Test
    public void getShortTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getShort("flag"));
        show(neoMap.getShort("test"));
        show(neoMap.getShort("test2"));
        show(neoMap.getShort("t"));
    }

    @Test
    public void getIntegerTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getInteger("flag"));
        show(neoMap.getInteger("test"));
        show(neoMap.getInteger("test2"));
        show(neoMap.getInteger("t"));
    }

    @Test
    public void getLongTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getLong("flag"));
        show(neoMap.getLong("test"));
        show(neoMap.getLong("test2"));
        show(neoMap.getLong("t"));
    }

    @Test
    public void getFloatTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getFloat("flag"));
        show(neoMap.getFloat("test"));
        show(neoMap.getFloat("test2"));
        show(neoMap.getFloat("t"));
    }

    @Test
    public void getDoubleTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.getDouble("flag"));
        show(neoMap.getDouble("test"));
        show(neoMap.getDouble("test2"));
        show(neoMap.getDouble("t"));
    }

    @Test
    public void getTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.get("flag"));
        show(neoMap.get("test"));
        show(neoMap.get("test2"));
        show(neoMap.get("t"));
    }

    @Test
    public void getObjectTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        show(neoMap.get(Integer.class, "flag", 12));
        show(neoMap.get(String.class, "test", "de"));
        show(neoMap.get(Long.class, "test2", 0L));
        show(neoMap.get(Double.class, "t", 0.1));
    }

    @Test
    public void getListTest1(){
        NeoMap neoMap = NeoMap.of("a", Arrays.asList("a", "b", "c"));

        List<String> integerList = neoMap.getList(String.class, "a");
        // [a, b, c]
        show(integerList);
    }

    /**
     * 不是原类型也是可以的
     */
    @Test
    public void getListTest2(){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        NeoMap neoMap = NeoMap.of("a", dataList);

        List<String> integerList = neoMap.getList(String.class, "a");
        //[1, 2, 3]
        show(integerList);
    }

    @Test
    public void getSetTest(){
        Set<String> dataSet = new HashSet<>();
        dataSet.add("1");
        dataSet.add("2");
        dataSet.add("3");
        NeoMap neoMap = NeoMap.of("a", dataSet);
        Set<Integer> stringSet = neoMap.getSet(Integer.class, "a");
        show(stringSet);
    }

    @Test
    public void getNeoMapTest1(){
        NeoMap param = NeoMap.of("a", 1, "b", 2);
        NeoMap data = NeoMap.of("a", param);

        show(data.getNeoMap("a"));
    }

    /**
     * getNeoMap除了返回值可以为NeoMap之外，还可以是普通对象
     */
    @Test
    public void getNeoMapTest2(){
        DemoEntity demoEntity = new DemoEntity().setName("name").setId(12L).setUserName("user");

        NeoMap data = NeoMap.of("a", NeoMap.of("name", "name", "id", 12L, "userName", "user"));

        Assert.assertTrue(demoEntity.equals(data.get(DemoEntity.class, "a")));
    }

    /**
     * getNeoMap除了返回值可以为实体对象外，还可以是NeoMap
     */
    @Test
    public void getNeoMapTest3(){
        TestEntity demoEntity = new TestEntity().setName("name").setId(12L).setUserName("user");

        NeoMap data = NeoMap.of("a", demoEntity);

        NeoMap result = NeoMap.of("name", "name", "id", 12L, "userName", "user");
        Assert.assertTrue(result.equals(data.get(NeoMap.class, "a")));
    }

    @Test
    public void appendTest1(){
        NeoMap neoMap = NeoMap.of();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("a", "1");
        dataMap.put("b", "2");
        dataMap.put("c", "3");
        show(neoMap.append(dataMap));
    }

    @Test
    public void getClassTest1(){
        NeoMap neoMap = NeoMap.of("a", "1", "b", "2");
        Integer result = neoMap.get(Integer.class, "a");
        Assert.assertTrue(result.equals(1));
    }

    @Test
    public void keyChgToOtherTest(){
        NeoMap neoMap = NeoMap.of("dataBaseUser", "a", "userName", "b");
        // {DataBaseUser=a, UserName=b}
        show(neoMap.keyChgFromSmallCamelTo(NamingChg.BIGCAMEL));
    }

    @Test
    public void camelChgTest2(){
        NeoMap neoMap = NeoMap.of("data_user_base", "a", "user_name", "b");
        // {DataBaseUser=a, UserName=b}
        show(neoMap.keyChgToSmallCamelFrom(NamingChg.UNDERLINE));
    }

    @Test
    public void getEnumTest1(){
        NeoMap neoMap = NeoMap.of("enum", EnumEntity.A1);
        Assert.assertEquals(EnumEntity.A1, neoMap.get("enum"));
    }

    /**
     * 对于枚举类型，原值为String也可以获取到枚举类型
     */
    @Test
    public void getEnumTest2(){
        NeoMap neoMap = NeoMap.of("enum", "A1");
        Assert.assertEquals(EnumEntity.A1, neoMap.get(EnumEntity.class, "enum"));
    }

    @Test
    public void andTest(){
        String table1 = "table1";
        String table2 = "table2";
        String table3 = "table3";

        NeoMap result = NeoMap.table(table1).cs("name", "a", "age", 123)
            .and(table2).cs("group", "g1")
            .and(table3).cs("name", "k");

        // table1.`group`=ok, table1.`name`=kk, table2.`age`=123
        show(result);
    }

    @Test
    public void containsKeysTest(){
        Assert.assertTrue(NeoMap.of("a", 1, "b", 2, "c", 3).containsKeys("a", "b"));
        Assert.assertFalse(NeoMap.of("a", 1, "b", 2, "c", 3).containsKeys("a", "d"));
    }

    @Test
    public void assignExceptTest1(){
        show(NeoMap.of("a", 1, "b", 2, "c", 3).assignExcept("a"));
    }

    /**
     * 测试克隆模式
     */
    @Test
    public void cloneTest(){
        NeoMap result = NeoMap.of("a", 1, "b", 2);
        NeoMap cloneMap = result.clone();
        show(cloneMap);
        result.remove("a");
        show(result);
        show(cloneMap);
    }

    @Test
    public void fromMapTest(){
        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("a", 1);
        dataMap.put("b", 2);
        dataMap.put("c", 3);
        show(NeoMap.fromMap(dataMap));
    }

    @Test
    public void getDateTest(){
        NeoMap neoMap = NeoMap.of("t", new Date().getTime());
        show(neoMap.get(Date.class, "t"));
    }

    @Test
    public void putTest(){
        NeoMap neoMap = NeoMap.of();
        neoMap.put("t", new Date());
        show(neoMap.get("t"));
        show(neoMap.getLong("t"));
        show(neoMap.get(Date.class, "t"));
    }

    @Test
    public void valueTypeTest(){
        NeoMap dataMap = NeoMap.of("a", 1, "b", 2);
        Map<String, Integer> integerMap = dataMap.getDataMapAssignValueType(Integer.class);
        show(integerMap);
    }
}
