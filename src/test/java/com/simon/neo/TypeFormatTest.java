package com.simon.neo;

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
        // -128
        show(TypeFormat.cast(Byte.class, 128));
        // -57
        show(TypeFormat.cast(Byte.class, 199));
        // null
        show(TypeFormat.cast(Byte.class, "ok"));
    }

    @Test
    public void ShortTest() {

    }

    @Test
    public void IntegerTest() {

    }

    @Test
    public void LongTest() {

    }

    @Test
    public void DoubleTest() {

    }

    @Test
    public void FloatTest() {

    }

    @Test
    public void EnumTest() {

    }

    @Test
    public void ArrayTest() {

    }

    @Test
    public void ListTest() {

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
