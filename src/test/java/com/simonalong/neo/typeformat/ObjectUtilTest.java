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
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/4 下午12:55
 */
public class ObjectUtilTest extends BaseTest {

    @Test
    public void CharTest() {
        //c
        show(ObjectUtil.cast(Character.class, 'c'));
        //a
        show(ObjectUtil.cast(Character.class, "adsf"));
        //1
        show(ObjectUtil.cast(Character.class, 1f));
        //1
        show(ObjectUtil.cast(char.class, 1f));
    }

    @Test
    public void StringTest() {
        //ok
        show(ObjectUtil.cast(String.class, "ok"));
        //1
        show(ObjectUtil.cast(String.class, 1));
        //1.0
        show(ObjectUtil.cast(String.class, 1f));
        //{a=12}
        show(ObjectUtil.cast(String.class, NeoMap.of("a", 12)));
    }

    /**
     * -128 ~ 127
     */
    @Test
    public void ByteTest() {
        // 1
        show(ObjectUtil.cast(Byte.class, 1));
        // 127
        show(ObjectUtil.cast(Byte.class, 127));
        // 127
        show(ObjectUtil.cast(byte.class, 127));
        // -128
        show(ObjectUtil.cast(Byte.class, 128));
        // -57
        show(ObjectUtil.cast(Byte.class, 199));
        // null
//        show(ObjectUtil.cast(Byte.class, "ok"));
    }

    @Test
    public void ShortTest() {
        Short s = 1;
        show(ObjectUtil.cast(Short.class, s));
        show(ObjectUtil.cast(short.class, s));
//        show(ObjectUtil.cast(Short.class, "s"));
    }

    @Test
    public void IntegerTest() {
        Integer s = 1;
        show(ObjectUtil.cast(Integer.class, s));
        show(ObjectUtil.cast(int.class, s));
//        show(ObjectUtil.cast(Integer.class, "s"));
    }

    @Test
    public void LongTest() {
        Long s = 1L;
        show(ObjectUtil.cast(Long.class, s));
        show(ObjectUtil.cast(long.class, s));
        show(ObjectUtil.cast(Long.class, "s"));
    }

    @Test
    public void DoubleTest() {
        Double s = 1D;
        show(ObjectUtil.cast(Long.class, s));
        show(ObjectUtil.cast(long.class, s));
//        show(ObjectUtil.cast(Long.class, "s"));
    }

    @Test
    public void FloatTest() {
        Float s = 1F;
        show(ObjectUtil.cast(Float.class, s));
        show(ObjectUtil.cast(float.class, s));
        show(ObjectUtil.cast(Float.class, "s"));
    }

    @Test
    public void EnumTest() {
        show(ObjectUtil.cast(EnumEntity.class, "T1"));
        show(ObjectUtil.cast(EnumEntity.class, 12));
    }

    @Test
    public void ArrayTest() {
        Integer[] integers = new Integer[]{1, 34, 5, 15};
        // [1,34,5,15]
        show(JSON.toJSONString(ObjectUtil.toArray(integers)));
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
