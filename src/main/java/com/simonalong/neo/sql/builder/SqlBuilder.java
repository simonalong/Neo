package com.simonalong.neo.sql.builder;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.simonalong.neo.Pair;
import com.simonalong.neo.db.DbType;
import com.simonalong.neo.db.NeoContext;
import com.simonalong.neo.util.CharSequenceUtil;
import lombok.experimental.UtilityClass;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:13
 */
@UtilityClass
public class SqlBuilder {

    /**
     * 构造in后面的数据，比如返回：('310','311')
     * @param values 数据集合
     * @param <T> 类型
     * @return 构造后的数据
     */
    public <T> String buildIn(Collection<T> values) {
        if (null == values || values.isEmpty()) {
            return "";
        }
        List<String> strList = values.stream().map(c -> (null == c) ? null : String.valueOf(c)).collect(Collectors.toList());
        return "('" + String.join("', '", strList) + "')";
    }

    /**
     * 构造where 对应的sql语句
     *
     * @param searchMap 搜索条件
     * @return 返回where对应的语句：where `group` =  ? and `name` =  ?
     */
    public String buildWhere(NeoMap searchMap) {
        if (!NeoMap.isEmpty(searchMap)) {
            String whereCondition = buildWhereCondition(searchMap);
            if (!"".equals(whereCondition)) {
                return " where " + whereCondition;
            }
        }
        return "";
    }

    public List<Object> buildValueList(NeoMap searchMap) {
        return new ArrayList<>(searchMap.values());
    }

    /**
     * 返回where后面的带有占位符的条件sql
     *
     * @param searchMap 搜索条件map
     * @return 比如：`group` = ? and `name` = ?
     */
    public String buildWhereCondition(NeoMap searchMap) {
        if (!NeoMap.isEmpty(searchMap)) {
            List<String> conditionList = buildConditionMeta(searchMap);
            if (!conditionList.isEmpty()) {
                return String.join(" and ", conditionList);
            }
        }
        return "";
    }

    /**
     * 转换后的条件元数据
     *
     * @param searchMap 搜索条件
     * @return 返回sql中的多个字段：{@code [`age` = ?, `group` =  ?]}
     */
    public List<String> buildConditionMeta(NeoMap searchMap) {
        if (searchMap.isSorted()) {
            return searchMap.clone().entryQueue().stream().map(SqlBuilder::valueFix).collect(Collectors.toList());
        } else {
            return searchMap.clone().entrySet().stream().map(SqlBuilder::valueFix).collect(Collectors.toList());
        }
    }

    /**
     * 将普通的列名转换为sql语句中的列，{@code group --> `group`}
     * @param column 原列名
     * @return 转换后的列名，比如name 到 `name`
     */
    public String toDbField(String column) {
        Neo neo = NeoContext.getNeo();
        if (null != neo && null != neo.getDbType()) {
            // pg不需要 ` 这种字段修饰符
            if (neo.getDbType().equals(DbType.PGSQL)) {
                return column;
            }
        }
        String dom = "`";
        if (column.contains(dom)) {
            return column;
        }

        String point = ".";
        if (column.contains(point)) {
            return column;
        }

        return "`" + column + "`";
    }

    public String toDbField(String tableName, String column) {
        Neo neo = NeoContext.getNeo();
        if (null != neo && null != neo.getDbType()) {
            // pg不需要 ` 这种字段修饰符
            if (neo.getDbType().equals(DbType.PGSQL)) {
                if (CharSequenceUtil.isEmpty(tableName) || tableName.equals(DEFAULT_TABLE)) {
                    return column;
                } else {
                    return tableName + "." + column;
                }
            }
        }
        String dom = "`";
        if (column.contains(dom)) {
            return column;
        }

        String point = ".";
        if (column.contains(point)) {
            return column;
        }

        if (tableName.equals(DEFAULT_TABLE)) {
            return "`" + column + "`";
        } else if (CharSequenceUtil.isEmpty(tableName)) {
            return "`" + column + "`";
        } else {
            return tableName + "." + "`" + column + "`";
        }
    }

    /**
     * 给批量更新使用的值
     *
     * @param dataMapList 值集合
     * @return 值集合
     */
    public List<Object> buildBatchValueList(List<NeoMap> dataMapList) {
        if (null == dataMapList || dataMapList.isEmpty()) {
            return Collections.emptyList();
        }

        return dataMapList.stream().flatMap(NeoMap::valueStream).collect(Collectors.toList());
    }

    /**
     * 将对应的值进行拼接
     *
     * @param entry 不是order by这样的数据
     * @return 条件元数据：比如：a=? 或者 a is null
     */
    public String valueFix(Pair<String, Object> entry) {
        Object value = entry.getValue();
        String key = toDbField(entry.getKey());
        if (null == value) {
            return key + " is null";
        }
        return key + " = ?";
    }

    public String valueFix(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        String key = toDbField(entry.getKey());
        if (null == value) {
            return key + " is null";
        }
        return key + " = ?";
    }
}
