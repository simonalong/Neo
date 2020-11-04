package com.simonalong.neo.sql;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.util.TimeRangeStrUtil;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * sql的监控
 *
 * @author zhouzhenyong
 * @since 2019/3/22 下午1:38
 */
@Slf4j
public final class SqlMonitor {

    private static final String PRE_LOG = LOG_PRE + "[Neo-monitor] ";
    private static final Integer ONE_MILLION = 1;
    private static final Integer ONE_SECOND = 1000 * ONE_MILLION;
    private static final Integer THREE_SECOND = 3 * ONE_SECOND;
    private static final Integer TEN_SECOND = 10 * ONE_SECOND;
    private static final Integer ONE_MINUTE = 60 * ONE_SECOND;
    /**
     * 默认的sql语句打印的最大长度
     */
    private static final Integer MAX_SQL_LENGTH = 2000;
    /**
     * 批量数据的展示这里最多展示5个
     */
    private static final Integer MAX_BATCH_PARAMETER_SHOW_SIZE = 30;
    /**
     * 本地时间设置
     */
    private ThreadLocal<SqlCost> sqlTime = new ThreadLocal<>();
    private static SqlMonitor instance = new SqlMonitor();

    private SqlMonitor() {
    }

    public static SqlMonitor getInstance() {
        return instance;
    }

    public void start(String sql, List<Object> paramsList) {
        sqlTime.set(new SqlCost(System.currentTimeMillis(), sql, paramsList));
    }

    public void startTx() {
        sqlTime.set(new SqlCost(System.currentTimeMillis()));
    }

    /**
     * 普通返回debug；超过3s则Info打印；超过10s则warn打印；超过1分钟则error打印
     * @param response 待解析的结构
     */
    public void calculate(Object response) {
        SqlCost start = sqlTime.get();
        Long cost = System.currentTimeMillis() - start.getStartTime();
        // 超时1分钟，上报重大告警
        if (cost > ONE_MINUTE) {
            log.error(PRE_LOG + start.buildCost(cost, response));
            return;
        }

        // 超时10秒，上报普通告警
        if (cost > TEN_SECOND) {
            log.warn(PRE_LOG + start.buildCost(cost, response));
            return;
        }

        // 超时3秒，上报info日志
        if (cost > THREE_SECOND) {
            log.info(PRE_LOG + start.buildCost(cost, response));
            return;
        }

        log.debug(PRE_LOG + start.buildCost(cost, response));
    }

    /**
     * 直接打印sql
     * @param response 待解析的结构
     */
    public void printLog(Object response) {
        SqlCost start = sqlTime.get();
        Long cost = System.currentTimeMillis() - start.getStartTime();
        log.info(LOG_PRE + "[Neo-sql] " + start.buildCost(cost, response));
    }

    /**
     * 请将该函数放到finally中，用于释放ThreadLocal中的数据，防止线程复用时候的数据残留造成异常
     */
    public void close() {
        sqlTime.remove();
    }

    /**
     * sql耗时实体
     */
    @Setter
    @AllArgsConstructor
    static class SqlCost {

        @Getter
        private Long startTime;
        private String sql;
        private List<Object> paramsList;

        public SqlCost(Long startTime){
            this.startTime = startTime;
        }

        private String getSql() {
            if (sql.length() <= MAX_SQL_LENGTH) {
                return sql;
            }
            log.debug("sql 长度过长超过"+MAX_SQL_LENGTH + "，剩余部分不再打印");
            return sql.substring(0, MAX_SQL_LENGTH) + " ...";
        }

        private List<Object> getParamsList(){
            if (paramsList.size() <= MAX_BATCH_PARAMETER_SHOW_SIZE) {
                return paramsList;
            }
            log.debug("sql 参数个数超过" + MAX_BATCH_PARAMETER_SHOW_SIZE + "个，剩余部分不再打印");
            List<Object> resultList = paramsList.subList(0, MAX_BATCH_PARAMETER_SHOW_SIZE);
            resultList.add("...");
            return resultList;
        }

        String buildCost(Long costTime, Object object) {
            LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
            StringBuilder resultStr = new StringBuilder("[耗时: " + TimeRangeStrUtil.parseTime(costTime) + "]");
            if (null != sql) {
                dataMap.put("sql ===> ", getSql());
                dataMap.put("params ===> ", getParamsList());
            }
            dataMap.put("result ===> ", object);
            resultStr.append(JSON.toJSONString(dataMap));
            return resultStr.toString();
        }
    }
}
