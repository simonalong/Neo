package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public interface TableSync extends Sync {

    /**
     * 新增数据
     * @param dataMap 待插入数据
     * @return 插入后的数据
     */
    NeoMap insert(NeoMap dataMap);

    /**
     * 新增数据
     * @param object 待插入的数据
     * @param <T> 数据类型对应的泛型
     * @return 插入后的数据
     */
    <T> T insert(T object);


    /**
     * 不存在则新增，存在则忽略
     *
     * @param dataMap 新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入后的数据
     */
    NeoMap insertOfUnExist(NeoMap dataMap, String... searchColumnKey);

    /**
     * 不存在则新增，存在则忽略
     *
     * @param object 新增的实体
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 插入后的数据
     */
    <T> T insertOfUnExist(T object, String... searchColumnKey);


    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param dataMap 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    NeoMap save(NeoMap dataMap, String... searchColumnKey);

    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param object 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 更新或者插入后的数据
     */
    <T> T save(T object, String... searchColumnKey);

    /**
     * 删除数据
     * @param searchMap 删除条件
     * @return 删除的个数
     */
    Integer delete(NeoMap searchMap);

    /**
     * 删除数据
     * @param searchExpress 复杂的删除条件
     * @return 删除个数
     */
    Integer delete(SearchExpress searchExpress);

    /**
     * 删除数据
     * @param object 实体条件
     * @param <T> 实体对应的泛型
     * @return 删除个数
     */
    <T> Integer delete(T object);

    /**
     * 删除数据
     * @param id 主键
     * @return 删除个数
     */
    Integer delete( Number id);


    /**
     * 更新数据
     * @param dataMap 待更新数据
     * @param searchMap 搜索条件
     * @return 更新后的数据
     */
    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    /**
     * 更新数据
     * @param dataMap 待更新的数据
     * @param searchExpress 复杂的搜索条件
     * @return 更新后的数据
     */
    NeoMap update(NeoMap dataMap, SearchExpress searchExpress);

    /**
     * 更新数据
     * @param setEntity 待设置的数据
     * @param searchMap 搜索条件
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    <T> T update(T setEntity, NeoMap searchMap);

    /**
     * 更新数据
     * @param setEntity 待更新的数据
     * @param searchExpress 复杂的搜索条件
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    <T> T update(T setEntity, SearchExpress searchExpress);

    /**
     * 更新数据
     * @param setEntity 待更新的数据
     * @param searchEntity 实体搜索条件
     * @param <T> 搜索条件对应的泛型
     * @return 更新后的数据
     */
    <T> T update(T setEntity, T searchEntity);

    /**
     * 更新数据
     * @param dataMap 待更新的数据
     * @param columns 将待更新中的数据key对应的value作为条件，其中由columns来指定
     * @return 更新后的数据
     */
    NeoMap update(NeoMap dataMap, Columns columns);

    /**
     * 更新数据
     * @param setMap 待更新的数据
     * @param searchEntity 更新条件
     * @param <T> 搜索条件对应的泛型
     * @return 更新后的数据
     */
    <T> NeoMap update(NeoMap setMap, T searchEntity);

    /**
     * 更新数据
     * @param entity 待更新的实体数据
     * @param columns 将待更新数据对应的列名作作为搜索条件，其中columns来配置哪个属性
     * @param <T> 待更新实体对应的泛型
     * @return 更新后的数据
     */
    <T> T update(T entity, Columns columns);

    /**
     * 更新数据
     * @param dataMap 待更新的数据，如果其中包含主键，则采用主键，否则按照全部作为条件搜索和更新
     * @return 更新后的数据
     */
    NeoMap update(NeoMap dataMap);

    /**
     * 更新数据
     * @param entity 待更新的数据，如果其中包含主键，则将主键对应的值作为搜索条件，否则就将全部作为条件搜索和更新
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    <T> T update(T entity);

    /**
     * 查询指定列的一行数据
     * @param columns 要查询的列名
     * @param searchMap 搜索条件
     * @return 一行数据
     */
    NeoMap one(Columns columns, NeoMap searchMap);

    /**
     * 查询指定列的一行数据
     * @param columns 要查询的列名
     * @param searchExpress 复杂搜索条件
     * @return 指定列的数据
     */
    NeoMap one(Columns columns, SearchExpress searchExpress);

    /**
     * 查询指定列的一个数据
     * @param columns 要查询的列名
     * @param entity 实体搜索条件
     * @param <T> 实体对应的泛型
     * @return 指定列的数据
     */
    <T> T one(Columns columns, T entity);

    /**
     * 查询指定列的一个数据
     * @param columns 要查询的列名
     * @param key 主键
     * @return 指定列的数据
     */
    NeoMap one(Columns columns, Number key);

    /**
     * 查询指定列的一行数据
     * @param searchMap 搜索条件
     * @return 对应的数据
     */
    NeoMap one(NeoMap searchMap);

    /**
     * 查询指定列的一行数据一行
     * @param searchExpress 复杂搜索条件
     * @return 对应的数据
     */
    NeoMap one(SearchExpress searchExpress);

    /**
     * 查询指定列的一行数据一行
     * @param entity 实体搜索条件
     * @param <T> 搜索对应的泛型
     * @return 对应的数据
     */
    <T> T one(T entity);

    /**
     * 查询指定列的一行数据一行
     * @param id 主键
     * @return 对应的数据
     */
    NeoMap one(Number id);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获得的数据的类型
     * @param columns 要获取的列
     * @param searchMap 搜索条件
     * @param <T> 获取的类型的泛型
     * @return 指定的某些列的类型的数据
     */
    <T> T one(Class<T> tClass, Columns columns, NeoMap searchMap);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获得的数据的类型
     * @param columns 要获取的列
     * @param searchExpress 复杂搜索条件
     * @param <T> 获取的类型的泛型
     * @return 指定列的对应类型的值
     */
    <T> T one(Class<T> tClass, Columns columns, SearchExpress searchExpress);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取的数据的类型
     * @param columns 要获取的列
     * @param key 主键
     * @param <T> 要获取的数据的类型的泛型
     * @return 指定列的对应类型的值
     */
    <T> T one(Class<T> tClass, Columns columns, Number key);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取的数据的类型
     * @param searchMap 搜索条件
     * @param <T> 待获取的数据类型的泛型
     * @return 指定类型的值
     */
    <T> T one(Class<T> tClass, NeoMap searchMap);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取数据的类型
     * @param searchExpress 复杂搜索条件
     * @param <T> 数据类型的泛型
     * @return 指定类型的值
     */
    <T> T one(Class<T> tClass, SearchExpress searchExpress);

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取数据的类型
     * @param id 主键
     * @param <T> 获取数据的类型对应的泛型
     * @return 指定类型的值
     */
    <T> T one(Class<T> tClass, Number id);


    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(Columns columns, NeoMap searchMap);

    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param searchExpress 复杂搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(Columns columns, SearchExpress searchExpress);

    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param entity 实体搜索条件
     * @param <T> 实体对应的类型的泛型
     * @return 多行数据
     */
    <T> List<T> list(Columns columns, T entity);

    /**
     * 获取多行数据
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(NeoMap searchMap);

    /**
     * 获取多行数据
     * @param searchExpress 复杂搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(SearchExpress searchExpress);

    /**
     * 获取多行数据
     * @param entity 实体作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 多行数据
     */
    <T> List<T> list(T entity);

    /**
     * 多行数据：指定列
     * @param columns 要回去的列名
     * @return 多行数据
     */
    List<NeoMap> list(Columns columns);

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param columns 指定列
     * @param searchMap 搜索条件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    <T> List<T> list(Class<T> tClass, Columns columns, NeoMap searchMap);

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param columns 指定列
     * @param searchExpress 复杂的搜索条件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    <T> List<T> list(Class<T> tClass, Columns columns, SearchExpress searchExpress);

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param searchMap 搜索交件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    <T> List<T> list(Class<T> tClass, NeoMap searchMap);

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param searchExpress 搜索条件
     * @param <T> 对应类型的泛型
     * @return 指定列的多行数据
     */
    <T> List<T> list(Class<T> tClass, SearchExpress searchExpress);

    /**
     * 多行数据：指定列
     * @param tClass   对应的类型
     * @param columns 指定列
     * @param <T> 对应类型的泛型
     * @return 指定列的多行数据
     */
    <T> List<T> list(Class<T> tClass, Columns columns);


    /**
     * 获取某行某列的某个值
     * @param tClass 值的对应类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 值的对应类型的泛型
     * @return 列对应的值
     */
    <T> T value(Class<T> tClass, String column, NeoMap searchMap);

    /**
     * 获取某行某列的某个值
     * @param tClass 值对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 值对应类型的泛型
     * @return 列对应的值
     */
    <T> T value(Class<T> tClass, String column, SearchExpress searchExpress);

    /**
     * 获取某行某列的某个值
     * @param tClass 值对应的类型
     * @param column 列名
     * @param entity 实体搜索条件
     * @param <T> 值对应的类型的泛型
     * @return 列对应的值
     */
    <T> T value(Class<T> tClass, String column, Object entity);

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param searchMap 搜索条件
     * @return 列对应的值
     */
    String value(String column, NeoMap searchMap);

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param searchExpress 搜索条件
     * @return 列对应的值
     */
    String value(String column, SearchExpress searchExpress);

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param entity 实体搜索条件
     * @return 返回的某个值
     */
    String value(String column, Object entity);

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param entity 实体搜索
     * @return 列的某个值
     */
    String value(String column, Number entity);


    /**
     * 获取一列的多个值
     *
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    <T> List<T> values(Class<T> tClass, String column, NeoMap searchMap);

    /**
     * 获取一列的多个值
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    <T> List<T> values(Class<T> tClass, String column, SearchExpress searchExpress);

    /**
     * 获取一列的多个值
     * @param tClass 列对应的类型
     * @param column 列名
     * @param entity 实体搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    <T> List<T> values(Class<T> tClass, String column, Object entity);

    /**
     * 获取一列的多个值
     * @param column 列名
     * @param searchMap 搜索条件
     * @return 一列的多个值
     */
    List<String> values(String column, NeoMap searchMap);

    /**
     * 获取一列的多个值
     * @param column 列名
     * @param searchExpress 搜索条件
     * @return 一列的多个值
     */
    List<String> values(String column, SearchExpress searchExpress);

    /**
     * 获取一列的多个值
     * @param column  列名
     * @param entity 实体搜索条件
     * @return 一列的多个值
     */
    List<String> values(String column, Object entity);

    /**
     * 获取一列的多个值
     * @param column 列名
     * @return 一列的多个值
     */
    List<String> values(String column);


    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 列对应类型的泛型
     * @return 一列对应的多个值
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String column, NeoMap searchMap);

    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列对应的多个值
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String column, SearchExpress searchExpress);

    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 类名
     * @param entity 实体搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列对应的多个值
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String column, Object entity);

    /**
     * 获取一列的多个值：去重
     * @param column    列名
     * @param searchMap 搜索条件
     * @return 一列对应的多个值
     */
    List<String> valuesOfDistinct(String column, NeoMap searchMap);

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @return 一列对应的多个值
     */
    List<String> valuesOfDistinct(String column, SearchExpress searchExpress);

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @param entity 实体搜索条件
     * @return 一列对应的多个值
     */
    List<String> valuesOfDistinct(String column, Object entity);

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @return 一列对应的多个值
     */
    List<String> valuesOfDistinct(String column);


    /**
     * 获取分页集合
     * @param columns 多个列
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page);

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(Columns columns, SearchExpress searchExpress, NeoPage page);

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param entity 实体搜索条件
     * @param page 分页信息
     * @param <T> 实体类型对应的泛型
     * @return 分页集合
     */
    <T> List<T> page(Columns columns, T entity, NeoPage page);

    /**
     * 获取分页集合
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(NeoMap searchMap, NeoPage page);

    /**
     * 获取分页集合
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(SearchExpress searchExpress, NeoPage page);

    /**
     * 获取分页集合
     * @param entity 实体搜索条件
     * @param page 分页信息
     * @param <T> 实体对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(T entity, NeoPage page);

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(Columns columns, NeoPage page);

    /**
     * 获取分页集合
     * @param page 分页信息
     * @return 分页集合
     */
    List<NeoMap> page(NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页対应的类型
     * @param columns 多个列
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, NeoMap searchMap, NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, SearchExpress searchExpress, NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param columns 多个列
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, Columns columns, NeoPage page);

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    <T> List<T> page(Class<T> tClass, NeoPage page);


    /**
     * 获取分页数据
     * @param columns 列名
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(columns, searchMap, page), count(searchMap));
    }

    /**
     * 获取分页数据
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(Columns columns, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(columns, searchExpress, page), count(searchExpress));
    }

    /**
     * 获取分页数据
     * @param columns 多个列名
     * @param entity 搜索实体
     * @param page 分页信息
     * @param <T> 搜索实体对应的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Columns columns, T entity, NeoPage page) {
        return new PageRsp<>(page(columns, entity, page), count(entity));
    }

    /**
     * 获取分页数据
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(searchMap, page), count(searchMap));
    }

    /**
     * 获取分页数据
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(searchExpress, page), count(searchExpress));
    }

    /**
     * 获取分页数据
     * @param entity 实体搜索条件
     * @param page 分页信息
     * @param <T> 实体类型对应的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(T entity, NeoPage page) {
        return new PageRsp<>(page(entity, page), count(entity));
    }

    /**
     * 获取分页数据
     * @param columns 多个列
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(Columns columns, NeoPage page) {
        return new PageRsp<>(page(columns, page), count(columns));
    }

    /**
     * 获取分页数据
     * @param page 分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(NeoPage page) {
        return new PageRsp<>(page(page), count());
    }

    /**
     * 获取分页数据
     * @param tClass 分页对应的类型
     * @param columns 多个列
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, searchMap, page), count(searchMap));
    }

    /**
     * 获取分页数据
     * @param tClass 分页数据对应的类型
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, searchExpress, page), count(searchExpress));
    }

    /**
     * 获取分页数据
     * @param tClass 分页数据对应的类型
     * @param searchMap 搜搜条件
     * @param page 分页信息
     * @param <T> 分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, searchMap, page), count(searchMap));
    }

    /**
     * 获取分页数据
     * @param tClass 分页数据对应的类型
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(tClass, searchExpress, page), count(searchExpress));
    }

    /**
     * 获取分页数据
     * @param tClass 分页数据对应的类型
     * @param columns 多个列
     * @param page 分页信息
     * @param <T> 分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, page), count());
    }

    /**
     * 获取分页数据
     * @param tClass 分页数据对应的类型
     * @param page 分页信息
     * @param <T> 分页数据类型对应的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, NeoPage page) {
        return new PageRsp<>(page(tClass, page), count());
    }


    /**
     * 获取个数
     * @param searchMap 搜索条件
     * @return 个数
     */
    Integer count(NeoMap searchMap);

    /**
     * 获取个数
     * @param searchExpress 复杂搜索条件
     * @return 个数
     */
    Integer count(SearchExpress searchExpress);

    /**
     * 获取个数
     * @param entity 实体搜索条件
     * @return 个数
     */
    Integer count(Object entity);

    /**
     * 获取个数
     * @return 个数
     */
    Integer count();


    /**
     * 数据是否存在
     * @param searchMap 搜索条件
     * @return 存在与否
     */
    Boolean exist(NeoMap searchMap);

    /**
     * 数据是否存在
     * @param searchExpress 复杂搜索条件
     * @return 存在与否
     */
    Boolean exist(SearchExpress searchExpress);

    /**
     * 数据是否存在
     * @param entity 实体搜搜条件
     * @return 存在与否
     */
    Boolean exist(Object entity);

    /**
     * 数据是否存在
     * @param id 主键
     * @return 存在与否
     */
    Boolean exist(Number id);


    /**
     * 批量插入
     * @param dataMapList 待插入数据
     * @return 成功插入的个数
     */
    Integer batchInsert(List<NeoMap> dataMapList);

    /**
     * 批量插入
     * @param dataList 待插入数据
     * @param <T> 数据对应类型的泛型
     * @return 成功插入的个数
     */
    <T> Integer batchInsertEntity(List<T> dataList);


    /**
     * 批量更新
     * @param dataList 待更新的数据，其中如果包含主键，则主键对应的值作为搜索条件
     * @return 成功更新的个数
     */
    Integer batchUpdate(List<NeoMap> dataList);

    /**
     * 批量更新
     * @param dataList 待更新的数据
     * @param searchColumns 待更新数据中的一些colomns包含的列对应的值作为搜索条件
     * @return 成功更新的个数
     */
    Integer batchUpdate(List<NeoMap> dataList, Columns searchColumns);

    /**
     * 批量更新实体
     * @param dataList 待更新的实体，如果包含主键，则将主键对应的值作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 成功更新的个数
     */
    <T> Integer batchUpdateEntity(List<T> dataList);

    /**
     * 批量更新实体
     * @param dataList 待更新的实体
     * @param searchColumns 待更新实体中的columns指定的列作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 成功更新的个数
     */
    <T> Integer batchUpdateEntity(List<T> dataList, Columns searchColumns);
}
