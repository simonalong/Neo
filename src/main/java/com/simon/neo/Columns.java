package com.simon.neo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public class Columns {

    private Set<String> fieldSets = new LinkedHashSet<>();

    private Columns(){}

    public static Columns of(String... fields){
        return new Columns().addAll(fields);
    }

    public static boolean isEmpty(Columns columns){
        return null == columns || columns.isEmpty();
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields(){
        return "`" + String.join("`, `", fieldSets) + "`";
    }

    public Columns addAll(String... fields){
        this.fieldSets.addAll(Arrays.asList(fields));
        return this;
    }

    public boolean isEmpty(){
        return fieldSets.isEmpty();
    }
}
