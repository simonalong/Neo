package com.simonalong.neo;

import com.simonalong.neo.core.join.AbstractBaseJoinner;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.sql.builder.JoinSqlBuilder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author shizi
 * @since 2020/5/23 6:39 PM
 */
public class NeoJoiner extends AbstractBaseJoinner {

    private final Neo neo;
    @Getter
    private TableJoinOn tableJoinOn;

    public NeoJoiner(Neo neo) {
        this.neo = neo;
    }

    public NeoJoiner(Neo neo, String tableName) {
        this.neo = neo;
        this.tableJoinOn = new TableJoinOn(tableName);
    }

    public NeoJoiner join(String leftTableName, String rightTableName){
        tableJoinOn.innerJoin(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner leftJoin(String leftTableName, String rightTableName) {
        tableJoinOn.leftJoin(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner rightJoin(String leftTableName, String rightTableName) {
        tableJoinOn.rightJoin(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner innerJoin(String leftTableName, String rightTableName) {
        tableJoinOn.innerJoin(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner outerJoin(String leftTableName, String rightTableName) {
        tableJoinOn.outerJoin(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner leftJoinExceptInner(String leftTableName, String rightTableName) {
        tableJoinOn.leftJoinExceptInner(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner rightJoinExceptInner(String leftTableName, String rightTableName) {
        tableJoinOn.rightJoinExceptInner(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner outerJoinExceptInner(String leftTableName, String rightTableName) {
        tableJoinOn.outerJoinExceptInner(leftTableName, rightTableName);
        return this;
    }

    public NeoJoiner on(String leftId, String rightId) {
        tableJoinOn.on(leftId, rightId);
        return this;
    }


    @Override
    public TableMap one(Columns columns, TableMap searchMap) {
        return one(columns, tableJoinOn, searchMap);
    }

    @Override
    public TableMap one(Columns columns, SearchQuery searchQuery) {
        return one(columns, tableJoinOn, searchQuery);
    }

    @Override
    public <T> T one(Class<T> tClass, Columns columns, TableMap searchMap) {
        return one(tClass, columns, tableJoinOn, searchMap);
    }

    @Override
    public <T> T one(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return one(tClass, columns, tableJoinOn, searchQuery);
    }

    @Override
    public List<TableMap> list(Columns columns, TableMap searchMap) {
        return list(columns, tableJoinOn, searchMap);
    }

    @Override
    public List<TableMap> list(Columns columns, SearchQuery searchQuery) {
        return list(columns, tableJoinOn, searchQuery);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, Columns columns, TableMap searchMap) {
        return list(tClass, columns, tableJoinOn, searchMap);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return list(tClass, columns, tableJoinOn, searchQuery);
    }

    @Override
    public String value(Columns columns, TableMap searchMap) {
        return value(columns, tableJoinOn, searchMap);
    }

    @Override
    public String value(Columns columns, SearchQuery searchQuery) {
        return value(columns, tableJoinOn, searchQuery);
    }

    @Override
    public <T> T value(Class<T> tClass, Columns columns, TableMap searchMap) {
        return value(tClass, columns, tableJoinOn, searchMap);
    }

    @Override
    public <T> T value(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return value(tClass, columns, tableJoinOn, searchQuery);
    }

    @Override
    public List<String> values(Columns columns, TableMap searchMap) {
        return values(columns, tableJoinOn, searchMap);
    }

    @Override
    public List<String> values(Columns columns, SearchQuery searchQuery) {
        return values(columns, tableJoinOn, searchQuery);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, Columns columns, TableMap searchMap) {
        return values(tClass, columns, tableJoinOn, searchMap);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return values(tClass, columns, tableJoinOn, searchQuery);
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableMap searchMap) {
        return valuesOfDistinct(tClass, columns, tableJoinOn, searchMap);
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return valuesOfDistinct(tClass, columns, tableJoinOn, searchQuery);
    }

    @Override
    public List<String> valuesOfDistinct(Columns columns, TableMap searchMap) {
        return valuesOfDistinct(columns, tableJoinOn, searchMap);
    }

    @Override
    public List<String> valuesOfDistinct(Columns columns, SearchQuery searchQuery) {
        return valuesOfDistinct(columns, tableJoinOn, searchQuery);
    }

    @Override
    public List<TableMap> page(Columns columns, TableMap searchMap, PageReq<?> pageReq) {
        return page(columns, tableJoinOn, searchMap, pageReq);
    }

    @Override
    public List<TableMap> page(Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return page(columns, tableJoinOn, searchQuery, pageReq);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, Columns columns, TableMap searchMap, PageReq<?> pageReq) {
        return page(tClass, columns, tableJoinOn, searchMap, pageReq);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return page(tClass, columns, tableJoinOn, searchQuery, pageReq);
    }

    @Override
    public Integer count(TableMap searchMap) {
        return count(tableJoinOn, searchMap);
    }

    @Override
    public Integer count(SearchQuery searchQuery) {
        return count(tableJoinOn, searchQuery);
    }

    @Override
    public CompletableFuture<TableMap> oneAsync(Columns joinColumns, TableMap tableMap) {
        return oneAsync(joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap) {
        return oneAsync(tClass, joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public CompletableFuture<List<TableMap>> listAsync(Columns joinColumns, TableMap tableMap) {
        return listAsync(joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap) {
        return listAsync(tClass, joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public CompletableFuture<String> valueAsync(Columns joinColumns, TableMap tableMap) {
        return valueAsync(joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap) {
        return valueAsync(tClass, joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(Columns joinColumns, TableMap tableMap) {
        return valuesAsync(joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap) {
        return valuesAsync(tClass, joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap) {
        return valuesOfDistinctAsync(tClass, joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(tClass, joinColumns, tableJoinOn, searchQuery);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableMap tableMap) {
        return valuesOfDistinctAsync(joinColumns, tableJoinOn, tableMap);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(joinColumns, tableJoinOn, searchQuery);
    }

    @Override
    public CompletableFuture<List<TableMap>> pageAsync(Columns joinColumns, TableMap tableMap, PageReq<?> pageReq) {
        return pageAsync(joinColumns, tableJoinOn, tableMap, pageReq);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap, PageReq<?> pageReq) {
        return pageAsync(tClass, joinColumns, tableJoinOn, tableMap, pageReq);
    }

    @Override
    @Deprecated
    public CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableMap tableMap, PageReq<?> pageReq) {
        return getPageAsync(joinColumns, tableJoinOn, tableMap, pageReq);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableMap tableMap, PageReq<?> pageReq) {
        return getPageAsync(tClass, joinColumns, tableJoinOn, tableMap, pageReq);
    }

    @Override
    @Deprecated
    public CompletableFuture<Integer> countAsync(TableMap tableMap) {
        return countAsync(tableJoinOn, tableMap);
    }


    @Override
    @Deprecated
    public TableMap one(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeOne(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public TableMap one(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeOne(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeOne(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeOne(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeList(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeList(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeList(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeList(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public String value(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValue(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public String value(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValue(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValue(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValue(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public List<String> values(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValues(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public List<String> values(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValues(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValues(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValues(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValues(tClass, JoinSqlBuilder.buildDistinct(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValues(tClass, JoinSqlBuilder.buildDistinct(neo.getTenantHandler(), columns, tableJoinOn, searchQuery),
            JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeValues(JoinSqlBuilder.buildDistinct(neo.getTenantHandler(), columns, tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeValues(JoinSqlBuilder.buildDistinct(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return neo.exePage(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), neoPage, JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage) {
        return neo.exePage(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), neoPage, JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return neo.exePage(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), neoPage, JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage) {
        return neo.exePage(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), neoPage,
            JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }


    @Override
    @Deprecated
    public List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return neo.exePage(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), pageReq, JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq) {
        return neo.exePage(JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), pageReq, JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return neo.exePage(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchMap), pageReq, JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq) {
        return neo.exePage(tClass, JoinSqlBuilder.build(neo.getTenantHandler(), columns, tableJoinOn, searchQuery), pageReq,
            JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }


    @Override
    @Deprecated
    public Integer count(TableJoinOn tableJoinOn, TableMap searchMap) {
        return neo.exeCount(JoinSqlBuilder.buildCount(neo.getTenantHandler(), tableJoinOn, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
    }

    @Override
    @Deprecated
    public Integer count(TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return neo.exeCount(JoinSqlBuilder.buildCount(neo.getTenantHandler(), tableJoinOn, searchQuery), JoinSqlBuilder.buildValueList(searchQuery).toArray());
    }
}
