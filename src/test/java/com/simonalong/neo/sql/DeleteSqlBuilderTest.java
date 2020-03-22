package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.DeleteSqlBuilder;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/3/22 下午8:10
 */
public class DeleteSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild() {
        // delete from table1 where `age` =  ? and `name` =  ?
        show(DeleteSqlBuilder.build("table1", NeoMap.of("name", "nana", "age", 12)));
    }
}
