package com.simonalong.neo;

import static com.simonalong.neo.NeoConstant.ALL_COLUMN_NAME;

import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.table.AliasParser;
import com.simonalong.neo.exception.ColumnParseException;
import java.util.ArrayList;
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

    @Setter
    @Accessors(chain = true)
    private Neo neo;
    /**
     * 全局默认采用小驼峰和数据库下划线方式
     */
    @Setter
    private static NamingChg globalNaming = NamingChg.UNDERLINE;
    @Getter
    private Map<String, Set<String>> tableFieldsMap = new LinkedHashMap<>();

    private Columns() {}

    public static Columns of(String... fields) {
        Columns columns = new Columns();
        if (null == fields || 0 == fields.length || Stream.of(fields).allMatch(Objects::isNull)) {
            return columns;
        }
        return columns.table(DEFAULT_TABLE, fields);
    }

    public static Columns of(Neo neo){
        return Columns.of().setNeo(neo);
    }

    public static Columns from(Class tClass) {
        return Columns.from(tClass, "");
    }

    public static Columns from(Class tClass, String... excludeFields) {
        if (null == tClass) {
            return Columns.of();
        }

        Set<String> excludeFieldList = new HashSet<>();
        if (null != excludeFields){
            excludeFieldList = Stream.of(excludeFields).collect(Collectors.toSet());
        }

        Set<String> finalExcludeFieldList = excludeFieldList;
        List<String> fieldNames = Arrays.stream(tClass.getDeclaredFields())
            .filter(field -> {
                if(!finalExcludeFieldList.isEmpty()){
                    return !finalExcludeFieldList.contains(field.getName());
                }
                return true;
            })
            .map(field -> {
                Column column = field.getDeclaredAnnotation(Column.class);
                if (null != column && !"".equals(column.value())) {
                    return column.value();
                } else {
                    return globalNaming.smallCamelToOther(field.getName());
                }
            })
            .collect(Collectors.toList());
        return Columns.of(fieldNames.toArray(new String[]{}));
    }

    public Columns table(String tableName, String... fields) {
//        String tableName = AliasParser.getOrigin(tableNameStr);
        List<String> fieldList = new ArrayList<>(Arrays.asList(fields));
        if (fieldList.isEmpty()) {
            fieldList.add("*");
        }
        if (fieldList.contains(ALL_COLUMN_NAME)) {
            if (null == neo) {
                throw new ColumnParseException("列中含有符号'*'，请先添加库对象neo（setNeo），或者使用of(Neo neo)先构造对象");
            } else if(null == tableName){
                throw new ColumnParseException("列中含有符号'*'，表名不能为空");
            } else {
                fieldList.remove(ALL_COLUMN_NAME);
                fieldList.addAll(neo.getColumnNameList(AliasParser.getOrigin(tableName)));
            }
        }
        this.tableFieldsMap.compute(tableName, (k, v) -> {
            if (null == v) {
                return new HashSet<>(fieldList);
            } else {
                v.addAll(fieldList);
                return v;
            }
        });
        return this;
    }

    public static boolean isEmpty(Columns columns) {
        return null == columns || columns.isEmpty();
    }

    public static boolean isEmpty(Collection<Columns> columnsList) {
        return null == columnsList || columnsList.isEmpty();
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
                    return AliasParser.getAlias(tableName) + "." + c;
                }
                return c;
            });
        }).collect(Collectors.toSet()));
    }

    public Set<String> getFieldSets() {
        return tableFieldsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Columns append(Columns columns) {
        if (Columns.isEmpty(columns)) {
            return this;
        }
        this.tableFieldsMap.putAll(columns.getTableFieldsMap());
        return this;
    }

    public Columns append(String... cs) {
        return table(DEFAULT_TABLE, cs);
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
