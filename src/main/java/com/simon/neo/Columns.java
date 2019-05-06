package com.simon.neo;

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

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午9:18
 */
public class Columns {

    private static final String DEFAULT_TABLE = "";
    @Getter
    private Map<String, Set<String>> tableFieldsMap = new LinkedHashMap<>();

    private Columns() {
    }

    public static Columns table(String tableName, String... cs) {
        return Columns.of().and(tableName, cs);
    }

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length || Stream.of(fields).allMatch(Objects::isNull)) {
            return columns;
        }
        return columns.and(DEFAULT_TABLE, fields);
    }

    public static Columns of(List<Field> fieldList) {
        return Columns.of(fieldList.stream().map(Field::getName).collect(Collectors.toList()).toArray(new String[]{}));
    }

    public static Columns from(Class tClass) {
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

    public static boolean isEmpty(Columns columns) {
        return null == columns || columns.isEmpty();
    }

    public static boolean isEmpty(Collection<Columns> columnsList) {
        return null == columnsList || columnsList.isEmpty();
    }

    public Columns and(String tableName, String... cs) {
        List<String> fieldList = Arrays.stream(cs).filter(c->!"".equals(c)).collect(Collectors.toList());
        return and(tableName, new HashSet<>(fieldList));
    }

    public Columns and(String tableName, Set<String> fieldSets) {
        if (null == fieldSets || fieldSets.isEmpty()) {
            return this;
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
     * 针对列中如果包含*，则进行列扩展
     *
     * 请注意，这里如果列中包含*，那么必须指明表名，用于获取指定的列
     */
    public Columns extend(Neo neo) {
        String allColumnName = "*";
        tableFieldsMap.forEach((tableName, fieldSets) -> {
            if (!tableName.equals(DEFAULT_TABLE)) {
                if (fieldSets.contains(allColumnName)) {
                    fieldSets.addAll(neo.getColumnNameList(tableName));
                }
            }
        });

        remove("*");
        return this;
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     *
     * @return 比如：`xxx`, `kkk`
     */
    public String buildFields() {
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

    public Columns add(Columns columns) {
        this.getTableFieldsMap().putAll(columns.getTableFieldsMap());
        return this;
    }

    public Columns add(String... cs) {
        return and(DEFAULT_TABLE, cs);
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

    /**
     * 将列名的前后添加"`"，name -> `name`
     *
     * 针对有相同列名的情况，这里进行转换，比如：group as group1 -> `group` as group1
     *
     * 注意：还有一种是有列为*，展开后会有很多列，然后再在列后面添加对应的列转换，则这里会进行覆盖和替换 对于这样的一种情况：group, group as group1 -> `group` as group1
     */
    private Set<String> columnToDbField(Set<String> fieldSets) {
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
                if (!f.contains(asStr)) {
                    fieldSet.add("`" + f + "`");
                } else {
                    fieldSet.add("`" + f.substring(0, f.indexOf(asStr)) + "`" + f.substring(f.indexOf(asStr)));
                }
            }
        });
        return fieldSet;
    }
}
