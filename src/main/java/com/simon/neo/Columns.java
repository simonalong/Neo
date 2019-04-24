package com.simon.neo;

import com.simon.neo.db.NeoColumn.Column;
import com.simon.neo.db.NeoTable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public class Columns {

    private static final String DEFAULT_TABLE = "";
    @Getter
    private Map<String, Set<String>> tableFieldsMap = new LinkedHashMap<>();

    private Columns(){}

    public static Columns table(String tableName, String... cs){
        return Columns.of().and(tableName, cs);
    }

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length) {
            return columns;
        }
        return columns.and(DEFAULT_TABLE, fields);
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

    /**
     * 将对应表的所有列返回
     *
     * @param neo 库对象
     * @param tableName 表名
     * @return 列所有的数据
     */
    public static Columns all(Neo neo, String tableName) {
        return Columns.of().and(tableName, neo.getColumnNameList(tableName));
    }

    public static boolean isEmpty(Columns columns){
        return null == columns || columns.isEmpty();
    }

    public static boolean isEmpty(Collection<Columns> columnsList){
        return null == columnsList || columnsList.isEmpty();
    }

    public Columns and(String tableName, String... cs) {
        return and(tableName, new HashSet<>(Arrays.asList(cs)));
    }

    public Columns and(String tableName, Set<String> fieldSets) {
        this.tableFieldsMap.compute(tableName, (k, v) -> {
            if (null == v) {
                return new HashSet<>(fieldSets);
            } else {
                v.addAll(fieldSets);
                return v;
            }
        });
        return this;
    }

    /**
     * 针对列中如果包含*，则进行列扩展
     *
     * 请注意，这里如果列中包含*，那么必须指明表名，用于获取指定的列
     */
    public Columns extend(Neo neo){
        String allColumnName = "*";
        tableFieldsMap.forEach((tableName, fieldSets) -> {
            if(!tableName.equals(DEFAULT_TABLE)){
                if(fieldSets.contains(allColumnName)){
                    fieldSets.addAll(neo.getColumnNameList(tableName));
                }
            }
        });

        remove("*");
        return this;
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields() {
        return String.join(", ", tableFieldsMap.entrySet().stream().flatMap(e->{
            String tableName = e.getKey();
            return columnToDbField(e.getValue()).stream().map(c->{
                if(!tableName.equals(DEFAULT_TABLE)){
                    return tableName + "." + c;
                }
                return c;
            });
        }).collect(Collectors.toSet()));
    }

    public Set<String> getFieldSets(){
        return tableFieldsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     *
     * @param tableName 表名
     * @return 比如：table1.`c2`, table1.`c3`, table1.`c1`
     */
    // todo to delete
    public String buildFields(String tableName){
        Set<String> fieldSetList = columnToDbField(new HashSet<>());
        return tableName + "." + String.join(", " + tableName + ".", fieldSetList);
    }

//    /**
//     * 返回一个表的所有的列的数据
//     * @param neo 库对象
//     * @param columns 列数据
//     * @return 所有的列构成的查询列的字符，比如：table1.`c2`, table1.`c3`, table1.`c1`
//     */
//    public String buildAllFields(Neo neo, Columns columns){
//        if (null == tableName){
//           return "";
//        }
//        return Columns.all(neo, tableName).add(columns).buildFields(tableName);
//    }

    // todo delete
    public static String buildAllFields(Neo neo, Columns columns, String tableName){
        return Columns.all(neo, tableName).add(columns).buildFields(tableName);
    }

    public Columns add(Columns columns){
        this.getTableFieldsMap().putAll(columns.getTableFieldsMap());
        return this;
    }

    public Columns add(String...cs){
        return and(DEFAULT_TABLE, cs);
    }

    public boolean contains(String data){
        return tableFieldsMap.values().stream().flatMap(Collection::stream).anyMatch(c->c.equals(data));
    }

    public void remove(String key){
        tableFieldsMap.forEach((key1, value) -> value.remove(key));
    }

    public boolean isEmpty(){
        return tableFieldsMap.isEmpty();
    }

    @Override
    public String toString(){
        return buildFields();
    }

    /**
     * 将列名的前后添加"`"，name -> `name`
     *
     * 针对有相同列名的情况，这里进行转换，比如：group as group1 -> `group` as group1
     *
     * 注意：还有一种是有列为*，展开后会有很多列，然后再在列后面添加对应的列转换，则这里会进行覆盖和替换
     * 对于这样的一种情况：group, group as group1 -> `group` as group1
     */
    private Set<String> columnToDbField(Set<String> fieldSets){
        String asStr = " as ";
        Set<String> fieldSet = new HashSet<>();

        // key: group, value: `group` as group1
        Map<String, String> columnMap = fieldSets.stream()
            .filter(f -> f.contains(asStr))
            .collect(Collectors.toMap(f -> f.substring(0, f.indexOf(asStr)),
                f -> "`" + f.substring(0, f.indexOf(asStr)) + "`" + f.substring(f.indexOf(asStr))));

        fieldSets.forEach(f -> {
            if (columnMap.containsKey(f)) {
                fieldSet.add(columnMap.get(f));
            } else {
                if (!f.contains(asStr)){
                    fieldSet.add("`" + f + "`");
                } else{
                    fieldSet.add("`" + f.substring(0, f.indexOf(asStr)) + "`" + f.substring(f.indexOf(asStr)));
                }
            }
        });
        return fieldSet;
    }
}
