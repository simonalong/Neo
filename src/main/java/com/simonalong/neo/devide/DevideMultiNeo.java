package com.simonalong.neo.devide;

import com.simonalong.neo.*;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.exception.NeoNotSupport;
import com.simonalong.neo.express.SearchExpress;
import com.simonalong.neo.util.CharSequenceUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.simonalong.neo.NeoConstant.*;

/**
 * 分库分表中的多库多表查询处理
 * <p>
 * 谨记：该类处理的是扫描多库和多表，所以性能会不是很高。对于单个查询，查找到返回，对于集合查询，则会查找所有的库的所有表进行返回
 *
 * @author shizi
 * @since 2020/6/11 2:41 PM
 */
@Slf4j
public class DevideMultiNeo extends AbstractBaseQuery {

    /**
     * 待分库的库集合
     */
    private List<Neo> dbList;
    private Neo defaultDb;
    /**
     * 表的哈希处理映射, key：表名，value表的哈希信息
     */
    private Map<String, TableDevideConfig> devideTableInfoMap;

    public DevideMultiNeo(List<Neo> dbList, Neo defaultDb, Map<String, TableDevideConfig> devideTableInfoMap) {
        this.dbList = dbList;
        this.defaultDb = defaultDb;
        this.devideTableInfoMap = devideTableInfoMap;
    }

    private List<String> getActTableList(String tableName) {
        if (devideTableInfoMap.containsKey(tableName)) {
            return devideTableInfoMap.get(tableName).getActTableNameList();
        }
        return Collections.singletonList(tableName);
    }

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, columns, searchMap));
    }

    @Override
    public NeoMap one(String tableName, Columns columns, SearchExpress searchExpress){
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, columns, searchExpress));
    }

    @Override
    public <T> T one(String tableName, Columns columns, T entity) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, columns, entity));
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, columns, key));
    }

    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, searchMap));
    }

    @Override
    public <T> T one(String tableName, T entity) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, entity));
    }

    @Override
    public NeoMap one(String tableName, Number key) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, key));
    }

    @Override
    public NeoMap one(String tableName, SearchExpress searchExpress) {
        return executeOne(tableName, (db, actTableName) -> db.one(actTableName, searchExpress));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, columns, searchMap));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, SearchExpress searchExpress) {
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, columns, searchExpress));
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, columns, entity));
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, searchMap));
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, entity));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    public List<NeoMap> list(String tableName, SearchExpress searchExpress){
        return executeList(tableName, (db, actTableName) -> db.list(actTableName, searchExpress));
    }

    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, tClass, field, searchMap));
    }

    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, tClass, field, entity));
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return executeOne(tableName, (db, actTableName) -> db.value(tClass, actTableName, field, searchMap));
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, SearchExpress searchExpress) {
        return executeOne(tableName, (db, actTableName) -> db.value(tClass, actTableName, field, searchExpress));
    }

    @SuppressWarnings("all")
    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {
        return executeOne(tableName, (db, actTableName) -> db.value(tClass, actTableName, field, entity));
    }

    @SuppressWarnings("all")
    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, field, searchMap));
    }

    @Override
    public String value(String tableName, String field, SearchExpress searchExpress) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, field, searchExpress));
    }

    @SuppressWarnings("all")
    @Override
    public String value(String tableName, String field, Object entity) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, field, entity));
    }

    @Override
    public String value(String tableName, String field, Number id) {
        return executeOne(tableName, (db, actTableName) -> db.value(actTableName, field, id));
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.values(tClass, actTableName, field, searchMap));
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return executeList(tableName, (db, actTableName) -> db.values(tClass, actTableName, field, entity));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.values(tClass, actTableName, field, searchMap));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, SearchExpress searchExpress) {
        return executeList(tableName, (db, actTableName) -> db.values(tClass, actTableName, field, searchExpress));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity) {
        return executeList(tableName, (db, actTableName) -> db.values(tClass, actTableName, field, entity));
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.values(actTableName, field, searchMap));
    }

    @Override
    public List<String> values(String tableName, String field, SearchExpress searchExpress) {
        return executeList(tableName, (db, actTableName) -> db.values(actTableName, field, searchExpress));
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        return executeList(tableName, (db, actTableName) -> db.values(actTableName, field, entity));
    }

    @Override
    public List<String> values(String tableName, String field) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(tClass, actTableName, field, searchMap));
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, SearchExpress searchExpress) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(tClass, actTableName, field, searchExpress));
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, Object entity) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(tClass, actTableName, field, entity));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, NeoMap searchMap) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(actTableName, field, searchMap));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, SearchExpress searchExpress) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(actTableName, field, searchExpress));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, Object entity) {
        return executeList(tableName, (db, actTableName) -> db.valuesOfDistinct(actTableName, field, entity));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }


    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return executePage(searchMap, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, columns, searchMap, extendPage)));
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, SearchExpress searchExpress, NeoPage page){
        return executePage(searchExpress, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, columns, searchExpress, extendPage)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        NeoMap searchMap = NeoMap.from(entity, NeoMap.NamingChg.UNDERLINE);
        List<NeoMap> dataMapList = executePage(searchMap, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, columns, searchMap, extendPage)));
        return NeoMap.asArray(dataMapList, NeoMap.NamingChg.UNDERLINE, (Class<T>) entity.getClass());
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return executePage(searchMap, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, searchMap, extendPage)));
    }

    @Override
    public List<NeoMap> page(String tableName, SearchExpress searchExpress, NeoPage page) {
        return executePage(searchExpress, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, searchExpress, extendPage)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        NeoMap searchMap = NeoMap.from(entity, NeoMap.NamingChg.UNDERLINE);
        List<NeoMap> dataMapList = executePage(searchMap, page, (extendPage) -> executeList(tableName, (db, actTableName) -> db.page(actTableName, searchMap, extendPage)));
        return NeoMap.asArray(dataMapList, NeoMap.NamingChg.UNDERLINE, (Class<T>) entity.getClass());
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        return executeOneToList(tableName, (db, actTableName) -> db.count(actTableName, searchMap)).stream().reduce(Integer::sum).orElse(0);
    }

    @Override
    public Integer count(String tableName, SearchExpress searchExpress) {
        return executeOneToList(tableName, (db, actTableName) -> db.count(actTableName, searchExpress)).stream().reduce(Integer::sum).orElse(0);
    }

    @Override
    public Integer count(String tableName, Object entity) {
        return executeOneToList(tableName, (db, actTableName) -> db.count(actTableName, entity)).stream().reduce(Integer::sum).orElse(0);
    }

    @Override
    public Integer count(String tableName) {
        return executeOneToList(tableName, Neo::count).stream().reduce(Integer::sum).orElse(0);
    }

    /**
     * 回调执行器，找到一个就返回
     *
     * @param tableName 逻辑表名
     * @param function  回调处理
     * @param <T>       返回类型
     * @return 返回类型的值
     */
    private <T> T executeOne(String tableName, BiFunction<Neo, String, T> function) {
        if (null != dbList && !dbList.isEmpty()) {
            for (Neo db : dbList) {
                T result = doExecuteOne(tableName, db, function);
                if (null != result) {
                    return result;
                }
            }
        } else if (null != defaultDb) {
            return doExecuteOne(tableName, defaultDb, function);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T doExecuteOne(String tableName, Neo db, BiFunction<Neo, String, T> function) {
        List<String> tableList = getActTableList(tableName);
        for (String actTableName : tableList) {
            T result = function.apply(db, actTableName);
            if (result instanceof NeoMap) {
                if (NeoMap.isUnEmpty(NeoMap.class.cast(result))) {
                    return result;
                } else {
                    return (T) NeoMap.of();
                }
            }
            if (null != result) {
                return result;
            }
        }
        return null;
    }

    /**
     * 回调执行器，找到所有的数据合并返回
     *
     * @param tableName 逻辑表名
     * @param function  回调处理
     * @param <T>       返回类型
     * @return 返回类型的值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T executeList(String tableName, BiFunction<Neo, String, T> function) {
        List resultList = new ArrayList<>();
        if (null != dbList && !dbList.isEmpty()) {
            for (Neo db : dbList) {
                doExecuteList(tableName, db, resultList, function);
            }
        } else if (null != defaultDb) {
            doExecuteList(tableName, defaultDb, resultList, function);
        }

        return (T) resultList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> void doExecuteList(String tableName, Neo db, List resultList, BiFunction<Neo, String, T> function) {
        List<String> tableList = getActTableList(tableName);
        for (String actTableName : tableList) {
            T result = function.apply(db, actTableName);
            if (null != result) {
                if (result instanceof List) {
                    List dataList = (List) result;
                    if (!dataList.isEmpty()) {
                        resultList.addAll(dataList);
                    }
                } else {
                    resultList.add(result);
                }
            }
        }
    }

    /**
     * 将回调为一个数据的值进行合并为多个
     *
     * @param tableName 表名
     * @param function  回调
     * @param <T>       一个回调返回的类型
     * @return 回调返回的类型的集合
     */
    @SuppressWarnings("all")
    private <T> List<T> executeOneToList(String tableName, BiFunction<Neo, String, T> function) {
        List resultList = new ArrayList();
        if (null != dbList && !dbList.isEmpty()) {
            for (Neo db : dbList) {
                doExecuteList(tableName, db, resultList, function);
            }
        } else if (null != defaultDb) {
            doExecuteList(tableName, defaultDb, resultList, function);
        }

        return resultList;
    }

    /**
     * 多库或者多表数据分页的合并
     *
     * @param searchObject 搜索条件：NeoMap类型或者Express表达式类型
     * @param page         分页
     * @param function     原始数据生成回调
     * @return 分页过滤处理后的数据
     */
    @SuppressWarnings("unchecked")
    private List<NeoMap> executePage(Object searchObject, NeoPage page, Function<NeoPage, List<NeoMap>> function) {
        NeoPage extendPage = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
        Integer startIndex = page.getStartIndex();
        Integer pageSize = page.getPageSize();
        List<NeoMap> resultList = function.apply(extendPage);
        // 获取排序条件
        List<ColumnSortConfig> columnSortConfigList = getColumnAndSortList(searchObject);
        if (!resultList.isEmpty()) {
            // 多数据的归并后排序
            resultList = resultList.stream().sorted((a, b) -> {
                for (ColumnSortConfig sortConfig : columnSortConfigList) {
                    Comparable<Object> comparableLeft = a.get(Comparable.class, sortConfig.getColumnName());
                    Comparable<Object> comparableRight = b.get(Comparable.class, sortConfig.getColumnName());
                    Integer sort = sortConfig.getSort();
                    if (1 == sort) {
                        int compareResult = comparableLeft.compareTo(comparableRight);
                        if (0 != compareResult) {
                            return compareResult;
                        }
                    } else {
                        int compareResult = comparableRight.compareTo(comparableLeft);
                        if (0 != compareResult) {
                            return compareResult;
                        }
                    }
                }
                return 0;
            }).collect(Collectors.toList());
            if (startIndex + pageSize < resultList.size()) {
                return resultList.subList(startIndex, startIndex + pageSize);
            } else {
                return Collections.emptyList();
            }
        }
        return resultList;
    }

    /**
     * 从neoMap中获取到order by语句
     *
     * <p>
     *     比如：{@code order by `create_time`, `id` desc, `group` asc"} 转换之后变成
     *     {@code [{"columnName":"`create_time`","sort":0},{"columnName":"`id`","sort":0},{"columnName":"`group`","sort":1}]}
     *
     * @param searchObject 搜索条件
     * @return 列排序配置
     */
    public static List<ColumnSortConfig> getColumnAndSortList(Object searchObject) {
        String orderByValue = null;
        if(searchObject instanceof NeoMap) {
            NeoMap searchMap = (NeoMap) searchObject;
            if (searchMap.containsKey(ORDER_BY)) {
                orderByValue = searchMap.getString(ORDER_BY);
            }
        } else if (searchObject instanceof SearchExpress) {
            SearchExpress searchExpress = (SearchExpress) searchObject;
            orderByValue = searchExpress.getFirstOperateStr(ORDER_BY);
            if (CharSequenceUtil.isNotEmpty(orderByValue)) {
                orderByValue = orderByValue.trim();
                orderByValue = orderByValue.substring(NeoConstant.GROUP_BY.length());
            }
        }

        NeoQueue<String> neoQueue = NeoQueue.of();
        if (null != orderByValue) {
            while (orderByValue.contains(DESC) || orderByValue.contains(ASC)) {
                int descIndex = orderByValue.indexOf(DESC);
                int ascIndex = orderByValue.indexOf(ASC);
                String subStr;
                if ((-1 != descIndex && descIndex < ascIndex) || -1 == ascIndex) {
                    subStr = orderByValue.substring(0, descIndex);
                    Arrays.stream(subStr.split(",")).map(String::trim).filter(CharSequenceUtil::isNotEmpty).forEach(e -> neoQueue.add(e + " " + DESC));
                    orderByValue = orderByValue.substring(descIndex + DESC.length());
                }
                if ((-1 != ascIndex && ascIndex < descIndex) || -1 == descIndex) {
                    subStr = orderByValue.substring(0, ascIndex);
                    Arrays.stream(subStr.split(",")).map(String::trim).filter(CharSequenceUtil::isNotEmpty).forEach(e -> neoQueue.add(e + " " + ASC));
                    orderByValue = orderByValue.substring(ascIndex + ASC.length());
                }
            }
        }

        return neoQueue.stream().map(e->{
            ColumnSortConfig sortConfig = new ColumnSortConfig();
            if (e.contains(" ")) {
                int spaceIndex = e.indexOf(" ");
                sortConfig.setColumnName(e.substring(0, spaceIndex));
                sortConfig.setSortStr(e.substring(spaceIndex + 1));
                return sortConfig;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Getter
    @Setter
    private static class ColumnSortConfig {

        /**
         * 列名
         */
        private String columnName;
        /**
         * 0：desc降序， 1：asc升序
         */
        private Integer sort = 1;

        void setSortStr(String sortStr) {
            sortStr = sortStr.toLowerCase(Locale.ENGLISH);
            if (DESC.equals(sortStr)) {
                sort = 0;
            } else {
                sort = 1;
            }
        }
    }
}
