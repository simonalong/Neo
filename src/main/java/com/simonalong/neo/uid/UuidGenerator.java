package com.simonalong.neo.uid;

import com.simonalong.neo.Neo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.UuidException;
import com.simonalong.neo.uid.splicer.DefaultUuidSplicer;
import com.simonalong.neo.uid.splicer.UuidSplicer;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.neo.uid.UuidConstant.NEO_UUID_TABLE;

/**
 * 分布式全局id生成器
 *
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
@Slf4j
public final class UuidGenerator {

    /**
     * 2020-02-22 00:00:00.000 对应的时间
     */
    static Long startTime = 1582300800000L;
    private static final Integer FIRST_YEAR = 2020;
    private Neo neo;
    private static volatile UuidGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UuidSplicer> uUidBuilderMap = new HashMap<>();

    private UuidGenerator() {}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @return 全局id生成器对象
     */
    public static UuidGenerator getInstance(Neo neo) {
        if (null == instance) {
            synchronized (UuidGenerator.class) {
                if (null == instance) {
                    instance = new UuidGenerator();
                    instance.neo = neo;
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        if (null == neo) {
            throw new UuidException("数据库对象为空");
        }
        if (!neo.tableExist(NEO_UUID_TABLE)) {
            throw new UuidException("数据库uuid表不存在，请创建表 neo_uuid_generator");
        }
    }

    /**
     * 设置启动时间
     * <p>
     * 目前当前的启动时间是按照2020年2月22号算起，如果不设置，则最久可以用到2083年左右
     * @param year 起始时间
     * @param month 起始时间
     * @param dayOfMonth 起始时间
     * @param hour 起始时间
     * @param minute 起始时间
     * @param second 起始时间
     */
    public static void setStartTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        if (year < FIRST_YEAR) {
            throw new NeoException("请设置未来时间");
        }
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        startTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     */
    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> addUUidSplicer(n, new DefaultUuidSplicer(n, neo)));
    }

    /**
     * 获取对应命名空间的全局id
     *
     * @param namespace 业务的命名空间
     * @return 全局id生成器
     */
    public long getUUid(String namespace) {
        return getUUidSplicer(namespace).splice();
    }

    private UuidSplicer getUUidSplicer(String namespace) {
        if (!uUidBuilderMap.containsKey(namespace)) {
            throw new UuidException("命名空间" + namespace + "不存在，请先添加命名空间");
        }
        return uUidBuilderMap.get(namespace);
    }

    /**
     * 添加对应业务命名空间的uuid构造器
     *
     * @param namespace          业务命名空间
     * @param defaultUuidSplicer uuid构造器
     */
    private void addUUidSplicer(String namespace, UuidSplicer defaultUuidSplicer) {
        uUidBuilderMap.putIfAbsent(namespace, defaultUuidSplicer);
    }
}
