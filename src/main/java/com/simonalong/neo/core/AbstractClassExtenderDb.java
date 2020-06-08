package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/8 8:23 AM
 */
public abstract class AbstractClassExtenderDb extends AbstractBaseDb {

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        NeoMap data = one(tableName, columns, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        NeoMap data = one(tableName, columns, key);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        NeoMap data = one(tableName, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Number id) {
        NeoMap data = one(tableName, id);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }


    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, columns, searchMap), tClass);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, searchMap), tClass);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return NeoMap.asArray(list(tableName, columns), tClass);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, searchMap, page), tClass);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, searchMap, page), tClass);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, page), tClass);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return NeoMap.asArray(page(tableName, page), tClass);
    }

    @Override
    public Boolean exist(String tableName, NeoMap searchMap) {
        return NeoMap.isUnEmpty(one(tableName, searchMap));
    }

    @Override
    public Boolean exist(String tableName, Object entity) {
        return null != one(tableName, entity);
    }
}
