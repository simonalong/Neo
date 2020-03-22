package com.simonalong.neo;

import static com.simonalong.neo.NeoConstant.ALIAS_DOM;
import static com.simonalong.neo.NeoConstant.ALL_COLUMN_NAME;
import static com.simonalong.neo.NeoConstant.COLUMN_PRE;

import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.db.AliasParser;
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
import lombok.EqualsAndHashCode;
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
    /**
     * key为表名，value为表列名的转换后的名字的集合
     */
    @Getter
    private Map<String, Set<ColumnValue>> tableFieldsMap = new LinkedHashMap<>();

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
        if (null != excludeFields) {
            excludeFieldList = Stream.of(excludeFields).collect(Collectors.toSet());
        }

        Set<String> finalExcludeFieldList = excludeFieldList;
        List<String> fieldNames = Arrays.stream(tClass.getDeclaredFields())
            .filter(field -> {
                if (!finalExcludeFieldList.isEmpty()) {
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

    /**
     * 基于表获取列的信息
     * @param tableName 表名
     * @param fields 列名
     * @return 实体
     */
    public Columns table(String tableName, String... fields) {
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
                return new HashSet<>(decorateColumn(tableName, fieldList));
            } else {
                v.addAll(decorateColumn(tableName, fieldList));
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
     * 获取装饰之后的列名集合
     * @return 转换之后的列名集合
     */
    public Set<String> getFieldSets() {
        return tableFieldsMap.values().stream().flatMap(t->t.stream().map(ColumnValue::getCurrentValue)).collect(Collectors.toSet());
    }

    /**
     * 获取原列名的集合
     * @return 原列名集合
     */
    public Set<String> getMetaFieldSets() {
        return tableFieldsMap.values().stream().flatMap(t->t.stream().map(ColumnValue::getMetaValue)).collect(Collectors.toSet());
    }

    public Columns append(Columns columns) {
        if (Columns.isEmpty(columns)) {
            return this;
        }

        Map<String, Set<ColumnValue>> dataMap = columns.getTableFieldsMap();
        // 将columns中的数据合并到当前的数据中，不存在则返回columns中的，表存在的，则添加合并
        dataMap.forEach((k1, v1)-> tableFieldsMap.compute(k1, (k, v)->{
            if (null == v) {
                return v1;
            }else{
                v.addAll(v1);
                return v;
            }
        }));
        return this;
    }

    public Columns append(String... cs) {
        return table(DEFAULT_TABLE, cs);
    }

    public boolean contains(String data) {
        return tableFieldsMap.entrySet().stream().anyMatch(e -> {
            return e.getValue().stream().anyMatch(c -> c.getMetaValue().equals(data));
        });
    }

    public void remove(String key) {
        tableFieldsMap.forEach((key1, value) -> value.removeIf(v->v.getMetaValue().equals(key)));
    }

    public boolean isEmpty() {
        return tableFieldsMap.isEmpty();
    }

    public Stream<String> stream(String tableName) {
        return tableFieldsMap.get(tableName).stream().map(ColumnValue::getCurrentValue);
    }

    public Stream<String> stream() {
        return stream(DEFAULT_TABLE);
    }

    public Stream<String> streamMeta(String tableName) {
        return tableFieldsMap.get(tableName).stream().map(ColumnValue::getMetaValue);
    }

    public Stream<String> streamMeta() {
        return streamMeta(DEFAULT_TABLE);
    }

    /**
     * 返回select 后面选择的列对应的数据库的字段
     *
     * @return 比如：`xxx`, `kkk`
     */
    @Override
    public String toString() {
        return tableFieldsMap.toString();
    }

    public String toSelectString(){
        return String.join(", ", tableFieldsMap.values().stream()
            .flatMap(c->c.stream().map(ColumnValue::getCurrentValue)).collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Columns) {
            return this.getTableFieldsMap().equals(Columns.class.cast(obj).getTableFieldsMap());
        }
        return false;
    }

    /**
     * 给列进行装饰
     * <p>{@code c1 到 `c``}{@code table as t1, 则转换为t1.`c` as t1_NDom_c}
     * @param tableName 表名
     * @param columns 列集合
     * @return 转换之后的列名
     */
    private List<ColumnValue> decorateColumn(String tableName, Collection<String> columns) {
        return columns.stream().map(c -> new ColumnValue(tableName, c)).collect(Collectors.toList());
    }

    /**
     * 存放列名数据的实体
     */
    @EqualsAndHashCode(of = {"tableName", "currentValue"})
    public static class ColumnValue {

        /**
         * 表名
         */
        @Getter
        private String tableName;
        /**
         * 列的原名
         */
        @Getter
        private String metaValue;
        /**
         * 列转换之后的名字
         */
        @Getter
        private String currentValue;

        ColumnValue(String tableName, String fieldName) {
            this.tableName = tableName;
            this.metaValue = fieldName;
            this.currentValue = fieldName;
            if (!fieldName.startsWith(COLUMN_PRE) || !fieldName.endsWith(COLUMN_PRE)) {
                this.currentValue = toDbField(fieldName);
            }

            if (!tableName.equals(DEFAULT_TABLE)) {
                String alias = AliasParser.getAlias(tableName);
                this.currentValue = alias + "." + filterColumnToFinal(fieldName) + " as " + alias + ALIAS_DOM + reduceDom(fieldName);
            }
        }

        /**
         * 筛选处理为合法的sql字段
         *
         * 对于这么几种情况进行处理：{@code name --> `name`} {@code db.name --> db.`name`} {@code db.`name` --> db.`name`}
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
            return COLUMN_PRE + field + COLUMN_PRE;
        }

        /**
         * 获取列名的原名
         * <p>{@code `c` 到 c}
         * @param columnStr 装饰的列名
         * @return 原列名
         */
        private String reduceDom(String columnStr){
            if (columnStr.startsWith(COLUMN_PRE) && columnStr.endsWith(COLUMN_PRE)){
                return columnStr.substring(1, columnStr.length() - 1);
            }
            return columnStr;
        }
    }
}
