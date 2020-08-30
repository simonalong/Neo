package com.simonalong.neo.sql.builder;

import static com.simonalong.neo.NeoConstant.*;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoConstant;
import com.simonalong.neo.NeoMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.simonalong.neo.Pair;
import com.simonalong.neo.db.DbType;
import com.simonalong.neo.db.NeoContext;
import com.simonalong.neo.sql.InList;
import lombok.experimental.UtilityClass;


/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:13
 */
@UtilityClass
public class SqlBuilder {

    /**
     * 字段搜索的like前缀
     */
    private static final String LIKE_PRE = "like ";
    /**
     * 比较操作符的前缀
     */
    private static final List<String> THAN_PRE = Arrays.asList(">", "<", ">=", "<=");

    private static ThreadLocal<Boolean> withValueFlag = ThreadLocal.withInitial(() -> false);

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
        return "('" + String.join("','", strList) + "')";
    }

    /**
     * 构造where 对应的sql语句
     *
     * @param searchMap 搜索条件
     * @return 返回where对应的语句：where `group` =  ? and `name` =  ?
     */
    public String buildWhere(NeoMap searchMap) {
        NeoMap conditionMap = searchMap.assignExcept(ORDER_BY);
        if (!NeoMap.isEmpty(conditionMap)) {
            String whereCondition = buildWhereCondition(searchMap);
            if (!"".equals(whereCondition)) {
                return " where " + whereCondition;
            }
        }
        return "";
    }

    /**
     * 提取order by数据
     *
     * @param searchMap 搜索条件
     * @return 返回 order by `xxx` 或者 order by `xxx` desc
     */
    public String buildOrderBy(NeoMap searchMap) {
        if (!NeoMap.isEmpty(searchMap) && searchMap.containsKey(ORDER_BY)) {
            return " " + ORDER_BY + " " + orderByStr(searchMap.getString(ORDER_BY));
        }
        return "";
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
     * order by 后面的字符转换为数据库字样，比如
     * {@code group desc --> `group` desc} {@code group desc, name asc --> `group` desc, `name` asc}
     * @param orderByValueStr order by后面的字符
     * @return 转换后的字符
     */
    private String orderByStr(String orderByValueStr) {
        if (null == orderByValueStr) {
            return "";
        }
        List<String> values = Arrays.asList(orderByValueStr.split(","));
        List<String> valueList = new ArrayList<>();
        values.forEach(v -> {
            v = v.trim();
            if (v.contains(DESC) || v.contains(ASC)) {
                Integer index = v.indexOf(" ");
                valueList.add(toDbField(v.substring(0, index)) + " " + v.substring(index + 1));
            } else {
                valueList.add(toDbField(v));
            }
        });
        return String.join(", ", valueList);
    }

    /**
     * 转换后的条件元数据
     *
     * <ul>
     *      <li>1.如果值为like开头，则支持模糊查询</li>
     *      <li>2.如果为比较符号{@code >、>=、<、<=}这种，则表示有比较操作</li>
     * </ul>
     *
     * @param searchMap 搜索条件
     * @return 返回sql中的多个字段：{@code [`age` > ?, `group` =  ?, `name` like 'haode%']}
     */
    public List<String> buildConditionMeta(NeoMap searchMap) {
        String orderByStr = "order by";
        return searchMap.clone().entrySet().stream()
            .filter(r -> !r.getKey().trim().equals(orderByStr))
            .filter(e-> searchMap.satisfyCondition(e.getKey()))
            .map(e -> valueFix(searchMap, e))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
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
        if (column.startsWith(dom) && column.endsWith(dom)) {
            return column;
        }
        return "`" + column + "`";
    }

    /**
     * 生成值列表，对于特殊的值：含有like和比较符进行处理
     *
     * @param searchMap 搜索的值map
     * @return 值结果集合：[ok, haode]
     */
    public List<Object> buildValueList(NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }

        String orderByStr = "order by";
        return searchMap.stream().filter(r -> !r.getKey().trim().equals(orderByStr)).filter(e->{
            return searchMap.satisfyCondition(e.getKey());
        }).map(o->{
            Object v = o.getValue();
            if (v instanceof String) {
                String valueStr = String.class.cast(v);

                // 处理模糊搜索，like
                if (valueStr.startsWith(LIKE_PRE)) {
                    return null;
                }

                // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
                if (haveThanPre(valueStr)) {
                    return getSymbolAndValue(valueStr).getValue();
                }
            } else if (v instanceof InList) {
                return null;
            }
            return v;
        }).filter(Objects::nonNull).collect(Collectors.toList());
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
     * <p>
     *     数值有两种情况：
     *     <ul>
     *         <li>1. like xx这种值要单独解析出来</li>
     *         <li>2. >= xx这种符号的值也要解析出来</li>
     *     </ul>
     * @param searchMap 条件map
     * @param entry 不是order by这样的数据
     * @return 条件元数据：比如：a=? 或者 a >= ? 或者 a like xxx%
     */
    private String valueFix(NeoMap searchMap, Entry<String, Object> entry){
        Object value = entry.getValue();
        String key = toDbField(entry.getKey());
        if (null == value) {
            return key + " is null";
        }
        if (value instanceof String) {
            String valueStr = String.class.cast(value);

            // 处理模糊搜索，like
            if (valueStr.startsWith(LIKE_PRE)) {
                return key + " like " +  "'" + getLikeValue(valueStr) + "'";
            }

            // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
            if (haveThanPre(valueStr)) {
                Pair<String, String> symbolAndValue = getSymbolAndValue(valueStr);
                searchMap.put(entry.getKey(), symbolAndValue.getValue().trim());
                return key + " " + symbolAndValue.getKey() + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
            }
            return key + " = " + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
        } else if (value instanceof InList) {
            return ((InList) value).buildSql(key);
        }
        return key + " = " + ((withValueFlag.get() ? value : " ?"));
    }

    /**
     * 搜索的数据是否有比较类型的前缀
     *
     * @param value 值
     * @return 是否包含比较符
     */
    public boolean haveThanPre(String value){
        if (null == value || "".equals(value)) {
            return false;
        }

        for(String pre : THAN_PRE){
            if(value.startsWith(pre)){
                return true;
            }
        }
        return false;
    }

    /**
     * 将传入的包含有like前缀的字符串，提取出value，然后拼接，比如：like xxx -> '%xxx%'，like #xxx -> '%xxx'
     */
    private String getLikeValue(String likeValue) {
        String value = likeValue.substring(likeValue.indexOf(LIKE_PRE) + LIKE_PRE.length());
        if (value.contains("#")) {
            value = value.replaceAll("#", "%");
        } else {
            value = "%" + value + "%";
        }
        return value;
    }

    /**
     * 获取值的比较符和数据值
     *
     * @param valueStr 带有比较符的参数
     * @return key为比较符，value为字符数据
     */
    @SuppressWarnings("all")
    public Pair<String, String> getSymbolAndValue(String valueStr) {
        valueStr = valueStr.trim();
        if (valueStr.startsWith(">")) {
            if (valueStr.startsWith(">=")) {
                return new Pair<>(">=", valueStr.substring(valueStr.indexOf(">=") + ">=".length()));
            } else {
                return new Pair<>(">", valueStr.substring(valueStr.indexOf(">") + ">".length()));
            }
        } else if (valueStr.startsWith("<")) {
            if (valueStr.startsWith("<=")) {
                return new Pair<>("<=", valueStr.substring(valueStr.indexOf("<=") + "<=".length()));
            } else {
                return new Pair<>("<", valueStr.substring(valueStr.indexOf("<") + "<".length()));
            }
        }
        return new Pair<>("=", valueStr);
    }
}
