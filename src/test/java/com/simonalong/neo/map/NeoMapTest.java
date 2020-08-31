package com.simonalong.neo.map;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.entity.EnumEntity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simonalong.neo.exception.NeoMapChgException;
import com.simonalong.neo.map.table.NeoMapEnum;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.util.Maps;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:49
 */
public class NeoMapTest extends BaseTest {

    /**
     * 测试of
     */
    @Test
    public void testOf1(){
        NeoMap neoMap1 = NeoMap.of("a", 123, "b", 12);

        Map<String, Object> expect = Maps.of().add("a", 123).add("b", 12).build();
        // {"a":123,"b":12}
        Assert.assertEquals(expect, neoMap1.getDataMap());
    }

    /**
     * 测试，如果有value为null的情况，则两个NeoMap相等
     */
    @Test
    public void testEqual1(){
        NeoMap neoMap1 = NeoMap.of("a", 123, "b", 12);
        NeoMap neoMap2 = NeoMap.of("a", 123, "b", 12);
        NeoMap neoMap3 = NeoMap.of("a", 123, "b", 12, "c", null);

        Assert.assertEquals(neoMap1, neoMap2);
        Assert.assertEquals(neoMap1, neoMap3);
    }

    /**
     * 测试，如果有value为null的情况，而且设置了允许value为空
     */
    @Test
    public void testEqual2(){
        NeoMap neoMap1 = NeoMap.of("a", 123, "b", 12);
        NeoMap neoMap2 = NeoMap.of("a", 123, "b", 12);
        NeoMap neoMap3 = NeoMap.of();
        neoMap3.setSupportValueNull(true);
        neoMap3.put("a", 123);
        neoMap3.put("b", 12);
        neoMap3.put("c", null);

        Assert.assertEquals(neoMap1, neoMap2);
        Assert.assertNotEquals(neoMap1, neoMap3);
    }

    /**
     * 测试append
     */
    @Test
    public void testAppend1(){
        NeoMap neoMap1 = NeoMap.of("a", 123);
        NeoMap neoMap2 = NeoMap.of("b", 123);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        // {"a":123,"b":123}
        Map<String, Object> expect = Maps.of().add("a", 123).add("b", 123).build();
        Assert.assertEquals(expect, data.getDataMap());
    }

    /**
     * 测试append：数据覆盖
     */
    @Test
    public void testAppend2(){
        NeoMap neoMap1 = NeoMap.of("a", 111);
        NeoMap neoMap2 = NeoMap.of("a", 222);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        // {"a":222}
        Map<String, Object> expect = Maps.of().add("a", 222).build();
        Assert.assertEquals(expect, data.getDataMap());
    }

    /**
     * 测试as：key为实体的属性名，对实体而言默
     */
    @Test
    public void testAs1() {
        NeoMap neoMap = NeoMap.of("name", "name1", "user_name", "userName1");
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setName("name1");
        expect.setUserName("userName1");
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试as：配置全局转换规则
     */
    @Test
    public void testAs2() {
        NeoMap.setGlobalNaming(NamingChg.MIDDLELINE);
        // 注意其中有个属性是为中划线的
        NeoMap neoMap = NeoMap.of("name", "name1", "user-name", "userName1");
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setName("name1");
        expect.setUserName("userName1");
        Assert.assertEquals(expect, neoMapAsEntity);
        NeoMap.setGlobalNaming(NamingChg.DEFAULT);
    }

    /**
     * 测试as：配置as转换时候的转换规则
     */
    @Test
    public void testAs3() {
        // 注意其中有个属性是为中划线的
        NeoMap neoMap = NeoMap.of("name", "name1", "user-name", "userName1");
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class, NamingChg.MIDDLELINE);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setName("name1");
        expect.setUserName("userName1");
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试as：配置neoMap单独的转化规则
     */
    @Test
    public void testAs4() {
        // 注意其中有个属性是为中划线的
        NeoMap neoMap = NeoMap.of("name", "name1", "user-name", "userName1");
        neoMap.setNamingChg(NamingChg.MIDDLELINE);
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setName("name1");
        expect.setUserName("userName1");
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试as：类型兼容转换
     */
    @Test
    public void testAs5() {
        Date now = new Date();
        NeoMap neoMap = NeoMap.of("age", 12L, "time", now.getTime());
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setAge(12);
        expect.setTime(now);
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试as：有@Column注解时候，按照注解中的值作为NeoMap中的key
     */
    @Test
    public void testAs6() {
        NeoMap neoMap = NeoMap.of("friend_name", "nana");
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setMyFriendName("nana");
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试as：有@Column注解时候，按照注解中的值作为NeoMap中的key，无视转换规则
     */
    @Test
    public void testAs6_1() {
        NeoMap neoMap = NeoMap.of("friend_name", "nana");
        neoMap.setNamingChg(NamingChg.MIDDLELINE);
        NeoMapAsEntity neoMapAsEntity = neoMap.as(NeoMapAsEntity.class);

        NeoMapAsEntity expect = new NeoMapAsEntity();
        expect.setMyFriendName("nana");
        Assert.assertEquals(expect, neoMapAsEntity);
    }

    /**
     * 测试from：转换规则同as，默认key不转换
     */
    @Test
    public void testFrom1(){
        NeoMapFromEntity fromEntity = new NeoMapFromEntity();
        fromEntity.setName("name");
        fromEntity.setUserName("username1");

        NeoMap neoMap = NeoMap.from(fromEntity);
        // {"name":"name","userName":"username1"}
        show(neoMap);

        NeoMapFromEntity expect = new NeoMapFromEntity();
        expect.setName("name");
        expect.setUserName("username1");
        Assert.assertEquals(expect, neoMap.as(NeoMapFromEntity.class));
    }

    /**
     * 测试from：设置只转换的属性
     */
    @Test
    public void testFrom2(){
        NeoMapFromEntity demo = new NeoMapFromEntity();
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setAge(12);

        NeoMap neoMap = NeoMap.fromInclude(demo, "name", "age");
        // {"age":12,"name":"name1"}
        show(neoMap);

        NeoMapFromEntity expect = new NeoMapFromEntity();
        expect.setName("name1");
        expect.setAge(12);
        Assert.assertEquals(expect, neoMap.as(NeoMapFromEntity.class));
    }

    /**
     * 测试from：排除指定属性
     */
    @Test
    public void testFrom3(){
        NeoMapFromEntity demo = new NeoMapFromEntity();
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setAge(12);

        NeoMap neoMap = NeoMap.fromExclude(demo, "userName");
        // {"age":12,"name":"name1"}
        show(neoMap);

        NeoMapFromEntity expect = new NeoMapFromEntity();
        expect.setName("name1");
        expect.setAge(12);
        Assert.assertEquals(expect, neoMap.as(NeoMapFromEntity.class));
    }

    /**
     * 测试from：排除指定属性
     */
    @Test
    public void testFrom3_1(){
        NeoMapFromEntity demo = new NeoMapFromEntity();
        demo.setName("name1");
        demo.setMyFriendName("nana");
        demo.setAge(12);

        NeoMap neoMap = NeoMap.fromExclude(demo, "myFriendName");
        // {"age":12,"friend_name":"nana","name":"name1"}
        show(neoMap);

        NeoMapFromEntity expect = new NeoMapFromEntity();
        expect.setName("name1");
        expect.setAge(12);
        Assert.assertEquals(expect, neoMap.as(NeoMapFromEntity.class));
    }

    /**
     * 测试：from：基本类型转换失败
     */
    @Test(expected = NeoMapChgException.class)
    public void testFrom4_1(){
        NeoMap neoMap = NeoMap.from(1);
        // {"name":"name","userName":"username1"}
        show(neoMap);
    }

    /**
     * 测试：from：基本类型转换失败
     */
    @Test(expected = NeoMapChgException.class)
    public void testFrom4_2(){
        NeoMap neoMap = NeoMap.from(NeoMapEnum.TEST1);
        // {"name":"name","userName":"username1"}
        show(neoMap);
    }

    /**
     * 测试from：带有自定义转换规则
     */
    @Test
    public void testFrom5() {
        NeoMapFromEntity demo = new NeoMapFromEntity();
        demo.setName("name1");
        demo.setMyFriendName("nana");
        demo.setUserName("username1");

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.fromInclude(demo, NamingChg.MIDDLELINE, "userName", "myFriendName");
        // {"friend_name":"nana","user-name":"username1"}
        show(neoMap);

        NeoMapFromEntity expect = new NeoMapFromEntity();
        expect.setUserName("username1");
        expect.setMyFriendName("nana");
        Assert.assertEquals(expect, neoMap.as(NeoMapFromEntity.class, NamingChg.MIDDLELINE));
    }

    /**
     * 测试from：指定Columns
     * <p>
     *     来自实体转换的，则其中的key为实体的属性名字，是经过转换的，如果有@Column则会根据@Column的配置进行转换
     */
    @Test
    public void testFrom6() {
        NeoMapFromEntity demo = new NeoMapFromEntity();
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setMyFriendName("nana");

        // 对于of中的数据，要转换实体，是要走转换规则和@Column的
        NeoMap expect = NeoMap.of("name", "name1", "user_name", "userName1", "friend_name", "nana");
        Assert.assertEquals(expect, NeoMap.from(demo, "name", "userName", "myFriendName"));
        Assert.assertEquals(expect, NeoMap.fromInclude(demo, "name", "userName", "myFriendName"));
    }

    /**
     * 测试from：转换规则
     */
    @Test
    public void testFrom7() {
        // 即使这里添加了全局转化规则，但是对于局部规则转换这里是不生效的
        NeoMap.setGlobalNaming(NamingChg.UNDERLINE);
        NeoMapFromEntity2 demo = new NeoMapFromEntity2();
        demo.setName("name1");
        demo.setUserName("username1");

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, NamingChg.DEFAULT);
        // {"group":"group1","user_name":"userName1"}
        show(neoMap);
        NeoMap expectMap = NeoMap.of("name", "name1", "userName", "username1");
        Assert.assertEquals(expectMap, neoMap);
    }

    /**
     * 测试from：这里是fromMap，从另外一个NeoMap获取数据
     */
    @Test
    public void testFrom8(){
        NeoMap sourceMap = NeoMap.of("group", "group1", "user_name", "userName1");

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.fromMap(sourceMap, NamingChg.UNDERLINE);
        // {"group":"group1","user_name":"userName1"}
        show(neoMap);
        Map<String, Object> expect = Maps.of().add("group", "group1").add("user_name", "userName1").build();
        Assert.assertEquals(expect, neoMap.getDataMap());
    }

    /**
     * 测试from：时间类型的转换
     */
    @Test
    public void testFrom9(){
        Date now = new Date();
        NeoMapFromEntity demoEntity = new NeoMapFromEntity()
            .setSqlDate(new java.sql.Date(now.getTime()))
            .setTime(new Time(now.getTime()))
            .setTimestamp(new Timestamp(now.getTime()))
            .setUtilDate(now);

        NeoMap neoMap = NeoMap.from(demoEntity);
        // {"sl":0,"sqlDate":1569988689412,"time":1569988689412,"timestamp":1569988689412,"utilDate":1569988689412}
        Assert.assertEquals(demoEntity, neoMap.as(NeoMapFromEntity.class));
    }

    /**
     * 测试from：若object类型为{@code Map<String, ?>}，则from和fromMap是一样的
     */
    @Test
    public void testFrom11(){
        NeoMap map1 = NeoMap.of("a", 123, "b", 4332);
        show(NeoMap.from(map1));
        show(NeoMap.fromMap(map1));
        Assert.assertEquals(NeoMap.from(map1), NeoMap.fromMap(map1));
    }

    /**
     * 测试assign
     */
    @Test
    public void testAssign1(){
        NeoMap neoMap1 = NeoMap.of("a", "1", "b", "2", "c", "3");
        NeoMap neoMapResult = neoMap1.assign(Columns.of("a", "c"));

        NeoMap neoMap2 = NeoMap.of("a", "1", "c", "3");
        Assert.assertEquals(neoMap2, neoMapResult);
    }

    /**
     * 测试assign：
     */
    @Test
    public void testAssign2(){
        NeoMap neoMap1 = NeoMap.of("a", "1", "b", "2", "c", "3");
        NeoMap neoMapResult = neoMap1.assign("a", "c");

        NeoMap neoMap2 = NeoMap.of("a", "1", "c", "3");
        Assert.assertEquals(neoMap2, neoMapResult);
    }

    @Test
    public void getBooleanTest(){
        NeoMap neoMap = NeoMap.of("flag", "true", "test", "asdf", "test2", 2321);

        Assert.assertTrue(neoMap.getBoolean("flag"));
        Assert.assertFalse(neoMap.getBoolean("test"));
        Assert.assertFalse(neoMap.getBoolean("test2"));
    }

    @Test
    public void getCharacterTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);

        // a
        show(neoMap.getChar("flag"));
        // d
        show(neoMap.getChar("test"));
        // 1
        show(neoMap.getChar("test2"));
        // 1
        show(neoMap.getChar("t"));
    }

    @Test
    public void getByteTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
        // 异常
        // show(neoMap.getByte("flag"));
        // 异常
        // show(neoMap.getByte("test"));
        show(neoMap.getByte("test2"));
        // 异常
        show(neoMap.getByte("t"));
    }

    @Test
    public void getShortTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
        show(neoMap.getShort("flag"));
        show(neoMap.getShort("test"));
        show(neoMap.getShort("test2"));
        show(neoMap.getShort("t"));
    }

    @Test
    public void getIntegerTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
        // 异常
//        show(neoMap.getInteger("flag"));
        // 异常
//        show(neoMap.getInteger("test"));
        Assert.assertTrue(12 == neoMap.getInteger("test2"));
        Assert.assertTrue(12.0f == neoMap.getInteger("t"));
    }

    @Test
    public void getLongTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
        // 异常
//        show(neoMap.getLong("flag"));
        // 异常
//        show(neoMap.getLong("test"));
        Assert.assertEquals(12, (long) neoMap.getLong("test2"));
        Assert.assertEquals(12.0f, neoMap.getLong("t"), 0.0);
    }

    @Test
    public void getFloatTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
        // 异常
//        show(neoMap.getFloat("flag"));
        // 异常
//        show(neoMap.getFloat("test"));
        show(neoMap.getFloat("test2"));
        show(neoMap.getFloat("t"));
        show(neoMap.getLong("t"));
        Assert.assertEquals(12, neoMap.getFloat("test2"), 0.0);
        Assert.assertEquals(12.0f, neoMap.getFloat("t"), 0.0);

    }

    @Test
    public void getDoubleTest(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
//        show(neoMap.getDouble("flag"));
//        show(neoMap.getDouble("test"));
        Assert.assertEquals(12, neoMap.getDouble("test2"), 0.0);
        Assert.assertEquals(12.0f, neoMap.getDouble("t"), 0.0);
    }

    @Test
    public void getTest1(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", 12.0f);
//        show(neoMap.get("flag"));
//        show(neoMap.get("test"));
        Assert.assertEquals(12, neoMap.get("test2"));
        Assert.assertEquals(12.0f, neoMap.get("t"));
    }

    @Test
    public void getTest2(){
        NeoMap neoMap = NeoMap.of("flag", 'a', "test", "d", "test2", 12, "t", "");
        // show(neoMap.get(String.class, "test"));
        Assert.assertEquals(12L, neoMap.get(Long.class, "test2"), 0.0);
        // show(neoMap.get(Double.class, "t"));
    }

    /**
     * get测试：兼容类型也可以
     */
    @Test
    public void getTest3(){
        NeoMap neoMap = NeoMap.of("a", "1", "b", "2");
        Integer result = neoMap.get(Integer.class, "a");
        Assert.assertEquals(1, (int) result);
    }

    /**
     * get测试：测试枚举
     */
    @Test
    public void getTest4(){
        NeoMap neoMap = NeoMap.of("enum", EnumEntity.A1);
        Assert.assertEquals(EnumEntity.A1, neoMap.get("enum"));
    }

    /**
     * get测试：测试枚举。对于枚举类型，原值为String也可以获取到枚举类型
     */
    @Test
    public void getTest4_1(){
        NeoMap neoMap = NeoMap.of("enum", "A1");
        Assert.assertEquals(EnumEntity.A1, neoMap.get(EnumEntity.class, "enum"));
    }

    /**
     * get测试：类型兼容，放置的是数字，转换为时间，会自动转换
     */
    @Test
    public void getDateTest(){
        Date now = new Date();
        NeoMap neoMap = NeoMap.of("t", now.getTime());
        Assert.assertEquals(now, neoMap.get(Date.class, "t"));
    }

    /**
     * 测试getList
     */
    @Test
    public void getListTest1(){
        NeoMap neoMap = NeoMap.of("a", Arrays.asList("a", "b", "c"));

        List<String> expect = new ArrayList<>();
        expect.add("a");
        expect.add("b");
        expect.add("c");
        Assert.assertEquals(expect, neoMap.getList(String.class, "a"));
    }

    /**
     * 测试getList：不是原类型也是可以的
     */
    @Test
    public void getListTest2(){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        NeoMap neoMap = NeoMap.of("a", dataList);

        List<String> expect = new ArrayList<>();
        expect.add("1");
        expect.add("2");
        expect.add("3");
        Assert.assertEquals(expect, neoMap.getList(String.class, "a"));
    }

    /**
     * 测试getNeoMap：
     */
    @Test
    public void getNeoMapTest1() {
        NeoMap param = NeoMap.of("a", 1, "b", 2);
        NeoMap data = NeoMap.of("a", param);

        NeoMap expect = NeoMap.of("a", 1, "b", 2);
        Assert.assertEquals(expect, data.getNeoMap("a"));
    }

    /**
     * 测试getNeoMap：getNeoMap除了返回值可以为NeoMap之外，还可以是普通对象
     */
    @Test
    public void getNeoMapTest2() {
        NeoMapGetNeoMapEntity demoEntity = new NeoMapGetNeoMapEntity().setName("name").setAge(12).setUserName("user");
        NeoMap data = NeoMap.of("a", NeoMap.of("name", "name", "age", 12L, "user_name", "user"));
        Assert.assertEquals(demoEntity, data.get(NeoMapGetNeoMapEntity.class, "a"));
    }

    /**
     * get测试：类型如果为NeoMap，但是数据为实体，也是可以的
     */
    @Test
    public void getNeoMapTest3(){
        NeoMapGetNeoMapEntity demoEntity = new NeoMapGetNeoMapEntity().setName("name").setAge(12).setUserName("user");

        NeoMap data = NeoMap.of("a", demoEntity);
        NeoMap expect = NeoMap.of("name", "name", "age", 12, "user_name", "user");
        Assert.assertEquals(expect, data.get(NeoMap.class, "a"));
    }

    /**
     * append测试：
     */
    @Test
    public void appendTest1(){
        NeoMap neoMap = NeoMap.of();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("a", "1");
        dataMap.put("b", "2");
        dataMap.put("c", "3");
        neoMap.append(dataMap);

        NeoMap expect = NeoMap.of("a", "1", "b", "2", "c", "3");

        Assert.assertEquals(expect, neoMap);
    }

    /**
     * key转换测试：
     */
    @Test
    public void keyChgToOtherTest(){
        NeoMap neoMap = NeoMap.of("dataBaseUser", "a", "userName", "b");

        NeoMap expect1 = NeoMap.of("DataBaseUser", "a", "UserName", "b");
        NeoMap expect2 = NeoMap.of("data_base_user", "a", "user_name", "b");
        Assert.assertEquals(expect1, neoMap.keyChgFromSmallCamelTo(NamingChg.BIGCAMEL));
        Assert.assertEquals(expect2, neoMap.keyChgFromSmallCamelTo(NamingChg.UNDERLINE));
    }

    @Test
    public void containsKeysTest(){
        Assert.assertTrue(NeoMap.of("a", 1, "b", 2, "c", 3).containsKeys("a", "b"));
        Assert.assertFalse(NeoMap.of("a", 1, "b", 2, "c", 3).containsKeys("a", "d"));
    }

    @Test
    public void assignExceptTest1(){
        Assert.assertEquals(NeoMap.of("a", 1), NeoMap.of("a", 1, "b", 2, "c", 3).assignExcept("b", "c"));
    }

    @Test
    public void assignExceptTest2(){
        Assert.assertEquals(NeoMap.of("a", 1), NeoMap.of("a", 1, "b", 2, "c", 3).assignExcept(Columns.of("b", "c")));
    }

    /**
     * clone测试：
     */
    @Test
    public void cloneTest1(){
        NeoMap result = NeoMap.of("a", 1, "b", 2);
        NeoMap cloneMap = result.clone();

        result.remove("a");
        NeoMap expect = NeoMap.of("b", 2);
        Assert.assertEquals(expect, result);


        NeoMap expect2 = NeoMap.of("a", 1, "b", 2);
        Assert.assertEquals(expect2, cloneMap);
    }

    /**
     * clone测试：
     */
    @Test
    public void cloneTest2(){
        NeoMap result = NeoMap.of("a", 1, "b", 2);
        NeoMap cloneMap = result.clone();
        Assert.assertNotSame(result.getDataMap(), cloneMap.getDataMap());
        Assert.assertSame(result.getDataMap(), result.getDataMap());
    }

    @Test
    public void appendTest(){
        Date now = new Date();
        NeoMap neoMap = NeoMap.of().append("t", now);

        Assert.assertEquals(now.getTime(), neoMap.getLong("t").longValue());
        Assert.assertEquals(now, neoMap.get(Date.class, "t"));
    }

    /**
     * value类型转换测试：将value的类型设置为指定的类型
     */
    @Test
    public void valueTypeTest(){
        NeoMap dataMap = NeoMap.of("a", 1, "b", 2);

        Map<String, Integer> expect = new HashMap<>();
        expect.put("a", 1);
        expect.put("b", 2);
        Assert.assertEquals(expect, dataMap.getDataMapAssignValueType(Integer.class));
    }

    /**
     * asArray测试：
     */
    @Test
    public void asArrayTest1(){
        NeoMap neoMap1 = NeoMap.of("age", 12, "data_user", "ok1");
        NeoMap neoMap2 = NeoMap.of("age", 13, "data_user", "ok2");
        NeoMap neoMap3 = NeoMap.of("age", 14, "data_user", "ok3");
        List<NeoMap> dataMap = Arrays.asList(neoMap1, neoMap2, neoMap3);

        List<NeoMapEntity> expectList = new ArrayList<>();
        NeoMapEntity demo1 = new NeoMapEntity().setAge(12).setDataNameUser("ok1");
        NeoMapEntity demo2 = new NeoMapEntity().setAge(13).setDataNameUser("ok2");
        NeoMapEntity demo3 = new NeoMapEntity().setAge(14).setDataNameUser("ok3");
        expectList.add(demo1);
        expectList.add(demo2);
        expectList.add(demo3);

        Assert.assertEquals(expectList, NeoMap.asArray(dataMap, NeoMapEntity.class));
    }

    /**
     * fromArray测试：将实体集合设置为NeoMap集合
     */
    @Test
    public void fromArrayTest1(){
        NeoMapEntity entity1 = new NeoMapEntity().setAge(11).setDataNameUser("ok1");
        NeoMapEntity entity2 = new NeoMapEntity().setAge(12).setDataNameUser("ok2");
        NeoMapEntity entity3 = new NeoMapEntity().setAge(13).setDataNameUser("ok3");
        List<NeoMapEntity> entityList = Arrays.asList(entity1, entity2, entity3);

        List<NeoMap> expectList = new ArrayList<>();
        NeoMap demo1 = NeoMap.of().append("age", 11).append("data_user", "ok1").append("user_address", null).append("name", null);
        NeoMap demo2 = NeoMap.of().append("age", 12).append("data_user", "ok2").append("user_address", null).append("name", null);
        NeoMap demo3 = NeoMap.of().append("age", 13).append("data_user", "ok3").append("user_address", null).append("name", null);
        expectList.add(demo1);
        expectList.add(demo2);
        expectList.add(demo3);

        Assert.assertEquals(expectList, NeoMap.fromArray(entityList));
    }

    /**
     * fromArray测试：可以返回指定的列
     */
    @Test
    public void fromArrayTest2(){
        NeoMapEntity entity1 = new NeoMapEntity().setAge(11).setDataNameUser("ok1");
        NeoMapEntity entity2 = new NeoMapEntity().setAge(12).setDataNameUser("ok2");
        NeoMapEntity entity3 = new NeoMapEntity().setAge(13).setDataNameUser("ok3");
        List<NeoMapEntity> entityList = Arrays.asList(entity1, entity2, entity3);
        show(NeoMap.fromArray(entityList, Columns.of("age")));

        List<NeoMap> expectList = new ArrayList<>();
        expectList.add(NeoMap.of("age", 11));
        expectList.add(NeoMap.of("age", 12));
        expectList.add(NeoMap.of("age", 13));

        Assert.assertEquals(expectList, NeoMap.fromArray(entityList, Columns.of("age")));
    }

    /**
     * fromArray测试：可以返回指定的列
     */
    @Test
    public void fromArrayTest2_1(){
        NeoMapEntity entity1 = new NeoMapEntity().setAge(11).setDataNameUser("ok1");
        NeoMapEntity entity2 = new NeoMapEntity().setAge(12).setDataNameUser("ok2");
        NeoMapEntity entity3 = new NeoMapEntity().setAge(13).setDataNameUser("ok3");
        List<NeoMapEntity> entityList = Arrays.asList(entity1, entity2, entity3);

        List<NeoMap> expectList = new ArrayList<>();
        expectList.add(NeoMap.of("data_user", "ok1"));
        expectList.add(NeoMap.of("data_user", "ok2"));
        expectList.add(NeoMap.of("data_user", "ok3"));

        // 其中columns的参数必须为实体的列名
        Assert.assertEquals(expectList, NeoMap.fromArray(entityList, Columns.of("dataNameUser")));
    }

    /**
     * dbToJavaStr测试：表列信息向java的写法转换
     */
    @Test
    public void dbToJavaStrTest1() {
        NeoMap.setGlobalNaming(NamingChg.UNDERLINE);
        // dataUser
        show(NeoMap.dbToJavaStr("data_user"));
    }

    /**
     * dbToJavaStr测试：表列信息向java的写法转换，转换规则可以放函数中，作为一个参数
     */
    @Test
    public void dbToJavaStrTest2() {
        // dataUser
        show(NeoMap.dbToJavaStr("data-user", NamingChg.MIDDLELINE));
    }

    /**
     * isEmpty测试：
     */
    @Test
    public void isEmptyTest(){
        List<NeoMap> neoMaps = Arrays.asList(NeoMap.of("a", 12), NeoMap.of("b", "ok"));
        Assert.assertFalse(NeoMap.isEmpty(neoMaps));
        List<NeoMap> neoMap2 = Arrays.asList(NeoMap.of(), NeoMap.of());
        Assert.assertTrue(NeoMap.isEmpty(neoMap2));
    }

    /**
     * keyStream测试：
     */
    @Test
    public void keyStream(){
        NeoMap neoMap = NeoMap.of("a", 12, "b", "ok");
        neoMap.keyStream().forEach(this::show);
    }

    @Test
    public void gsonTest(){
        NeoMapEntity entity = new NeoMapEntity();
        entity.setAge(12);
        entity.setUserAddress("nihao");

        NeoMap dataMap = NeoMap.from(entity);
        String gsonString = dataMap.toGsonString();

        Assert.assertEquals(entity, NeoMap.fromGsonStr(gsonString).as(NeoMapEntity.class));
    }

    @Test
    public void fastJsonTest(){
        NeoMapEntity entity = new NeoMapEntity();
        entity.setAge(12);
        entity.setUserAddress("nihao");

        NeoMap dataMap = NeoMap.from(entity);
        String gsonString = dataMap.toFastJsonString();

        Assert.assertEquals(entity, NeoMap.fromFastJsonStr(gsonString).as(NeoMapEntity.class));
    }


    @Test
    public void testSqlBuilder(){
        NeoMap dataMap = NeoMap.of();
        dataMap.setSupportValueNull(true);
        dataMap.put("k1", null);
        dataMap.put("k2", 12);

        show(SqlBuilder.buildWhere(dataMap));
        show(SqlBuilder.buildValueList(dataMap));
    }

    /**
     * 就是key是按照顺序进行排序的
     */
    @Test
    public void sortTest(){
        NeoMap dataMap = NeoMap.of();
        dataMap.openSorted();
        dataMap.put("a", 1);
        dataMap.put("b", 2);
        dataMap.put("2", 3);
        dataMap.put("1", 3);
        dataMap.put("c", 4);
        dataMap.put("d", 5);
        show(dataMap.toFastJsonString());
    }
}
