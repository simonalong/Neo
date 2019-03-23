package com.simon.neo.sql;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * sql规范
 * @author zhouzhenyong
 * @since 2019/3/21 上午8:16
 */
@Slf4j
public class SqlStandard {

    private static final String PRE_LOG = "[Neo-standard] ";
    /**
     * 规范映射
     */
    private Map<Pattern, Standard> patternTypeMap = new ConcurrentHashMap<>();
    private static SqlStandard instance = new SqlStandard();

    private SqlStandard() {
        Arrays.asList(StandardEnum.values())
            .forEach(s -> patternTypeMap.putIfAbsent(Pattern.compile(s.standard.regex), s.standard));
    }

    public static SqlStandard getInstance(){
        return instance;
    }

    /**
     * sql是否可行，只针对有FORBIDDEN的规范返回false
     * @param sql 待规范的sql
     * @return 命中有重大强制规范的sql
     */
    public boolean valid(String sql){
        Set<Pattern> patternSet = patternTypeMap.keySet();
        final Standard[] standard = {new Standard(LogType.NONE)};
        patternSet.forEach(p -> {
            if (p.matcher(sql).matches()) {
                Standard temStd = patternTypeMap.get(p);
                if (temStd.logType.compareTo(standard[0].logType) > 0) {
                    standard[0] = temStd;
                }
            }
        });

        switch (standard[0].logType) {
            case TRACE:
                log.trace(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql => " + sql + "]");
                break;
            case DEBUG:
                log.debug(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql => " + sql + "]");
                break;
            case INFO:
                log.info(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql => " + sql + "]");
                break;
            case WARN:
                log.warn(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql=> " + sql + "]");
                break;
            case ERROR:
                log.error(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql => " + sql + "]");
                break;
            case FORBIDDEN:
                log.error(PRE_LOG + "[命中规范] [" + standard[0].desc + "] [sql => " + sql + "]");
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * 给外部开辟规范新增sql规范接口
     * @param regex 匹配sql的正则表达式
     * @param desc 命中之后的标书
     * @param logType 命中之后的日志类型
     */
    public void addStandard(String regex, String desc, LogType logType){
        patternTypeMap.putIfAbsent(Pattern.compile(regex), new Standard(regex, desc, logType));
    }

    /**
     * 规范类
     */
    static class Standard{
        private String regex;
        private String desc;
        private LogType logType;

        Standard(LogType logType){
            this.regex = "";
            this.desc = "";
            this.logType = logType;
        }

        Standard (String regex, String desc, LogType logType){
            this.regex = regex;
            this.desc = desc;
            this.logType = logType;
        }
    }

    /**
     * 系统默认的一些sql语句规范
     */
    @AllArgsConstructor
    enum StandardEnum {

        /**
         * 不要使用select *，尽量使用具体的列
         */
        SELECT(new Standard("^.*(select |SELECT )\\*.*$", "请不要使用select *，尽量使用具体的列", LogType.WARN)),

        /**
         * where子句中，尽量不要使用，!=,<>,not,not in, not exists, not like
         */
        WHERE_NOT(new Standard("^.*( where | WHERE ).*(!=|<>| not | NOT | not in| NOT IN| not exists| NOT EXISTS| not like| NOT LIKE)+.*$",
            "where子句中，尽量不要使用，!=,<>,not,not in, not exists, not like", LogType.WARN)),

        /**
         * where子句中，like 这里的模糊请尽量不要用通配符%开头匹配，即like '%xxx'
         */
        LIKE(new Standard("^.*( like| LIKE) '%.*'.*$", "where子句中，like 这里的模糊请尽量不要用通配符%开头匹配，即like '%xxx'", LogType.WARN)),

        /**
         * where子句中有in操作，请谨慎使用
         */
        IN(new Standard("^.*( in | in\\(| IN | IN\\()+.*$", "where子句中有in操作，请谨慎使用", LogType.INFO));

        private Standard standard;
    }

    enum LogType{

        /**
         * 默认
         */
        NONE,
        /**
         * 日志各自对应的类型
         */
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        /**
         * 这个为禁用，其他的均为打印日志，sql如果匹配上，则直接抛出异常，并禁止
         */
        FORBIDDEN;
    }
}
