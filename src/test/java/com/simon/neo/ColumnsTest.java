package com.simon.neo;

import com.simon.neo.entity.DemoEntity;
import com.simon.neo.neo.NeoBaseTest;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:26
 */
public class ColumnsTest extends NeoBaseTest {

    public ColumnsTest() throws SQLException {
    }

    @Test
    public void testOf(){
        show(Columns.of("name", "name1"));
    }

    @Test
    public void testFrom(){
        show(Columns.from(DemoEntity.class));
    }

    @Test
    public void buildFieldsTest1(){
        Columns columns = Columns.of("c1", "c2", "c3");
        // `c1`, `c2`, `c3`
        show(columns.buildFields());
    }

    @Test
    public void buildFieldsTest11(){
        Columns columns = Columns.of("c1 as c11", "c2", "c3");
        // `c1` as c11, `c2`, `c3`
        show(columns.buildFields());
    }

    /**
     * 多个列，则进行转换
     */
    @Test
    public void buildFieldsTest12(){
        Columns columns = Columns.of("c1 as c11", "c1", "c3");
        // `c1` as c11, `c3`
        show(columns.buildFields());
    }

    @Test
    public void buildFieldsTest2(){
        Columns columns = Columns.table("tableName", "c1", "c2", "c3");
        // tableName.`c2`, tableName.`c3`, tableName.`c1`
        show(columns.buildFields());
    }

    @Test
    public void buildFieldsTest3(){
        Columns columns = Columns.of("c1", "c2", "c3").add("a1", "a2");
        // `c1`, `c2`, `c3`, `a1`, `a2`
        show(columns.buildFields());
    }

    @Test
    public void buildFieldsTest4(){
        Columns columns = Columns.table("tableName", "c1", "c2", "c3").add("a1", "a2");
        // tableName.`c1`, tableName.`c2`, tableName.`c3`, `a1`, `a2`
        show(columns.buildFields());
    }

    @Test
    public void buildFieldsTest5(){
        String table1 = "table1";
        String table2 = "table2";
        String table3 = "table3";
        Columns columns = Columns.table(table1, "a1", "b1", "c1").and(table2, "a2", "b2", "c2").and(table3, "a3", "b3");

        // table3.`b3`, table1.`b1`, table1.`c1`, table1.`a1`, table2.`c2`, table2.`b2`, table2.`a2`, table3.`a3`
        show(columns.buildFields());
    }

    @Test
    public void removeTest(){
        Columns columns = Columns.of("c1", "c2");
        columns.remove("c2");
        // `c1`
        show(columns);
    }

    @Test
    public void extendTest1(){
        Columns columns = Columns.table("neo_table1", "*", "group").extend(neo);
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        show(columns);
    }

    @Test
    public void extendTest2(){
        Columns columns = Columns.table("neo_table1",  "group").extend(neo);
        // neo_table1.`group`
        show(columns);
    }

}
