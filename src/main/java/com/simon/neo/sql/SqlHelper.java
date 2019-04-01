package com.simon.neo.sql;

import com.simon.neo.NeoMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:13
 */
@UtilityClass
public class SqlHelper {

    /**
     * 字段搜索的like前缀
     */
    private static final String LIKE_PRE = "like ";
    /**
     * 比较操作符的前缀
     */
    private static final List<String> THAN_PRE = Arrays.asList(">", "<", ">=", "<=");

    private static ThreadLocal<Boolean> withValueFlag = ThreadLocal.withInitial(() -> false);

    public <T> String in(List<T> values) {
        if (null == values || values.isEmpty()) {
            return "";
        }
        List<String> strList = values.stream().map(c -> (null == c) ? null : String.valueOf(c)).collect(Collectors.toList());
        return "('" + String.join("','", strList) + "')";
    }

    public String buildWhere(NeoMap searchMap) {
        if(!NeoMap.isEmpty(searchMap)) {
            return " where " + buildCondition(searchMap);
        }
        return "";
    }

    public String buildWhereWithValue(NeoMap searchMap) {
        withValueFlag.set(true);
        return buildWhere(searchMap);
    }

    /**
     * 值为占位符的条件拼接
     * @param searchMap 搜索条件map
     * @return 比如：`group` = ? and `name` = ?
     */
    public String buildCondition(NeoMap searchMap){
        StringBuilder stringBuilder = new StringBuilder();
        if (!NeoMap.isEmpty(searchMap)) {
            stringBuilder.append("`");
            return stringBuilder
                .append(String.join(" and `", buildConditionMeta(searchMap)))
                .toString();
        }
        return stringBuilder.toString();
    }

    /**
     * 带值的搜索拼接
     * @param searchMap 搜索条件map
     * @return 比如：`group` = 'group1' and `name` = 'name1'
     */
    public String buildConditionWithValue(NeoMap searchMap){
        withValueFlag.set(true);
        return buildCondition(searchMap);
    }

    private List<String> buildConditionMeta(NeoMap searchMap) {
        return searchMap.entrySet().stream().map(e->valueFix(searchMap, e)).collect(Collectors.toList());
    }

    private String valueFix(NeoMap searchMap, Entry<String, Object> entry){
        Object value = entry.getValue();
        if (value instanceof String) {
            String valueStr = String.class.cast(value);
            // 设置模糊搜索
            if (valueStr.startsWith(LIKE_PRE)) {
                searchMap.put(entry.getKey(), getLikeValue(valueStr));
                return entry.getKey() + "` like " + (withValueFlag.get() ? "'" + valueStr + "'" : "?");
            }

            // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
            if (haveThanPre(valueStr)) {
                Pair<String, String> symbolAndValue = getSymbolAndValue(valueStr);
                searchMap.put(entry.getKey(), symbolAndValue.getValue());
                return entry.getKey() + "` " + symbolAndValue.getKey() + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
            }
            return entry.getKey() + "` = " + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
        }
        return entry.getKey() + "` = " + ((withValueFlag.get() ? value : " ?"));
    }

    /**
     * 搜索的数据是否有比较类型的前缀
     */
    private boolean haveThanPre(String value){
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
     * 将传入的包含有like前缀的字符串，提取出value，然后拼接，比如：like xxx -> 'xxx%'
     */
    private String getLikeValue(String likeValue) {
        return likeValue.substring(likeValue.indexOf(LIKE_PRE) + LIKE_PRE.length()) + "%";
    }

    @SuppressWarnings("all")
    private Pair<String, String> getSymbolAndValue(String valueStr) {
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
