package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.DbNotSetException;
import com.simonalong.neo.db.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.simonalong.neo.express.SearchExpress;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.neo.NeoConstant.LOG_PRE_NEO;

/**
 * 该抽象类做三件事情
 * <ul>
 * <li>1.提供默认的线程池</li>
 * <li>2.异步接口实现，通过调用同步函数完成</li>
 * <li>3.同步函数调用通过db的同步函数完成</li>
 * <li>4.提供db外部实现接口</li>
 * <li>5.提供table外部获取接口</li>
 *</ul>
 * @author zhouzhenyong
 * @since 2019-08-17 17:18
 */
@Slf4j
public abstract class AbstractBaseTable extends AbstractTableAsync implements TableSync {

    @Override
    public Executor getExecutor() {
        return DefaultExecutor.getInstance().getExecutor();
    }

    /**
     * 获取当前处理该表的DB数据库
     * @return db同步库对象
     */
    abstract public Neo getDb();

    /**
     * 获取当前处理的表名
     * @return 返回表名
     */
    abstract public String getTableName();

    /**
     * 新增数据
     * @param dataMap 待插入数据
     * @return 插入后的数据
     */
    @Override
    public NeoMap insert(NeoMap dataMap) {
        return getDbInner().insert(getTableName(), dataMap);
    }

    /**
     * 新增数据
     * @param object 待插入的数据
     * @param <T> 数据类型对应的泛型
     * @return 插入后的数据
     */
    @Override
    public <T> T insert(T object) {
        return getDbInner().insert(getTableName(), object);
    }

    /**
     * 不存在则新增，存在则忽略
     *
     * @param dataMap 新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入后的数据
     */
    @Override
    public NeoMap insertOfUnExist(NeoMap dataMap, String... searchColumnKey) {
        return getDbInner().insertOfUnExist(getTableName(), dataMap, searchColumnKey);
    }

    /**
     * 不存在则新增，存在则忽略
     *
     * @param object 新增的实体
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 插入后的数据
     */
    @Override
    public <T> T insertOfUnExist(T object, String... searchColumnKey) {
        return getDbInner().insertOfUnExist(getTableName(), object, searchColumnKey);
    }

    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param dataMap 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    @Override
    public NeoMap save(NeoMap dataMap, String... searchColumnKey) {
        return getDbInner().save(getTableName(), dataMap, searchColumnKey);
    }

    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param object 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 更新或者插入后的数据
     */
    @Override
    public <T> T save(T object, String... searchColumnKey) {
        return getDbInner().save(getTableName(), object, searchColumnKey);
    }

    /**
     * 删除数据
     * @param searchMap 删除条件
     * @return 删除的个数
     */
    @Override
    public Integer delete(NeoMap searchMap){
        return getDbInner().delete(getTableName(), searchMap);
    }

    /**
     * 删除数据
     * @param searchExpress 复杂的删除条件
     * @return 删除个数
     */
    @Override
    public Integer delete(SearchExpress searchExpress) {
        return getDbInner().delete(getTableName(), searchExpress);
    }

    /**
     * 删除数据
     * @param object 实体条件
     * @param <T> 实体对应的泛型
     * @return 删除个数
     */
    @Override
    public <T> Integer delete(T object){
        return getDbInner().delete(getTableName(), object);
    }

    /**
     * 删除数据
     * @param id 主键
     * @return 删除个数
     */
    @Override
    public Integer delete(Number id){
        return getDbInner().delete(getTableName(), id);
    }

    /**
     * 更新数据
     * @param dataMap 待更新数据
     * @param searchMap 搜索条件
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap, NeoMap searchMap){
        return getDbInner().update(getTableName(), dataMap, searchMap);
    }

    /**
     * 更新数据
     * @param dataMap 待更新的数据
     * @param searchExpress 复杂的搜索条件
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap, SearchExpress searchExpress) {
        return getDbInner().update(getTableName(), dataMap, searchExpress);
    }


    /**
     * 更新数据
     * @param setEntity 待设置的数据
     * @param searchMap 搜索条件
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> T update(T setEntity, NeoMap searchMap){
        return getDbInner().update(getTableName(), setEntity, searchMap);
    }

    /**
     * 更新数据
     * @param setEntity 待更新的数据
     * @param searchExpress 复杂的搜索条件
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> T update(T setEntity, SearchExpress searchExpress) {
        return getDbInner().update(getTableName(), setEntity, searchExpress);
    }

    /**
     * 更新数据
     * @param setEntity 待更新的数据
     * @param searchEntity 实体搜索条件
     * @param <T> 搜索条件对应的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> T update(T setEntity, T searchEntity){
        return getDbInner().update(getTableName(), setEntity, searchEntity);
    }

    /**
     * 更新数据
     * @param dataMap 待更新的数据
     * @param columns 将待更新中的数据key对应的value作为条件，其中由columns来指定
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap, Columns columns){
        return getDbInner().update(getTableName(), dataMap, columns);
    }

    /**
     * 更新数据
     * @param setMap 待更新的数据
     * @param searchEntity 更新条件
     * @param <T> 搜索条件对应的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> NeoMap update(NeoMap setMap, T searchEntity){
        return getDbInner().update(getTableName(), setMap, searchEntity);
    }

    /**
     * 更新数据
     * @param entity 待更新的实体数据
     * @param columns 将待更新数据对应的列名作作为搜索条件，其中columns来配置哪个属性
     * @param <T> 待更新实体对应的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> T update(T entity, Columns columns){
        return getDbInner().update(getTableName(), entity, columns);
    }

    /**
     * 更新数据
     * @param dataMap 待更新的数据，如果其中包含主键，则采用主键，否则按照全部作为条件搜索和更新
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap){
        return getDbInner().update(getTableName(), dataMap);
    }

    /**
     * 更新数据
     * @param entity 待更新的数据，如果其中包含主键，则将主键对应的值作为搜索条件，否则就将全部作为条件搜索和更新
     * @param <T> 待更新数据的泛型
     * @return 更新后的数据
     */
    @Override
    public <T> T update(T entity){
        return getDbInner().update(getTableName(), entity);
    }

    /**
     * 查询指定列的一行数据
     * @param columns 要查询的列名
     * @param searchMap 搜索条件
     * @return 一行数据
     */
    @Override
    public NeoMap one(Columns columns, NeoMap searchMap){
        return getDbInner().one(getTableName(), columns, searchMap);
    }

    /**
     * 查询指定列的一行数据
     * @param columns 要查询的列名
     * @param searchExpress 复杂搜索条件
     * @return 指定列的数据
     */
    @Override
    public NeoMap one(Columns columns, SearchExpress searchExpress) {
        return getDbInner().one(getTableName(), columns, searchExpress);
    }

    /**
     * 查询指定列的一个数据
     * @param columns 要查询的列名
     * @param entity 实体搜索条件
     * @param <T> 实体对应的泛型
     * @return 指定列的数据
     */
    @Override
    public <T> T one(Columns columns, T entity){
        return getDbInner().one(getTableName(), columns, entity);
    }

    /**
     * 查询指定列的一个数据
     * @param columns 要查询的列名
     * @param key 主键
     * @return 指定列的数据
     */
    @Override
    public NeoMap one(Columns columns, Number key){
        return getDbInner().one(getTableName(), columns, key);
    }

    /**
     * 查询指定列的一行数据
     * @param searchMap 搜索条件
     * @return 对应的数据
     */
    @Override
    public NeoMap one(NeoMap searchMap){
        return getDbInner().one(getTableName(), searchMap);
    }

    /**
     * 查询指定列的一行数据一行
     * @param searchExpress 复杂搜索条件
     * @return 对应的数据
     */
    @Override
    public NeoMap one(SearchExpress searchExpress) {
        return getDbInner().one(getTableName(), searchExpress);
    }

    /**
     * 查询指定列的一行数据一行
     * @param entity 实体搜索条件
     * @param <T> 搜索对应的泛型
     * @return 对应的数据
     */
    @Override
    public <T> T one(T entity){
        return getDbInner().one(getTableName(), entity);
    }

    /**
     * 查询指定列的一行数据一行
     * @param id 主键
     * @return 对应的数据
     */
    @Override
    public NeoMap one(Number id){
        return getDbInner().one(getTableName(), id);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获得的数据的类型
     * @param columns 要获取的列
     * @param searchMap 搜索条件
     * @param <T> 获取的类型的泛型
     * @return 指定的某些列的类型的数据
     */
    @Override
    public <T> T one(Class<T> tClass, Columns columns, NeoMap searchMap) {
        return getDbInner().one(tClass, getTableName(), searchMap);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获得的数据的类型
     * @param columns 要获取的列
     * @param searchExpress 复杂搜索条件
     * @param <T> 获取的类型的泛型
     * @return 指定列的对应类型的值
     */
    @Override
    public <T> T one(Class<T> tClass, Columns columns, SearchExpress searchExpress) {
        return getDbInner().one(tClass, getTableName(), searchExpress);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取的数据的类型
     * @param columns 要获取的列
     * @param key 主键
     * @param <T> 要获取的数据的类型的泛型
     * @return 指定列的对应类型的值
     */
    @Override
    public <T> T one(Class<T> tClass, Columns columns, Number key) {
        return getDbInner().one(tClass, getTableName(), columns, key);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取的数据的类型
     * @param searchMap 搜索条件
     * @param <T> 待获取的数据类型的泛型
     * @return 指定类型的值
     */
    @Override
    public <T> T one(Class<T> tClass, NeoMap searchMap) {
        return getDbInner().one(tClass, getTableName(), searchMap);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取数据的类型
     * @param searchExpress 复杂搜索条件
     * @param <T> 数据类型的泛型
     * @return 指定类型的值
     */
    @Override
    public <T> T one(Class<T> tClass, SearchExpress searchExpress) {
        return getDbInner().one(tClass, getTableName(), searchExpress);
    }

    /**
     * 查询指定列的一行数据一行
     * @param tClass 获取数据的类型
     * @param id 主键
     * @param <T> 获取数据的类型对应的泛型
     * @return 指定类型的值
     */
    @Override
    public <T> T one(Class<T> tClass, Number id) {
        return getDbInner().one(tClass, getTableName(), id);
    }

    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    @Override
    public List<NeoMap> list(Columns columns, NeoMap searchMap){
        return getDbInner().list(getTableName(), columns, searchMap);
    }

    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param searchExpress 复杂搜索条件
     * @return 多行数据
     */
    @Override
    public List<NeoMap> list(Columns columns, SearchExpress searchExpress) {
        return getDbInner().list(getTableName(), columns, searchExpress);
    }

    /**
     * 获取多行数据
     * @param columns 要获取的列
     * @param entity 实体搜索条件
     * @param <T> 实体对应的类型的泛型
     * @return 多行数据
     */
    @Override
    public <T> List<T> list(Columns columns, T entity){
        return getDbInner().list(getTableName(), columns, entity);
    }

    /**
     * 获取多行数据
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    @Override
    public List<NeoMap> list(NeoMap searchMap){
        return getDbInner().list(getTableName(), searchMap);
    }

    /**
     * 获取多行数据
     * @param searchExpress 复杂搜索条件
     * @return 多行数据
     */
    @Override
    public List<NeoMap> list(SearchExpress searchExpress) {
        return getDbInner().list(getTableName(), searchExpress);
    }

    /**
     * 获取多行数据
     * @param entity 实体作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 多行数据
     */
    @Override
    public <T> List<T> list(T entity){
        return getDbInner().list(getTableName(), entity);
    }

    /**
     * 多行数据：指定列
     * @param columns 要回去的列名
     * @return 多行数据
     */
    @Override
    public List<NeoMap> list(Columns columns){
        return getDbInner().list(getTableName(), columns);
    }

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param columns 指定列
     * @param searchMap 搜索条件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    @Override
    public <T> List<T> list(Class<T> tClass, Columns columns, NeoMap searchMap) {
        return getDbInner().list(tClass, getTableName(), columns, searchMap);
    }

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param columns 指定列
     * @param searchExpress 复杂的搜索条件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    @Override
    public <T> List<T> list(Class<T> tClass, Columns columns, SearchExpress searchExpress) {
        return getDbInner().list(tClass, getTableName(), columns, searchExpress);
    }

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param searchMap 搜索交件
     * @param <T> 要获取的类型的泛型
     * @return 指定列的多行数据
     */
    @Override
    public <T> List<T> list(Class<T> tClass, NeoMap searchMap) {
        return getDbInner().list(tClass, getTableName(), searchMap);
    }

    /**
     * 多行数据：指定列
     * @param tClass 对应的类型
     * @param searchExpress 搜索条件
     * @param <T> 对应类型的泛型
     * @return 指定列的多行数据
     */
    @Override
    public <T> List<T> list(Class<T> tClass, SearchExpress searchExpress) {
        return getDbInner().list(tClass, getTableName(), searchExpress);
    }

    /**
     * 多行数据：指定列
     * @param tClass   对应的类型
     * @param columns 指定列
     * @param <T> 对应类型的泛型
     * @return 指定列的多行数据
     */
    @Override
    public <T> List<T> list(Class<T> tClass, Columns columns) {
        return getDbInner().list(tClass, getTableName(), columns);
    }

    /**
     * 获取某行某列的某个值
     * @param tClass 值的对应类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 值的对应类型的泛型
     * @return 列对应的值
     */
    @Override
    public <T> T value(Class<T> tClass, String column, NeoMap searchMap){
        return getDbInner().value(tClass, getTableName(), column, searchMap);
    }

    /**
     * 获取某行某列的某个值
     * @param tClass 值对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 值对应类型的泛型
     * @return 列对应的值
     */
    @Override
    public <T> T value(Class<T> tClass, String column, SearchExpress searchExpress) {
        return getDbInner().value(tClass, getTableName(), column, searchExpress);
    }

    /**
     * 获取某行某列的某个值
     * @param tClass 值对应的类型
     * @param column 列名
     * @param entity 实体搜索条件
     * @param <T> 值对应的类型的泛型
     * @return 列对应的值
     */
    @Override
    public <T> T value(Class<T> tClass, String column, Object entity){
        return getDbInner().value(tClass, getTableName(), column, entity);
    }

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param searchMap 搜索条件
     * @return 列对应的值
     */
    @Override
    public String value(String column, NeoMap searchMap){
        return getDbInner().value(getTableName(), column, searchMap);
    }

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param searchExpress 搜索条件
     * @return 列对应的值
     */
    @Override
    public String value(String column, SearchExpress searchExpress) {
        return getDbInner().value(getTableName(), column, searchExpress);
    }

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param entity 实体搜索条件
     * @return 返回的某个值
     */
    @Override
    public String value(String column, Object entity){
        return getDbInner().value(getTableName(), column, entity);
    }

    /**
     * 获取某行某列的某个值
     * @param column 列名
     * @param entity 实体搜索
     * @return 列的某个值
     */
    @Override
    public String value(String column, Number entity){
        return getDbInner().value(getTableName(), column, entity);
    }

    /**
     * 获取一列的多个值
     *
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    @Override
    public <T> List<T> values(Class<T> tClass, String column, NeoMap searchMap){
        return getDbInner().values(tClass, getTableName(), column, searchMap);
    }

    /**
     * 获取一列的多个值
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    @Override
    public <T> List<T> values(Class<T> tClass, String column, SearchExpress searchExpress) {
        return getDbInner().values(tClass, getTableName(), column, searchExpress);
    }

    /**
     * 获取一列的多个值
     * @param tClass 列对应的类型
     * @param column 列名
     * @param entity 实体搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列的多个值
     */
    @Override
    public <T> List<T> values(Class<T> tClass, String column, Object entity){
        return getDbInner().values(tClass, getTableName(), column, entity);
    }

    /**
     * 获取一列的多个值
     * @param column 列名
     * @param searchMap 搜索条件
     * @return 一列的多个值
     */
    @Override
    public List<String> values(String column, NeoMap searchMap){
        return getDbInner().values(getTableName(), column, searchMap);
    }

    /**
     * 获取一列的多个值
     * @param column 列名
     * @param searchExpress 搜索条件
     * @return 一列的多个值
     */
    @Override
    public List<String> values(String column, SearchExpress searchExpress) {
        return getDbInner().values(getTableName(), column, searchExpress);
    }

    /**
     * 获取一列的多个值
     * @param column  列名
     * @param entity 实体搜索条件
     * @return 一列的多个值
     */
    @Override
    public List<String> values(String column, Object entity){
        return getDbInner().values(getTableName(), column, entity);
    }

    /**
     * 获取一列的多个值
     * @param column 列名
     * @return 一列的多个值
     */
    @Override
    public List<String> values(String column){
        return getDbInner().values(getTableName(), column);
    }

    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchMap 搜索条件
     * @param <T> 列对应类型的泛型
     * @return 一列对应的多个值
     */
    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String column, NeoMap searchMap) {
        return getDbInner().valuesOfDistinct(tClass, getTableName(), column, searchMap);
    }

    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列对应的多个值
     */
    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String column, SearchExpress searchExpress) {
        return getDbInner().valuesOfDistinct(tClass, getTableName(), column, searchExpress);
    }

    /**
     * 获取一列的多个值：去重
     * @param tClass 列对应的类型
     * @param column 类名
     * @param entity 实体搜索条件
     * @param <T> 列对应的类型的泛型
     * @return 一列对应的多个值
     */
    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String column, Object entity) {
        return getDbInner().valuesOfDistinct(tClass, getTableName(), column, entity);
    }

    /**
     * 获取一列的多个值：去重
     * @param column    列名
     * @param searchMap 搜索条件
     * @return 一列对应的多个值
     */
    @Override
    public List<String> valuesOfDistinct(String column, NeoMap searchMap) {
        return getDbInner().valuesOfDistinct(getTableName(), column, searchMap);
    }

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @param searchExpress 复杂搜索条件
     * @return 一列对应的多个值
     */
    @Override
    public List<String> valuesOfDistinct(String column, SearchExpress searchExpress) {
        return getDbInner().valuesOfDistinct(getTableName(), column, searchExpress);
    }

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @param entity 实体搜索条件
     * @return 一列对应的多个值
     */
    @Override
    public List<String> valuesOfDistinct(String column, Object entity) {
        return getDbInner().valuesOfDistinct(getTableName(), column, entity);
    }

    /**
     * 获取一列的多个值：去重
     * @param column 列名
     * @return 一列对应的多个值
     */
    @Override
    public List<String> valuesOfDistinct(String column) {
        return getDbInner().valuesOfDistinct(getTableName(), column);
    }

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page){
        return getDbInner().page(getTableName(), columns, searchMap, page);
    }

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(Columns columns, SearchExpress searchExpress, NeoPage page) {
        return getDbInner().page(getTableName(), columns, searchExpress, page);
    }


    /**
     * 获取分页集合
     * @param columns 多个列
     * @param entity 实体搜索条件
     * @param page 分页信息
     * @param <T> 实体类型对应的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Columns columns, T entity, NeoPage page){
        return getDbInner().page(getTableName(), columns, entity, page);
    }

    /**
     * 获取分页集合
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(NeoMap searchMap, NeoPage page){
        return getDbInner().page(getTableName(), searchMap, page);
    }

    /**
     * 获取分页集合
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(SearchExpress searchExpress, NeoPage page) {
        return getDbInner().page(getTableName(), searchExpress, page);
    }

    /**
     * 获取分页集合
     * @param entity 实体搜索条件
     * @param page 分页信息
     * @param <T> 实体对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(T entity, NeoPage page){
        return getDbInner().page(getTableName(), entity, page);
    }

    /**
     * 获取分页集合
     * @param columns 多个列
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(Columns columns, NeoPage page){
        return getDbInner().page(getTableName(), columns, page);
    }

    /**
     * 获取分页集合
     * @param page 分页信息
     * @return 分页集合
     */
    @Override
    public List<NeoMap> page(NeoPage page){
        return getDbInner().page(getTableName(), page);
    }

    /**
     * 获取分页集合
     * @param tClass 分页対应的类型
     * @param columns 多个列
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), columns, searchMap, page);
    }

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param columns 多个列
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), columns, searchExpress, page);
    }


    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param searchMap 搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, NeoMap searchMap, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), searchMap, page);
    }

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param searchExpress 复杂搜索条件
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, SearchExpress searchExpress, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), searchExpress, page);
    }

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param columns 多个列
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, Columns columns, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), columns, page);
    }

    /**
     * 获取分页集合
     * @param tClass 分页对应的类型
     * @param page 分页信息
     * @param <T> 分页对应类型的泛型
     * @return 分页集合
     */
    @Override
    public <T> List<T> page(Class<T> tClass, NeoPage page) {
        return getDbInner().page(tClass, getTableName(), page);
    }

    /**
     * 获取个数
     * @param searchMap 搜索条件
     * @return 个数
     */
    @Override
    public Integer count(NeoMap searchMap){
        return getDbInner().count(getTableName(), searchMap);
    }

    /**
     * 获取个数
     * @param searchExpress 复杂搜索条件
     * @return 个数
     */
    @Override
    public Integer count(SearchExpress searchExpress) {
        return getDbInner().count(getTableName(), searchExpress);
    }

    /**
     * 获取个数
     * @param entity 实体搜索条件
     * @return 个数
     */
    @Override
    public Integer count(Object entity){
        return getDbInner().count(getTableName(), entity);
    }

    /**
     * 获取个数
     * @return 个数
     */
    @Override
    public Integer count(){
        return getDbInner().count(getTableName());
    }


    /**
     * 数据是否存在
     * @param searchMap 搜索条件
     * @return 存在与否
     */
    @Override
    public Boolean exist(NeoMap searchMap) {
        return getDbInner().exist(getTableName(), searchMap);
    }

    /**
     * 数据是否存在
     * @param searchExpress 复杂搜索条件
     * @return 存在与否
     */
    @Override
    public Boolean exist(SearchExpress searchExpress) {
        return getDbInner().exist(getTableName(), searchExpress);
    }

    /**
     * 数据是否存在
     * @param entity 实体搜搜条件
     * @return 存在与否
     */
    @Override
    public Boolean exist(Object entity) {
        return getDbInner().exist(getTableName(), entity);
    }

    /**
     * 数据是否存在
     * @param id 主键
     * @return 存在与否
     */
    @Override
    public Boolean exist(Number id) {
        return getDbInner().exist(getTableName(), id);
    }

    /**
     * 批量插入
     * @param dataMapList 待插入数据
     * @return 成功插入的个数
     */
    @Override
    public Integer batchInsert(List<NeoMap> dataMapList){
        return getDbInner().batchInsert(getTableName(), dataMapList);
    }

    /**
     * 批量插入
     * @param dataList 待插入数据
     * @param <T> 数据对应类型的泛型
     * @return 成功插入的个数
     */
    @Override
    public <T> Integer batchInsertEntity(List<T> dataList) {
        return getDbInner().batchInsertEntity(getTableName(), dataList);
    }

    /**
     * 批量更新
     * @param dataList 待更新的数据，其中如果包含主键，则主键对应的值作为搜索条件
     * @return 成功更新的个数
     */
    @Override
    public Integer batchUpdate(List<NeoMap> dataList) {
        return getDbInner().batchUpdate(getTableName(), dataList);
    }

    /**
     * 批量更新
     * @param dataList 待更新的数据
     * @param searchColumns 待更新数据中的一些colomns包含的列对应的值作为搜索条件
     * @return 成功更新的个数
     */
    @Override
    public Integer batchUpdate(List<NeoMap> dataList, Columns searchColumns) {
        return getDbInner().batchUpdate(getTableName(), dataList, searchColumns);
    }

    /**
     * 批量更新实体
     * @param dataList 待更新的实体，如果包含主键，则将主键对应的值作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 成功更新的个数
     */
    @Override
    public <T> Integer batchUpdateEntity(List<T> dataList) {
        return getDbInner().batchUpdateEntity(getTableName(), dataList);
    }

    /**
     * 批量更新实体
     * @param dataList 待更新的实体
     * @param searchColumns 待更新实体中的columns指定的列作为搜索条件
     * @param <T> 实体对应类型的泛型
     * @return 成功更新的个数
     */
    @Override
    public <T> Integer batchUpdateEntity(List<T> dataList, Columns searchColumns) {
        return getDbInner().batchUpdateEntity(getTableName(), dataList, searchColumns);
    }


    @Override
    public CompletableFuture<NeoMap> insertAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> insert(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object, Executor executor) {
        return CompletableFuture.supplyAsync(() -> insert(object), executor);
    }


    @Override
    public CompletableFuture<NeoMap> insertOfUnExistAsync(NeoMap dataMap, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> insertOfUnExist(dataMap, searchColumnKey), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertOfUnExistAsync(T object, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> insertOfUnExist(object, searchColumnKey), executor);
    }


    @Override
    public CompletableFuture<NeoMap> saveAsync(NeoMap dataMap, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> save(dataMap, searchColumnKey), executor);
    }

    @Override
    public <T> CompletableFuture<T> saveAsync(T object, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> save(object, searchColumnKey), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T object, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(object), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(id), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap, searchMap), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(setEntity, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(setEntity, searchEntity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap, columns), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(entity, columns), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, searchMap), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Columns columns, T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(id), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, columns, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, id), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, entity), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(searchMap), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(entity), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, columns, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, column, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, column, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, column, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(column, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(column, searchExpress), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(column, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String column, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(column), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, column, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, column, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, column, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(column, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(column, searchExpress), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(column, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String column, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(column), executor);
    }


    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, column, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, column, searchExpress), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, column, entity), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String column, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(column, searchMap), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String column, SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(column, searchExpress), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String column, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(column, entity), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, searchMap, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, SearchExpress searchExpress, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, searchExpress, page), executor);
    }


    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(searchMap, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(SearchExpress searchExpress, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(searchExpress, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(page), executor);
    }


    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, columns, searchMap, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, columns, searchExpress, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, searchMap, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, SearchExpress searchExpress, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, searchExpress, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, columns, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, page), executor);
    }


    @Override
    public CompletableFuture<Integer> countAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(searchMap), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(searchExpress), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(entity), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(Executor executor) {
        return CompletableFuture.supplyAsync(this::count, executor);
    }


    @Override
    public CompletableFuture<Boolean> existAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(()-> exist(searchMap), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(SearchExpress searchExpress, Executor executor) {
        return CompletableFuture.supplyAsync(()-> exist(searchExpress), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(()-> exist(entity), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(Number id, Executor executor) {
        return CompletableFuture.supplyAsync(()-> exist(id), executor);
    }

    @Override
    public CompletableFuture<Integer> batchInsertAsync(List<NeoMap> dataMapList, Executor executor) {
        return CompletableFuture.supplyAsync(() -> batchInsert(dataMapList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchInsertEntity(dataList), executor);
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(dataList), executor);
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(dataList, columns), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(dataList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(dataList, columns), executor);
    }

    private DbSync getDbInner() {
        DbSync db = getDb();
        if (null == db) {
            log.error(LOG_PRE_NEO + "DB not set");
            throw new DbNotSetException();
        }
        return db;
    }
}
