package com.simonalong.neo.operate;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.Express;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/8/31 10:52 上午
 */
public class NeoExpressTest extends NeoBaseTest {

    /**
     * 复杂表达式的搜索
     * <p>其中有默认的过滤原则
     */
    @Test
    public void oneTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.one("neo_table1", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "");
        show(neo.one("neo_table1", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", null);
        show(neo.one("neo_table1", searchExpress));
    }

    @Test
    public void listTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.list("neo_table1", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "");
        show(neo.list("neo_table1", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", null);
        show(neo.list("neo_table1", searchExpress));

        // select `id` from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", null);
        show(neo.list("neo_table1", Columns.of("id"), searchExpress));
    }

    @Test
    public void valueTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.value("neo_table1", "id", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "group12");
        show(neo.value(Long.class, "neo_table1", "id", searchExpress));
    }

    @Test
    public void valuesTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.values("neo_table1", "id", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "group12");
        show(neo.values(Long.class, "neo_table1", "id", searchExpress));
    }

    @Test
    public void pageTest() {
        Express searchExpress;

        // todo page
    }

    @Test
    public void countTest() {
        Express searchExpress;

        // todo page
    }

    @Test
    public void existsTest() {
        Express searchExpress;

        // todo page
    }

}
