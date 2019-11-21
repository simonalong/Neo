package com.simonalong.neo.tx;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import com.simonalong.neo.sql.TxIsolationEnum;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
                Thread.sleep(3 * 1000);
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

    /**
     * 只读事务
     */
    @Test
    public void test5(){
        AtomicReference<List<String>> groupList = new AtomicReference<>();
        AtomicReference<List<String>> nameList = new AtomicReference<>();
        neo.tx(true, ()->{
            groupList.set(neo.values(TABLE_NAME, "group"));
            nameList.set(neo.values(TABLE_NAME, "name"));
        });

        // [12, group555]
        show(groupList);
        // [name333]
        show(nameList);
    }

    /**
     * 事务的隔离级别
     */
    @Test
    public void test6(){
        // {age=2, group=kk, id=11, name=name333}
        show(neo.tx(TxIsolationEnum.TX_R_R, ()->{
            neo.update(TABLE_NAME, NeoMap.of("group", "kk"), NeoMap.of("id", 11));
            return neo.one(TABLE_NAME, NeoMap.of("id", 11));
        }));
    }
}
