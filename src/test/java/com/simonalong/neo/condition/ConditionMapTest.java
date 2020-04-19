package com.simonalong.neo.condition;

import com.simonalong.neo.ConditionMap;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/4/17 5:10 PM
 */
public class ConditionMapTest extends NeoBaseTest {

    /**
     * 搜索条件根据对应的条件进行拼接
     */
    @Test
    public void test1() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("age", 1);
        searchMap.put("group", "group_test");
        searchMap.put("user_name", "chenzhen");

        ConditionMap conditionMap = ConditionMap.of();
        // 脚本这里是groovy脚本，是一个返回为Boolean类型的返回值，返回为true，则表示该值会被拼接到sql中，否则不计算在内
        conditionMap.put("name", "#current != null && !#current.equals('')");
        conditionMap.put("age", "#current != null && #current > 12");
        conditionMap.put("group", "#current != null && !(#root.age < 10)");

        // 设置过滤条件
        searchMap.setConditionMap(conditionMap);

        Assert.assertTrue(searchMap.satisfyCondition("name"));
        Assert.assertFalse(searchMap.satisfyCondition("age"));
        Assert.assertFalse(searchMap.satisfyCondition("group"));
        Assert.assertTrue(searchMap.satisfyCondition("user_name"));

        show(SqlBuilder.buildWhere(searchMap));
        show(SqlBuilder.buildValueList(searchMap));
    }
}
