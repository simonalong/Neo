package com.simon.neo;

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

    /**
     * 规范映射
     */
    private Map<Pattern, Standard> patternTypeMap = new ConcurrentHashMap<>();

    public SqlStandard() {
        Arrays.asList(StandardEnum.values())
            .forEach(s -> patternTypeMap.putIfAbsent(Pattern.compile(s.standard.regex), s.standard));
    }

    /**
     * sql是否可行，只针对有FORBIDDEN的规范返回false
     * @param sql 待规范的sql
     * @return 命中有重大强制规范的sql
     */
    public boolean valid(String sql){
        Set<Pattern> patternSet = patternTypeMap.keySet();
        final Standard[] standard = {new Standard(LogType.TRACE)};
        patternSet.forEach(p -> {
            if (p.matcher(sql).matches()) {
                Standard temStd = patternTypeMap.get(p);
                if (temStd.logType.compareTo(standard[0].logType) > 0){
                    standard[0] = temStd;
                }
            }
        });

        switch (standard[0].logType) {
            case TRACE:
                log.trace("语句：sql => " + sql + ", " + standard[0].desc);
                break;
            case DEBUG:
                log.debug("语句：sql => " + sql + ", " + standard[0].desc);
                break;
            case INFO:
                log.info("语句：sql => " + sql + ", " + standard[0].desc);
                break;
            case WARN:
                log.warn("语句：sql => " + sql + ", " + standard[0].desc);
                break;
            case ERROR:
                log.error("语句：sql => " + sql + ", " + standard[0].desc);
                break;
            case FORBIDDEN:
                log.error("语句：sql => " + sql + ", " + standard[0].desc);
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

        Standard(){}

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
         * count表达式中必须
         */
        COUNT(new Standard("^$", "命中xxxx", LogType.INFO));

        private Standard standard;
    }

    enum LogType{

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
