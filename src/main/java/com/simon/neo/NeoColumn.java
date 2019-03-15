package com.simon.neo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午8:30
 */
public class NeoColumn {

    private Set<String> fieldSets = new LinkedHashSet<>();

    private NeoColumn(){}

    public static NeoColumn of(String... fields){
        return new NeoColumn().addAll(fields);
    }

    public static boolean isEmpty(NeoColumn neoColumn){
        return null == neoColumn || neoColumn.isEmpty();
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields(){
        return "`" + String.join("`, `", fieldSets) + "`";
    }

    public NeoColumn addAll(String... fields){
        this.fieldSets.addAll(Arrays.asList(fields));
        return this;
    }

    public boolean isEmpty(){
        return fieldSets.isEmpty();
    }
}
