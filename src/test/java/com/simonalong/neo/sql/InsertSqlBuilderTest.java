package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.InsertSqlBuilder;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/3/22 下午8:06
 */
public class InsertSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild() {
        // insert into table1 (`age`, `name`) values (?, ?)
        show(InsertSqlBuilder.build("table1", NeoMap.of("name", "nana", "age", 12)));
        show(InsertSqlBuilder.buildInsertValues(NeoMap.of("name", "nana", "age", 12)));
    }
}
