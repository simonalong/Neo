package com.simon.neo;

import com.alibaba.fastjson.JSON;
import com.sun.tools.javac.code.Attribute.Array;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/4 下午12:55
 */
public class TypeFormatTest extends BaseTest{

    @Test
    public void CharTest() {
        //c
        show(TypeFormat.cast(Character.class, 'c'));
        //a
        show(TypeFormat.cast(Character.class, "adsf"));
        //1
        show(TypeFormat.cast(Character.class, 1f));
        //1
        show(TypeFormat.cast(char.class, 1f));
    }

    @Test
    public void StringTest() {
        //ok
        show(TypeFormat.cast(String.class, "ok"));
        //1
        show(TypeFormat.cast(String.class, 1));
        //1.0
        show(TypeFormat.cast(String.class, 1f));
        //{a=12}
        show(TypeFormat.cast(String.class, NeoMap.of("a", 12)));
    }

    /**
     * -128 ~ 127
     */
    @Test
    public void ByteTest() {
        // 1
        show(TypeFormat.cast(Byte.class, 1));
        // 127
        show(TypeFormat.cast(Byte.class, 127));
        // 127
        show(TypeFormat.cast(byte.class, 127));
        // -128
        show(TypeFormat.cast(Byte.class, 128));
        // -57
        show(TypeFormat.cast(Byte.class, 199));
        // null
        show(TypeFormat.cast(Byte.class, "ok"));
    }

    @Test
    public void ShortTest() {
        Short s = 1;
        show(TypeFormat.cast(Short.class, s));
        show(TypeFormat.cast(short.class, s));
        show(TypeFormat.cast(Short.class, "s"));
    }

    @Test
    public void IntegerTest() {
        Integer s = 1;
        show(TypeFormat.cast(Integer.class, s));
        show(TypeFormat.cast(int.class, s));
        show(TypeFormat.cast(Integer.class, "s"));
    }

    @Test
    public void LongTest() {
        Long s = 1L;
        show(TypeFormat.cast(Long.class, s));
        show(TypeFormat.cast(long.class, s));
        show(TypeFormat.cast(Long.class, "s"));
    }

    @Test
    public void DoubleTest() {
        Double s = 1D;
        show(TypeFormat.cast(Long.class, s));
        show(TypeFormat.cast(long.class, s));
        show(TypeFormat.cast(Long.class, "s"));
    }

    @Test
    public void FloatTest() {
        Float s = 1F;
        show(TypeFormat.cast(Float.class, s));
        show(TypeFormat.cast(float.class, s));
        show(TypeFormat.cast(Float.class, "s"));
    }

    @Test
    public void EnumTest() {
        show(TypeFormat.cast(EnumEntity.class, "T1"));
        show(TypeFormat.cast(EnumEntity.class, 12));
    }

    @Test
    public void ArrayTest() {
        Integer[] integers = new Integer[]{1, 34, 5, 15};
        // [1,34,5,15]
        show(JSON.toJSONString(TypeFormat.toArray(integers)));
    }

    @Test
    public void ListTest() {
        List<Integer> integerList = Arrays.asList(22, 12, 3, 32, 45);
        show(TypeFormat.toList(integerList));
    }

    @Test
    public void SetTest() {

    }

    @Test
    public void QueueTest() {

    }

    @Test
    public void CollectionTest() {

    }

    @Test
    public void MapTest() {

    }

    @Test
    public void NeoMapTest() {

    }
}
