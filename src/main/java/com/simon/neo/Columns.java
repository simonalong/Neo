package com.simon.neo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public class Columns {

    @Getter
    private Set<String> fieldSets = new LinkedHashSet<>();

    private Columns(){}

    public static Columns all(Neo neo, String tableName) {
        return Columns.of(neo.getColumnNameList(tableName).toArray(new String[]{}));
    }

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length) {
            return columns;
        }
        return columns.addAll(fields);
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

    /**
     * 返回select 后面选择的列对应的数据库的字段
     * @return 比如：table1.`c2`, table1.`c3`, table1.`c1`
     */
    public String buildFields(String tableName){
        fieldSets = columnToDbField();
        return tableName + "." + String.join(", " + tableName + ".", fieldSets);
    }

    public Columns addAll(String... fields){
        this.fieldSets.addAll(Arrays.asList(fields));
        return this;
    }

    public boolean contains(String data){
        return fieldSets.contains(data);
    }

    public boolean isEmpty(){
        return fieldSets.isEmpty();
    }

    @Override
    public String toString(){
        return fieldSets.toString();
    }

    /**
     * 将列名的前后添加"`"，name -> `name`
     */
    private Set<String> columnToDbField(){
        Set<String> fieldSet = new HashSet<>();
        fieldSets.forEach(f-> fieldSet.add("`" + f + "`"));
        return fieldSet;
    }
}
