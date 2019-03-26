package com.simon.neo.neotest;

import com.simon.neo.NeoMap;
import com.simon.neo.entity.DemoEntity;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/26 上午8:35
 */
public class NeoTxTest extends NeoBaseTest {

    public NeoTxTest() throws SQLException {}

    /**
     * 验证普通的处理
     */
    @Test
    public void test1(){
        neo.tx(()->{
            neo.update(TABLE_NAME, NeoMap.of("id", 1, "group", "group111"));
            neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group112"));
            neo.update(TABLE_NAME, NeoMap.of("id", 3, "group", "group113"));
            neo.update(TABLE_NAME, NeoMap.of("id", 4, "group", "group114"));
        });
    }

    /**
     * 验证是否一起提交
     */
    @Test
    public void test2(){
        neo.tx(()->{
            neo.update(TABLE_NAME, NeoMap.of("id", 1, "group", "group21"));
            neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group22"));
            neo.update(TABLE_NAME, NeoMap.of("id", 3, "group", "group23"));
            neo.update(TABLE_NAME, NeoMap.of("id", 4, "group", "group24"));
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            neo.update(TABLE_NAME, NeoMap.of("id", 5, "group", "group25"));
        });
    }

    /**
     * 验证异常情况下的回退，并且不影响其他的执行
     */
    @Test
    public void test3(){
        DemoEntity entity = null;
        neo.tx(()->{
            neo.update(TABLE_NAME, NeoMap.of("id", 1, "group", "group31"));
            neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group32"));
            neo.update(TABLE_NAME, NeoMap.of("id", 3, "group", "group33"));
            neo.update(TABLE_NAME, NeoMap.of("id", 4, "group", "group34"));

            show(entity.getDataBaseName());
            neo.update(TABLE_NAME, NeoMap.of("id", 5, "group", "group35"));
        });
        neo.update(TABLE_NAME, NeoMap.of("id", 32, "group", "group335"));
    }

    /**
     * 验证异常情况下的回退，并且不影响其他的执行
     */
    @Test
    public void test4(){
        show(neo.tx(()->{
            neo.update(TABLE_NAME, NeoMap.of("id", 1, "group", "group31"));
            neo.update(TABLE_NAME, NeoMap.of("id", 2, "group", "group32"));
            neo.update(TABLE_NAME, NeoMap.of("id", 3, "group", "group33"));
            neo.update(TABLE_NAME, NeoMap.of("id", 4, "group", "group34"));
            neo.update(TABLE_NAME, NeoMap.of("id", 5, "group", "group35"));
            return neo.one(TABLE_NAME, NeoMap.of("id", 282));
        }));
    }
}
