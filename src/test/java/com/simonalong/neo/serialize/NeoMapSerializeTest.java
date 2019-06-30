package com.simonalong.neo.serialize;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.BaseTest;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/18 下午6:13
 */
public class NeoMapSerializeTest extends BaseTest {

    /**
     * 测试序列化
     */
    @Test
    public void jsonTest1(){
        show(JSON.toJSONString(NeoMap.of("a", 1)));
    }

    /**
     * 测试反序列化
     */
    @Test
    public void joinTest2(){
        String json = JSON.toJSONString(NeoMap.of("a", 1));
        show(JSON.parseObject(json, NeoMap.class));
    }

    @Test
    @SneakyThrows
    public void jsonTest3(){
        Class<?> clazz = NeoMap.class;
        Map<Object, Object> dataMap = (Map) new NeoMap();
    }
}
