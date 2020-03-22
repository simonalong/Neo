package com.simonalong.neo.sql.builder;

import com.simonalong.neo.NeoMap;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/3/22 下午8:04
 */
@UtilityClass
public class UpdateSqlBuilder {

    public String build(String tableName, NeoMap dataMap, NeoMap searchMap){
        return "update " + tableName + buildSetValues(dataMap) + SqlBuilder.buildWhere(searchMap);
    }

    public String buildSetValues(NeoMap searchMap){
        return " set " + String.join(", ", searchMap.keySet().stream().map(f -> SqlBuilder.toDbField(f) + "=?").collect(Collectors.toList()));
    }
}
