package com.simonalong.neo.express;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.util.LocalDateTimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;


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

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.page("neo_table1", searchExpress, NeoPage.of(1, 10)));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "group12");
        show(neo.page("neo_table1", Columns.of("id"), searchExpress, NeoPage.of(1, 10)));
    }

    @Test
    public void getPageTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.getPage("neo_table1", searchExpress, NeoPage.of(0, 10)));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12", "group", "group12");
        show(neo.getPage("neo_table1", Columns.of("id"), searchExpress, NeoPage.of(1, 10)));
    }

    @Test
    public void countTest() {
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", "name12");
        show(neo.count("neo_table1", searchExpress));
    }

    @Test
    public void existsTest() {
        show(LocalDateTimeUtil.localDateTimeToString(LocalDateTime.now(), LocalDateTimeUtil.yMdHms));
        show(LocalDateTimeUtil.localDateTimeToString(LocalDateTime.now(ZoneId.systemDefault()), LocalDateTimeUtil.yMdHms));
    }

}
