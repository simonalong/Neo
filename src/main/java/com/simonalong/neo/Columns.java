package com.simonalong.neo;

import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.db.AliasParser;
import com.simonalong.neo.db.ColumnParseException;
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
    /**
     * 表的原始名
     */
    private String tableNameOrigin;
    /**
     * 表的别名
     */
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

    public Columns setTableName(String tableName){
        this.tableNameOrigin = AliasParser.metaData(tableName);
        this.tableName = AliasParser.getAlias(tableName);
        return this;
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
                        f.addAll(neo.getColumnNameList(tableNameOrigin));
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

    public Stream<String> stream(String tableName){
        return tableFieldsMap.get(tableName).stream();
    }

    public Stream<String> stream(){
        return stream(DEFAULT_TABLE);
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
        Set<String> fieldSet = new HashSet<>();

        // key: group, value: `group` as group1
        Map<String, String> columnMap = fieldSets.stream()
            .filter(AliasParser::haveAlias)
            // 对于含有table.group的情况和table.`group`和`group`这种情况进行考虑
            .collect(Collectors.toMap(f -> filterColumnToMeta(AliasParser.metaData(f)),
                f -> filterColumnToFinal(AliasParser.metaData(f)) + " " + AliasParser.getAlias(f)));

        fieldSets.forEach(f -> {
            if (AliasParser.haveAlias(f) || columnMap.containsKey(filterColumnToMeta(f))){
                String meta = filterColumnToMeta(AliasParser.metaData(f));
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

    private String toDbField(String field){
        return "`" + field + "`";
    }
}