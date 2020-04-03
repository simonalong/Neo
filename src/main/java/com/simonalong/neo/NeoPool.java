package com.simonalong.neo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;

/**
 * 多db管理对象
 * @author zhouzhenyong
 * @since 2019/6/21 下午3:48
 */
public final class NeoPool {

    private static Map<String, Neo> neoMap = new ConcurrentHashMap<>(8);
    private static final NeoPool INSTANCE = new NeoPool();

    public static NeoPool getInstance(){
        return INSTANCE;
    }

    /**
     * 添加db数据
     * @param alias 数据库别名
     * @param dataSource 数据库链接对象
     * @return pool对象
     */
    public NeoPool add(String alias, DataSource dataSource){
        neoMap.putIfAbsent(alias, Neo.connect(dataSource));
        return this;
    }

    /**
     * 添加db数据
     * @param alias 数据库别名
     * @param neo 数据库对象
     * @return pool对象
     */
    public NeoPool add(String alias, Neo neo){
        neoMap.putIfAbsent(alias, neo);
        return this;
    }

    /**
     * 添加分库
     *
     * @param devideDbNames 分库的db分库表达式，{0, 12}作为后缀。比如：xxx{0, 12}
     * @param neoList db的列表
     * @return pool对象
     */
    public NeoPool addDevideDbList(String devideDbNames, List<Neo> neoList) {
        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideDbNames);

        while (matcher.find()) {
            String dbName = matcher.group(1);
            Integer min = Integer.valueOf(matcher.group(2));
            Integer max = Integer.valueOf(matcher.group(3));
            for (Integer index = min, indexJ = 0; index <= max; index++, indexJ++) {
                add(dbName + index, neoList.get(indexJ));
            }
        }
        return this;
    }

    /**
     * 根据列获取对应的分库
     *
     * @param devideDb 分库的库名
     * @param devideColumn 分库的列名
     * @return 库
     */
    public Neo getDevideDb(String devideDb, String devideColumn) {
        // todo
        return null;
    }

    /**
     * 获取对应的db数据
     * @param alias db别名
     * @return neo对象
     */
    public Neo get(String alias){
        return neoMap.get(alias);
    }
}
