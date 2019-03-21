package com.simon.neo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

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
    private Map<Pattern, LogType> patternTypeMap = new ConcurrentHashMap<>();

    public SqlStandard(){
        Arrays.asList(Standard.values()).forEach(s->{
            Pattern p = Pattern.compile(s.regex);
            patternTypeMap.putIfAbsent(p, s.logType);
        });

    }
    /**
     * sql是否可行，只针对有ERROR的规范返回false
     * @param sql 待规范的sql
     * @return 命中有重大强制规范的sql
     */
    public boolean valid(String sql){
        Set<Pattern> patternSet = patternTypeMap.keySet();
        final LogType[] resultLog = {LogType.TRACE};
        patternSet.forEach(p -> {
            if (p.matcher(sql).matches()) {
                LogType temLog = patternTypeMap.get(p);
                if (temLog.compareTo(resultLog[0]) > 0){
                   resultLog[0] = temLog;
                }
            }
        });

        // todo 规范校验
        return true;
    }

    /**
     * 给外部开辟规范新增接口
     * @param regex 命中的正则表达式
     * @param logType 命中后采用的日志打印类型
     */
    public void addJudge(String regex, LogType logType){
        patternTypeMap.putIfAbsent(Pattern.compile(regex), logType);
    }

    enum LogType{
        /**
         * 日志各自对应的类型
         */
        TRACE(),
        DEBUG(),
        INFO(),
        WARN(),
        ERROR();
    }

    @AllArgsConstructor
    enum Standard{
        /**
         * count表达式中必须
         */
        COUNT("^$", LogType.INFO);

        private String regex;
        private LogType logType;
    }
}
