package com.github.simonalong.neo.biz;

import com.github.simonalong.neo.Columns;
import com.github.simonalong.neo.Neo;
import com.github.simonalong.neo.NeoMap;
import com.github.simonalong.neo.db.NeoPage;
import java.util.List;

/**
 * 给业务方使用的，可以作为baseService使用
 *
 * @author zhouzhenyong
 * @since 2019/5/13 下午12:01
 */
public abstract class AbstractNeoService {

    /**
     * 获取库数据
     * @return 返回系统自定义的库
     */
    protected abstract Neo getNeo();

    /**
     * 返回当前使用的表名
     * @return 表名
     */
    protected abstract String getTableName();

    public NeoMap insert(NeoMap dataMap){
        return getNeo().insert(getTableName(), dataMap);
    }

    public Integer delete(NeoMap searchMap){
        return getNeo().delete(getTableName(), searchMap);
    }

    public Integer delete(Long id){
        return getNeo().delete(getTableName(), id);
    }

    /**
     * 更新数据
     * @param dataMap 设置待更新的数据
     * @param searchMap 搜索的数据
     * @return 更新后的数据
     */
    public NeoMap update(NeoMap dataMap, NeoMap searchMap) {
        return getNeo().update(getTableName(), dataMap, searchMap);
    }

    /**
     * 更新
     * @param dataMap 待更新的数据
     * @param columns 搜索条件，其中该列为 dataMap 中对应的key的名字
     * @return map对象
     */
    public NeoMap update(NeoMap dataMap, Columns columns) {
        return getNeo().update(getTableName(), dataMap, dataMap.assign(columns));
    }

    /**
     * 会默认查找dataMap中为主键的key，如果找到则按照对应的主键搜索
     * @param dataMap 待更新的数据
     * @return 更新后的数据
     */
    public NeoMap update(NeoMap dataMap) {
        return getNeo().update(getTableName(), dataMap);
    }

    public NeoMap one(Long id){
        return getNeo().one(getTableName(), NeoMap.of("id", id));
    }

    /**
     * 查询一行实体数据
     * @param columns 列名
     * @param searchMap 搜索条件
     * @return 返回一个实体的Map影射
     */
    public NeoMap one(Columns columns, NeoMap searchMap) {
        return getNeo().one(getTableName(), columns, searchMap);
    }

    /**
     * 查询具体的数据列表
     * @param columns 要展示的列
     * @param searchMap 搜索条件
     * @return 返回一列数据
     */
    public List<NeoMap> list(Columns columns, NeoMap searchMap) {
        return getNeo().list(getTableName(), columns, searchMap);
    }

    public List list(NeoMap dataMap) {
        return getNeo().list(getTableName(), dataMap);
    }

    /**
     * 分页数据
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param page  分页
     * @return 分页对应的数据
     */
    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page) {
        return getNeo().page(getTableName(), columns, searchMap, page);
    }

    /**
     * 分页数据
     * @param columns 列信息
     * @param searchMap 搜索条件，带有分页信息的搜索条件
     * @return 分页对应的数据
     */
    public List<NeoMap> page(Columns columns, NeoMap searchMap) {
        return getNeo().page(getTableName(), columns, searchMap);
    }

    /**
     * 分组数据
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param pageNo 分页的页数
     * @param pageSize  分页的页面数据大小
     * @return 分页对应的数据
     */
    public List<NeoMap> page(Columns columns, NeoMap searchMap, Integer pageNo, Integer pageSize) {
        return getNeo().page(getTableName(), columns, searchMap, NeoPage.of(pageNo, pageSize));
    }

    public List getPage(NeoMap dataMap, NeoPage page) {
        return getNeo().page(getTableName(), dataMap, page);
    }

    public List getPage(NeoMap searchMap, Integer pageNo, Integer pageSize) {
        return getNeo().page(getTableName(), searchMap, NeoPage.of(pageNo, pageSize));
    }

    public List getPage(NeoMap dataMap) {
        return getNeo().page(getTableName(), dataMap);
    }

    public Integer count(NeoMap searchMap) {
        return getNeo().count(getTableName(), searchMap);
    }

    /**
     * 查询某行某列的值
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param <T> 目标类型
     * @return 指定的数据值
     */
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().value(tClass, getTableName(), field, searchMap);
    }

    public String value(String field, NeoMap searchMap){
        return getNeo().value(getTableName(), field, searchMap);
    }

    public String value(String field, Long id){
        return getNeo().value(getTableName(), field, id);
    }

    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().values(tClass, getTableName(), field, searchMap);
    }

    public <T> List<T> values(Class<T> tClass, String field, Long id){
        return getNeo().values(tClass, getTableName(), field, id);
    }

    public List<String> values(String field, NeoMap searchMap){
        return getNeo().values(getTableName(), field, searchMap);
    }

    public List<String> values(String field, Long id){
        return getNeo().values(getTableName(), field, id);
    }
}
