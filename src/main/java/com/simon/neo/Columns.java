package com.simon.neo;

import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.db.ColumnParseException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public final class Columns {

    private static final String DEFAULT_TABLE = "";
    private static final String ALL_COLUMN_NAME = "*";
    @Setter
    @Accessors(chain = true)
    private String tableName;
    @Setter
    @Accessors(chain = true)
    private Neo neo;
    @Getter
    private Map<String, Set<String>> tableFieldsMap = new LinkedHashMap<>();

    private Columns() {
    }

    public static Columns table(String tableName){
        return Columns.of().setTableName(tableName);
    }

    public static Columns table(String tableName, Neo neo){
        return Columns.of().setTableName(tableName).setNeo(neo);
    }

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length || Stream.of(fields).allMatch(Objects::isNull)) {
            return columns;
        }
        return columns.and(DEFAULT_TABLE).cs(fields);
    }

    private static Columns of(List<Field> fieldList, NamingChg namingChg) {
        return Columns.of(fieldList.stream().map(f -> namingChg.smallCamelToOther(f.getName())).collect(Collectors.toList())
                .toArray(new String[]{}));
    }

    public static Columns from(Class tClass) {
        if (null == tClass) {
            return Columns.of();
        }
        return Columns.of(Arrays.asList(tClass.getDeclaredFields()), NamingChg.DEFAULT);
    }

    public static Columns from(Class tClass, NamingChg namingChg) {
        if (null == tClass) {
            return Columns.of();
        }
        return Columns.of(Arrays.asList(tClass.getDeclaredFields()), namingChg);
    }

    /**
     * 将对应表的所有列返回
     *
     * @param neo 库对象
     * @param tableName 表名
     * @return 列所有的数据
     */
    public static Columns from(Neo neo, String tableName) {
        return Columns.of().and(tableName).cs(neo.getColumnNameList(tableName));
    }

    public static boolean isEmpty(Columns columns) {
        return null == columns || columns.isEmpty();
    }

    public static boolean isEmpty(Collection<Columns> columnsList) {
        return null == columnsList || columnsList.isEmpty();
    }

    public Columns and(String tableName){
        return this.setTableName(tableName);
    }

    public Columns cs(String... cs){
        return cs(Arrays.stream(cs).filter(c->!"".equals(c)).collect(Collectors.toSet()));
    }

    public Columns cs(Set<String> fieldSets) {
        if(fieldSets.contains(ALL_COLUMN_NAME) && null == neo){
            throw new ColumnParseException("列中含有符号'*'，请先添加库对象neo（setNeo），或者使用table(String tableName, Neo neo)函数");
        }
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
     * 返回select 后面选择的列对应的数据库的字段
     *
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields() {
        if (null != neo) {
            tableFieldsMap.forEach((tableName, f) -> {
                if (!tableName.equals(DEFAULT_TABLE)) {
                    if (f.contains(ALL_COLUMN_NAME)) {
                        f.addAll(neo.getColumnNameList(tableName));
                    }
                }
            });
            remove(ALL_COLUMN_NAME);
        }

        return String.join(", ", tableFieldsMap.entrySet().stream().flatMap(e -> {
            String tableName = e.getKey();
            return columnToDbField(e.getValue()).stream().map(c -> {
                if (!tableName.equals(DEFAULT_TABLE)) {
                    return tableName + "." + c;
                }
                return c;
            });
        }).collect(Collectors.toSet()));
    }

    public Set<String> getFieldSets() {
        return tableFieldsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Columns append(Columns columns) {
        Map<String, Set<String>> otherTableFieldMap = columns.getTableFieldsMap();
        this.getTableFieldsMap().forEach((tableName, fieldSet)->{
            if(otherTableFieldMap.containsKey(tableName)){
                fieldSet.addAll(otherTableFieldMap.get(tableName));
            }
        });
        return this;
    }

    public Columns append(String... cs) {
        return and(DEFAULT_TABLE).cs(cs);
    }

    public boolean contains(String data) {
        return tableFieldsMap.values().stream().flatMap(Collection::stream).anyMatch(c -> c.equals(data));
    }

    public void remove(String key) {
        tableFieldsMap.forEach((key1, value) -> value.remove(key));
    }

    public boolean isEmpty() {
        return tableFieldsMap.isEmpty();
    }

    @Override
    public String toString() {
        return buildFields();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Columns) {
            return this.buildFields().equals(Columns.class.cast(obj).buildFields());
        }
        return false;
    }

    /**
     * 将列名的前后添加"`"，name -> `name`
     *
     * 针对有相同列名的情况，这里进行转换，比如：group as group1 -> `group` as group1
     *
     * 注意：还有一种是有列为*，展开后会有很多列，然后再在列后面添加对应的列转换，则这里会进行覆盖和替换，对于这样的一种情况：group, group as group1 -> `group` as group1
     */
    private Set<String> columnToDbField(Set<String> fieldSets) {
//        String asStr = " as ";
        Set<String> fieldSet = new HashSet<>();

        // key: group, value: `group` as group1
        Map<String, String> columnMap = fieldSets.stream()
            .filter(this::haveAlias)
            // 对于含有table.group的情况和table.`group`和`group`这种情况进行考虑
            .collect(Collectors.toMap(f -> filterColumnToMeta(fieldMetaAfterAlias(f)),
                f -> filterColumnToFinal(fieldMetaAfterAlias(f)) + " " + columnAlias(f)));

        fieldSets.forEach(f -> {
            if (haveAlias(f) || columnMap.containsKey(filterColumnToMeta(f))){
                String meta = filterColumnToMeta(fieldMetaAfterAlias(f));
                if (columnMap.containsKey(meta)) {
                    fieldSet.add(columnMap.get(meta));
                }
            } else {
                fieldSet.add(filterColumnToFinal(f));
            }
        });
        return fieldSet;
    }

    /**
     * 筛选和清理列名为原始字符
     *
     * 对于这么几种情况进行处理：{@code table.name --> name } {@code `name` --> name} {@code table.`name` --> name}
     * @param fieldName 指定的列名
     * @return 清理之后的列名
     */
    private String filterColumnToMeta(String fieldName) {
        String point = ".";
        String dom = "`";
        fieldName = fieldName.trim();
        if (fieldName.contains(point)) {
            fieldName = fieldName.substring(fieldName.indexOf(point) + 1);
            if (fieldName.startsWith(dom) && fieldName.endsWith(dom)) {
                return fieldName.substring(1, fieldName.length() - 1);
            }
            return fieldName;
        } else {
            if (fieldName.startsWith(dom) && fieldName.endsWith(dom)) {
                return fieldName.substring(1, fieldName.length() - 1);
            }
            return fieldName;
        }
    }

    /**
     * 筛选处理为合法的sql字段
     *
     * 对于这么几种情况进行处理：{@code name --> `name`} {@code table.name --> table.`name`} {@code table.`name` --> table.`name`}
     * @param fieldName 指定的列名
     * @return 处理之后的sql对应的列名
     */
    private String filterColumnToFinal(String fieldName){
        String point = ".";
        String dom = "`";
        fieldName = fieldName.trim();
        if (fieldName.contains(point)) {
            Integer index = fieldName.indexOf(point);
            String tableName = fieldName.substring(0, index);
            String columnName = fieldName.substring(index + 1);
            if (columnName.startsWith(dom) && columnName.endsWith(dom)) {
                return fieldName;
            }
            return tableName + "." + toDbField(columnName);
        } else {
            if (fieldName.startsWith(dom) && fieldName.endsWith(dom)) {
                return fieldName;
            }
            return toDbField(fieldName);
        }
    }

    /**
     * 判断列是否有别名
     * @param fieldStr 列名
     * @return true 有别名，false：没有别名
     */
    private Boolean haveAlias(String fieldStr){
        String asStr = " as ";
        String spaceStr = " ";
        fieldStr = fieldStr.trim();
        if (fieldStr.contains(asStr) || fieldStr.contains(spaceStr)) {
            return true;
        }
        return false;
    }

    /**
     * 去除as 字段之后的字段：{@code table.name as n --> table.name} {@code table.name n}
     * @param fieldStr 字段列
     * @return 去除别名后的名字
     */
    private String fieldMetaAfterAlias(String fieldStr){
        String asStr = "as";
        String spaceStr = " ";
        if(fieldStr.contains(asStr)){
            return fieldStr.substring(0, fieldStr.indexOf(asStr)).trim();
        }

        if (fieldStr.contains(spaceStr)){
            return fieldStr.substring(0, fieldStr.indexOf(spaceStr)).trim();
        }
        return fieldStr;
    }

    /**
     * 获取列的别名
     *
     * @param fieldStr 字段列
     * @return 列的别名
     */
    private String columnAlias(String fieldStr){
        String asStr = " as ";
        String spaceStr = " ";
        fieldStr = fieldStr.trim();
        if(fieldStr.contains(asStr)){
            return fieldStr.substring(fieldStr.indexOf(asStr) + 1);
        }

        if (fieldStr.contains(spaceStr)){
            return fieldStr.substring(fieldStr.indexOf(spaceStr) + 1);
        }
        return fieldStr;
    }

    private String toDbField(String field){
        return "`" + field + "`";
    }
}
