package com.simon.neo;

import com.simon.neo.entity.DemoEntity;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:26
 */
public class ColumnsTest extends BaseTest{

    @Test
    public void testOf(){
        show(Columns.of("name", "name1"));
    }

    @Test
    public void testFrom(){
        show(Columns.from(DemoEntity.class));
    }
    
    @Test
    public void buildFieldsTest(){
        Columns columns = Columns.of("c1", "c2", "c3");
        show(columns.buildFields("table1"));
    }

    @Test
    public void buildFieldsTest2(){
        Columns columns = Columns.name("tableName").columns("c1", "c2", "c3");
        show(columns.buildFields());
        show(columns.buildFields("table1"));
    }
}
