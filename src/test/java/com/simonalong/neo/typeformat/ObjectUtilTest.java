package com.simonalong.neo.typeformat;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.ValueCastClassException;
import com.simonalong.neo.util.ObjectUtil;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/4 下午12:55
 */
public class ObjectUtilTest extends BaseTest {

    @Test
    public void CharTest() {
        Assert.assertEquals(Character.valueOf('c'), ObjectUtil.cast(Character.class, 'c'));
        Assert.assertEquals(Character.valueOf('a'), ObjectUtil.cast(Character.class, "adsf"));
        Assert.assertEquals(Character.valueOf('1'), ObjectUtil.cast(Character.class, 1f));
        Assert.assertEquals(Character.valueOf('1'), ObjectUtil.cast(char.class, 1f));
    }

    @Test
    public void StringTest() {
        Assert.assertEquals("ok", ObjectUtil.cast(String.class, "ok"));
        Assert.assertEquals("1", ObjectUtil.cast(String.class, 1));
        Assert.assertEquals("1.0", ObjectUtil.cast(String.class, 1f));
        Assert.assertEquals("{a=12}", ObjectUtil.cast(String.class, NeoMap.of("a", 12)));
    }

    /**
     * -128 ~ 127
     */
    @Test
    public void ByteTest() {
        Assert.assertEquals(Byte.valueOf("1"), ObjectUtil.cast(Byte.class, 1));
        Assert.assertEquals(Byte.valueOf("127"), ObjectUtil.cast(Byte.class, 127));
        Assert.assertEquals(Byte.valueOf("127"), ObjectUtil.cast(byte.class, 127));
        Assert.assertEquals(Byte.valueOf("-128"), ObjectUtil.cast(Byte.class, 128));
        Assert.assertEquals(Byte.valueOf("-57"), ObjectUtil.cast(Byte.class, 199));
    }

    @Test
    public void ShortTest() {
        Assert.assertEquals(Short.valueOf("1"), ObjectUtil.cast(Short.class, 1));
        Assert.assertEquals(Short.valueOf("1"), ObjectUtil.cast(short.class, 1));
    }

    @Test
    public void IntegerTest() {
        Assert.assertEquals(Integer.valueOf("1"), ObjectUtil.cast(Integer.class, 1));
        Assert.assertEquals(Integer.valueOf("1"), ObjectUtil.cast(int.class, 1));
    }

    @Test
    public void LongTest() {
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(Long.class, 1L));
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(long.class, 1L));
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(Long.class, 1L));
    }

    @Test
    public void DoubleTest() {
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(Long.class, 1D));
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(long.class, 1D));
        Assert.assertEquals(Long.valueOf("1"), ObjectUtil.cast(Long.class, 1D));
    }

    @Test
    public void FloatTest() {
        Assert.assertEquals(Float.valueOf("1"), ObjectUtil.cast(Float.class, 1F));
        Assert.assertEquals(Float.valueOf("1"), ObjectUtil.cast(float.class, 1F));
        Assert.assertEquals(Float.valueOf("1"), ObjectUtil.cast(Float.class, 1F));
    }

    @Test
    public void EnumTest() {
        Assert.assertEquals(EnumEntity.T1, ObjectUtil.cast(EnumEntity.class, "T1"));
        Assert.assertNull(ObjectUtil.cast(EnumEntity.class, 12));
    }

    @Test
    public void ArrayTest() {
        Integer[] integers = new Integer[]{1, 34, 5, 15};
        // [1,34,5,15]
        ObjectUtil.toArray(integers);
    }

    @Test
    public void ListTest() {
        List<Integer> integerList = Arrays.asList(22, 12, 3, 32, 45);
        show(ObjectUtil.toList(integerList));
    }

    @Test
    public void SetTest() {
        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(1);
        integerSet.add(4);
        integerSet.add(12);
        show(ObjectUtil.toSet(integerSet));
    }

    @Test
    public void QueueTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.add(1);
        arrayDeque.add(4);
        arrayDeque.add(12);
        show(ObjectUtil.toQueue(arrayDeque));
    }

    @Test
    public void CollectionTest() {
        Collection<Integer> arrayDeque = new ArrayList<>();
        arrayDeque.add(1);
        arrayDeque.add(4);
        arrayDeque.add(12);
        show(ObjectUtil.toCollection(arrayDeque));
    }

    @Test
    public void MapTest() {
        Map<Integer, String> dataMap = new HashMap<>();
        dataMap.put(1, "a");
        dataMap.put(2, "b");
        dataMap.put(3, "c");

        show(ObjectUtil.toMap(dataMap));
    }

    @Test
    public void NeoMapTest1() {
        NeoMap neoMap = NeoMap.of("a", 12, "b", "ok");
        show(ObjectUtil.toNeoMap(neoMap));
    }

    @Test
    public void NeoMapTest2() {
        ObjectEntity objectEntity = new ObjectEntity().setName("word").setAge(12);
        show(ObjectUtil.toNeoMap(objectEntity));
    }

    /**
     * 其中一半情况下，tClass都是给数据库生成的字段转换的，因此这里需要处理
     */
    @Test(expected = ValueCastClassException.class)
    public void DateTest1(){
        Date time = new Date();
        ObjectUtil.cast(Timestamp.class, time);
    }
}
