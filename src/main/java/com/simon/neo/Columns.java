package com.simon.neo;

import com.simon.neo.db.NeoColumn.Column;
import com.simon.neo.db.NeoTable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public class Columns {

    @Getter
    private Set<String> fieldSets = new LinkedHashSet<>();
    @Setter
    private String tableName;

    private Columns(){}

    /**
     * 将对应表的所有列返回
     *
     * @param neo 库对象
     * @param tableName 表名
     * @return 列所有的数据
     */
    public static Columns all(Neo neo, String tableName) {
        return Columns.of(neo.getColumnNameList(tableName).toArray(new String[]{}));
    }

    public static Columns name(String tableName){
        Columns columns = Columns.of();
        columns.setTableName(tableName);
        return columns;
    }

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length) {
            return columns;
        }
        return columns.columns(fields);
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

    public static boolean isEmpty(Collection<Columns> columnsList){
        return null == columnsList || columnsList.isEmpty();
    }

    public Columns columns(String... fields){
        if (null == fields || 0 == fields.length) {
            return this;
        }
        this.fieldSets.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields() {
        Set<String> fieldSetList = columnToDbField();
        if (null == tableName) {
            return "`" + String.join("`, `", fieldSetList) + "`";
        } else {
            return tableName + "." + String.join(", " + tableName + ".", fieldSetList);
        }
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     *
     * @param tableName 表名
     * @return 比如：table1.`c2`, table1.`c3`, table1.`c1`
     */
    public String buildFields(String tableName){
        Set<String> fieldSetList = columnToDbField();
        return tableName + "." + String.join(", " + tableName + ".", fieldSetList);
    }

    /**
     * 返回一个表的所有的列的数据
     * @param neo 库对象
     * @param columns 列数据
     * @return 所有的列构成的查询列的字符，比如：table1.`c2`, table1.`c3`, table1.`c1`
     */
    public String buildAllFields(Neo neo, Columns columns){
        if (null == tableName){
           return "";
        }
        return Columns.all(neo, tableName).add(columns).buildFields(tableName);
    }

    // todo delete
    public static String buildAllFields(Neo neo, Columns columns, String tableName){
        return Columns.all(neo, tableName).add(columns).buildFields(tableName);
    }

    public Columns add(Columns columns){
        this.fieldSets.addAll(columns.getFieldSets());
        return this;
    }

    public boolean contains(String data){
        return fieldSets.contains(data);
    }

    public void remove(String key){
        fieldSets.remove(key);
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
     *
     * 针对有相同列名的情况，这里进行转换，比如：group as group1 -> `group` as group1
     *
     * 注意：还有一种是有列为*，展开后会有很多列，然后再在列后面添加对应的列转换，则这里会进行覆盖和替换
     * 对于这样的一种情况：group, group as group1 -> `group` as group1
     */
    private Set<String> columnToDbField(){
        String asStr = " as ";
        Set<String> fieldSet = new HashSet<>();

        // key: group, value: `group` as group1
        Map<String, String> columnMap = fieldSets.stream().filter(f -> f.contains(asStr))
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
