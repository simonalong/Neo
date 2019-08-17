package com.simonalong.neo.biz;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 给业务方使用的，可以作为baseService使用
 *
 * @author zhouzhenyong
 * @since 2019/5/13 下午12:01
 */
public abstract class AbstractNeoService implements BaseNeoService{

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

    @Override
    public NeoMap insert(NeoMap dataMap){
        return getNeo().insert(getTableName(), dataMap);
    }

    @Override
    public <T> T insert(T object){
        return getNeo().insert(getTableName(), object);
    }

    /**
     * 异步数据插入
     *
     * @param dataMap 待插入的数据
     * @return 插入之后的数据返回
     */
    @Override
    public CompletableFuture<NeoMap>insertAsync(NeoMap dataMap){
        return getNeo().insertAsync(getTableName(), dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T>insertAsync(T object){
        return getNeo().insertAsync(getTableName(), object, getExecutor());
    }

    @Override
    public Integer delete(NeoMap searchMap){
        return getNeo().delete(getTableName(), searchMap);
    }

    /**
     * 删除数据
     *
     * @param object 对象可以为主键Number类型也可以为实体数据
     * @param <T> 为Number类型，则认为是主键，否则认为是数据实体
     * @return 返回影响的个数
     */
    @Override
    public <T> Integer delete(T object){
        return getNeo().delete(getTableName(), object);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap){
        return getNeo().deleteAsync(getTableName(), dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T object){
        return getNeo().deleteAsync(getTableName(), object, getExecutor());
    }

    /**
     * 更新数据
     * @param dataMap 设置待更新的数据
     * @param searchMap 搜索的条件数据
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap, NeoMap searchMap) {
        return getNeo().update(getTableName(), dataMap, searchMap);
    }

    /**
     * 更新
     * @param dataMap 待更新的数据
     * @param columns 搜索条件，其中该列为 dataMap 中对应的key的名字
     * @return map对象
     */
    @Override
    public NeoMap update(NeoMap dataMap, Columns columns) {
        return getNeo().update(getTableName(), dataMap, dataMap.assign(columns));
    }

    /**
     * 会默认查找dataMap中为主键的key，如果找到则按照对应的主键搜索
     * @param dataMap 待更新的数据
     * @return 更新后的数据
     */
    @Override
    public NeoMap update(NeoMap dataMap) {
        return getNeo().update(getTableName(), dataMap);
    }

    @Override
    public <T> T update(T object) {
        return getNeo().update(getTableName(), object);
    }

    /**
     * 更新数据
     * @param dataMap 设置待更新的数据
     * @param searchMap 搜索的条件数据
     * @return 更新后的数据
     */
    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap) {
        return getNeo().updateAsync(getTableName(), dataMap, searchMap, getExecutor());
    }

    /**
     * 更新
     * @param dataMap 待更新的数据
     * @param columns 搜索条件，其中该列为 dataMap 中对应的key的名字
     * @return map对象
     */
    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns) {
        return getNeo().updateAsync(getTableName(), dataMap, dataMap.assign(columns), getExecutor());
    }

    /**
     * 会默认查找dataMap中为主键的key，如果找到则按照对应的主键搜索
     * @param dataMap 待更新的数据
     * @return 更新后的数据
     */
    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap) {
        return getNeo().updateAsync(getTableName(), dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T object) {
        return getNeo().updateAsync(getTableName(), object, getExecutor());
    }

    @Override
    public NeoMap one(Long id){
        return getNeo().one(getTableName(), id);
    }

    /**
     * 查询一行实体数据
     * @param columns 列名
     * @param searchMap 搜索条件
     * @return 返回一个实体的Map影射
     */
    @Override
    public NeoMap one(Columns columns, NeoMap searchMap) {
        return getNeo().one(getTableName(), columns, searchMap);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Long id){
        return getNeo().oneAsync(getTableName(), id, getExecutor());
    }

    /**
     * 查询一行实体数据
     * @param columns 列名
     * @param searchMap 搜索条件
     * @return 返回一个实体的Map影射
     */
    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap) {
        return getNeo().oneAsync(getTableName(), columns, searchMap, getExecutor());
    }

    /**
     * 查询具体的数据列表
     * @param columns 要展示的列
     * @param searchMap 搜索条件
     * @return 返回一列数据
     */
    @Override
    public List<NeoMap> list(Columns columns, NeoMap searchMap) {
        return getNeo().list(getTableName(), columns, searchMap);
    }

    @Override
    public List<NeoMap> list(NeoMap dataMap) {
        return getNeo().list(getTableName(), dataMap);
    }

    /**
     * 查询具体的数据列表
     * @param columns 要展示的列
     * @param searchMap 搜索条件
     * @return 返回一列数据
     */
    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap) {
        return getNeo().listAsync(getTableName(), columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(NeoMap dataMap) {
        return getNeo().listAsync(getTableName(), dataMap, getExecutor());
    }

    /**
     * 分页数据
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param page  分页
     * @return 分页对应的数据
     */
    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page) {
        return getNeo().page(getTableName(), columns, searchMap, page);
    }

    /**
     * 分页数据
     * @param columns 列信息
     * @param searchMap 搜索条件，带有分页信息的搜索条件
     * @return 分页对应的数据
     */
    @Override
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
    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap, Integer pageNo, Integer pageSize) {
        return getNeo().page(getTableName(), columns, searchMap, NeoPage.of(pageNo, pageSize));
    }

    @Override
    public List getPage(NeoMap dataMap, NeoPage page) {
        return getNeo().page(getTableName(), dataMap, page);
    }

    @Override
    public List getPage(NeoMap searchMap, Integer pageNo, Integer pageSize) {
        return getNeo().page(getTableName(), searchMap, NeoPage.of(pageNo, pageSize));
    }

    @Override
    public List getPage(NeoMap dataMap) {
        return getNeo().page(getTableName(), dataMap);
    }

    @Override
    public Integer count(NeoMap searchMap) {
        return getNeo().count(getTableName(), searchMap);
    }

    @Override
    public CompletableFuture<Integer> countAsync(NeoMap searchMap) {
        return getNeo().countAsync(getTableName(), searchMap, getExecutor());
    }

    /**
     * 查询某行某列的值
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param <T> 目标类型
     * @return 指定的数据值
     */
    @Override
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().value(tClass, getTableName(), field, searchMap);
    }

    @Override
    public String value(String field, NeoMap searchMap){
        return getNeo().value(getTableName(), field, searchMap);
    }

    @Override
    public String value(String field, Long id){
        return getNeo().value(getTableName(), field, id);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().values(tClass, getTableName(), field, searchMap);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, Long id){
        return getNeo().values(tClass, getTableName(), field, id);
    }

    @Override
    public List<String> values(String field, NeoMap searchMap){
        return getNeo().values(getTableName(), field, searchMap);
    }

    @Override
    public List<String> values(String field, Long id){
        return getNeo().values(getTableName(), field, id);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().valueAsync(tClass, getTableName(), field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, NeoMap searchMap){
        return getNeo().valueAsync(getTableName(), field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, Long id){
        return getNeo().valueAsync(getTableName(), field, id, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return getNeo().valuesAsync(tClass, getTableName(), field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Long id){
        return getNeo().valuesAsync(tClass, getTableName(), field, id, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap){
        return getNeo().valuesAsync(getTableName(), field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Long id){
        return getNeo().valuesAsync(getTableName(), field, id, getExecutor());
    }

    /**
     * 创建默认的异步回调的线程池
     *
     * <p>创建核心线程池为单机本身可用的cpu个数，最大为2倍cpu个数，队列为100倍的cpu处理个数队列，拒绝策略采用直接运行方式
     * <p>用户可以继承后并重写该线程池分配策略
     */
    @Override
    public ExecutorService getExecutor(){
        int processNum =  Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processNum, processNum * 2, 0, TimeUnit.NANOSECONDS,
            new LinkedBlockingQueue<>(processNum * 100),  new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-Neo-async-Call-" + threadNum.getAndIncrement());
            }
        }, new CallerRunsPolicy());
    }
}
