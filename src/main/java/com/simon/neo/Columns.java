package com.simon.neo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Columns of(List<Field> fieldList){
        return Columns.of(fieldList.stream().map(Field::getName).collect(Collectors.toList()).toArray(new String[]{}));
    }

    public static Columns from(Class tClass){
        if (null == tClass) {
            return Columns.of();
        }
        return Columns.of(Arrays.asList(tClass.getDeclaredFields()));
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

    @Override
    public String toString(){
        return fieldSets.toString();
    }
}
