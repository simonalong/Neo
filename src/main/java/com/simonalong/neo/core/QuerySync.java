package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoPageRsp;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.Express;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/11 2:58 PM
 */
public interface QuerySync extends Sync {

    NeoMap one(String tableName, Columns columns, NeoMap searchMap);

    <T> T one(String tableName, Columns columns, T entity);

    NeoMap one(String tableName, Columns columns, Number key);

    NeoMap one(String tableName, NeoMap searchMap);

    <T> T one(String tableName, T entity);

    NeoMap one(String tableName, Number id);

    /**
     * 查询一行（一个实体）
     * @param tableName 表名
     * @param searchExpress 复杂结构表达式
     * @return 一个实体对应的类型
     */
    NeoMap one(String tableName, Express searchExpress);


    default <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        NeoMap data = one(tableName, columns, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    default <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        NeoMap data = one(tableName, columns, key);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    default <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        NeoMap data = one(tableName, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    default <T> T one(Class<T> tClass, String tableName, Number id) {
        NeoMap data = one(tableName, id);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    default <T> T one(Class<T> tClass, String tableName, Express searchExpress) {
        NeoMap data = one(tableName, searchExpress);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }


    List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap);

    <T> List<T> list(String tableName, Columns columns, T entity);

    List<NeoMap> list(String tableName, NeoMap searchMap);

    <T> List<T> list(String tableName, T entity);

    List<NeoMap> list(String tableName, Columns columns);

    List<NeoMap> list(String tableName, Express searchExpress);


    default <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, columns, searchMap), tClass);
    }

    default <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, searchMap), tClass);
    }

    default <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return NeoMap.asArray(list(tableName, columns), tClass);
    }

    default <T> List<T> list(Class<T> tClass, String tableName, Express searchExpress) {
        return NeoMap.asArray(list(tableName, searchExpress), tClass);
    }


    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, Object entity);

    <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> T value(Class<T> tClass, String tableName, String field, Object entity);

    String value(String tableName, String field, NeoMap searchMap);

    String value(String tableName, String field, Express searchExpress);

    String value(String tableName, String field, Object entity);

    String value(String tableName, String field, Number id);


    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity);

    <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String tableName, String field, Express searchExpress);

    <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity);

    List<String> values(String tableName, String field, NeoMap searchMap);

    List<String> values(String tableName, String field, Express searchExpress);

    List<String> values(String tableName, String field, Object entity);

    List<String> values(String tableName, String field);


    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(String tableName, Columns columns, Express searchExpress, NeoPage page);

    <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page);

    List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(String tableName, Express searchExpress, NeoPage page);

    <T> List<T> page(String tableName, T entity, NeoPage page);

    List<NeoMap> page(String tableName, Columns columns, NeoPage page);

    List<NeoMap> page(String tableName, NeoPage page);

    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, searchMap, page), tClass);
    }

    default <T> List<T> page(Class<T> tClass, String tableName, Express searchExpress, NeoPage page) {
        return NeoMap.asArray(page(tableName, searchExpress, page), tClass);
    }

    default <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, searchMap, page), tClass);
    }

    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, page), tClass);
    }

    default <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return NeoMap.asArray(page(tableName, page), tClass);
    }


    default NeoPageRsp<NeoMap> getPage(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, columns, searchMap, page), count(tableName, searchMap));
    }

    default <T> NeoPageRsp<T> getPage(String tableName, Columns columns, T entity, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, columns, entity, page), count(tableName, entity));
    }

    default NeoPageRsp<NeoMap> getPage(String tableName, NeoMap searchMap, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, searchMap, page), count(tableName, searchMap));
    }

    default NeoPageRsp<NeoMap> getPage(String tableName, Express searchExpress, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, searchExpress, page), count(tableName, searchExpress));
    }

    default <T> NeoPageRsp<T> getPage(String tableName, T entity, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, entity, page), count(tableName, entity));
    }

    default NeoPageRsp<NeoMap> getPage(String tableName, Columns columns, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, columns, page), count(tableName, columns));
    }

    default NeoPageRsp<NeoMap> getPage(String tableName, NeoPage page) {
        return new NeoPageRsp<>(page(tableName, page), count(tableName));
    }

    default <T> NeoPageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return new NeoPageRsp<>(page(tClass, tableName, columns, searchMap, page), count(tableName, searchMap));
    }

    default <T> NeoPageRsp<T> getPage(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return new NeoPageRsp<>(page(tClass, tableName, searchMap, page), count(tableName, searchMap));
    }

    default <T> NeoPageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return new NeoPageRsp<>(page(tClass, tableName, columns, page), count(tableName));
    }

    default <T> NeoPageRsp<T> getPage(Class<T> tClass, String tableName, NeoPage page) {
        return new NeoPageRsp<>(page(tClass, tableName, page), count(tableName));
    }


    Integer count(String tableName, NeoMap searchMap);

    Integer count(String tableName, Express searchExpress);

    Integer count(String tableName, Object entity);

    Integer count(String tableName);


    default Boolean exist(String tableName, NeoMap searchMap) {
        return NeoMap.isUnEmpty(one(tableName, searchMap));
    }

    default Boolean exist(String tableName, Express searchExpress) {
        return NeoMap.isUnEmpty(one(tableName, searchExpress));
    }

    default Boolean exist(String tableName, Object entity) {
        return null != one(tableName, entity);
    }
}
