package com.simonalong.neo;

import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.express.ExpressParser;

import java.util.function.BiPredicate;
import java.util.regex.Pattern;

/**
 * @author shizi
 * @since 2020/4/17 5:21 PM
 */
public class ConditionMap {

    private static final String ROOT = "#root";
    private static final String CURRENT = "#current";
    private static final Integer KV_NUM = 2;
    private NeoMap dataMap = NeoMap.of();

    public static ConditionMap of(String... kvs) {
        if (kvs.length % KV_NUM != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        ConditionMap conditionMap = new ConditionMap();
        for (int i = 0; i < kvs.length; i += KV_NUM) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }
            conditionMap.put(kvs[i], kvs[i + 1]);
        }
        return conditionMap;
    }

    /**
     * 添加对应的条件脚本
     * <p>
     * 其中value脚本中支持两种占位符 #current和#root，其中#current 表示当前key在NeoMap中对应的值，而#root.xxx 表示在NeoMap中key为 xxx 对应的值
     *
     * @param key         key
     * @param scriptValue 返回值为Boolean的groovy脚本
     * @return 实例
     */
    public ConditionMap put(String key, String scriptValue) {
        BiPredicate<NeoMap, String> predicate;
        ExpressParser parser = new ExpressParser();
        predicate = (dataMap, currentKey) -> {
            parser.setBinding(parseConditionExpress(scriptValue, dataMap, currentKey));
            return parser.parse("import static java.lang.Math.*\n", rmvFix(scriptValue));
        };
        dataMap.put(key, predicate);
        return this;
    }

    /**
     * 判断当前的key和value是否满足条件
     *
     * @param dataMap 数据map
     * @param key     待校验的key
     * @return true：表示符合条件或者不包含，false：不符合条件
     */
    @SuppressWarnings("unchecked")
    public Boolean condition(NeoMap dataMap, String key) {
        if (!dataMap.containsKey(key)) {
            return true;
        }

        return dataMap.get(BiPredicate.class, key).test(dataMap, key);
    }

    private NeoMap parseConditionExpress(String scriptValue, NeoMap dataMap, String currentKey) {
        NeoMap map = NeoMap.of();
        String regex = "(#root)\\.(\\w+)";
        java.util.regex.Matcher m = Pattern.compile(regex).matcher(scriptValue);
        while (m.find()) {
            String fieldFullName = rmvFix(m.group());
            if (dataMap.containsKey(fieldFullName)) {
                map.put(fieldFullName, dataMap.get(fieldFullName));
            }
        }

        if (scriptValue.contains(CURRENT)) {
            map.put(rmvFix(CURRENT), dataMap.get(currentKey));
        }

        return map;
    }

    private String rmvFix(String str) {
        if (str.contains(ROOT + ".")) {
            str = str.replace(ROOT + ".", "");
        }

        if (str.contains("#")) {
            return str.replace("#", "");
        }
        return str;
    }
}
