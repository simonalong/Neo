package com.simonalong.neo.express;

import com.simonalong.neo.exception.NumberOfValueException;

import java.util.*;

/**
 * @author shizi
 * @since 2020/8/29 11:14 上午
 */
public class Express {

    Queue<Operate> operateQueue = new LinkedList<>();

    public Express() {}

    public Express(Object... objects) {
        operateQueue.addAll(Operate.parse(LogicOperate.AND, objects));
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express and(Object... objects) {
        operateQueue.addAll(Operate.parse(LogicOperate.AND, objects));
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express or(Object... objects) {
        operateQueue.addAll(Operate.parse(LogicOperate.OR, objects));
        return this;
    }

    /**
     * 空操作
     *
     * @return this
     */
    public Express em(Express... objects) {
        return this;
    }

    /**
     * 转换为sql对应的数据
     *
     * @return 顺序化的数据
     */
    List<Object> toValue() {
        return null;
    }

    /**
     * 转化为带?的sql字段
     *
     * @return sql字段
     */
    public String toSql() {
        StringBuilder stringBuilder = new StringBuilder();
        Operate operate;
        boolean haveCondition = false;
        while ((operate = operateQueue.poll()) != null) {
            if (!haveCondition && operate.haveCondition()) {
                haveCondition = true;
            }
            stringBuilder.append(operate.generateOperate());
        }

        String result = stringBuilder.toString().trim();
        if (result.length() != 0) {
            if (result.startsWith("and ")) {
                result = result.substring("and ".length()).trim();
            }
            if (result.startsWith("or ")) {
                result = result.substring("or ".length()).trim();
            }
            return " where " + result;
        }
        return stringBuilder.toString();
    }

    /**
     * 在sql拼接生成之后进行处理
     */
    private String afterGenerateSql(String conditionSql, Boolean haveCondition) {
        String resultSqlPart = conditionSql.trim();
        if(resultSqlPart.startsWith("and")) {
            resultSqlPart = resultSqlPart.substring("and".length());
        }

        if(resultSqlPart.startsWith("or")) {
            resultSqlPart = resultSqlPart.substring("or".length());
        }

        if (haveCondition) {
            return " where " + resultSqlPart;
        }
        return resultSqlPart;
    }

    static enum LogicOperate{
        AND,
        OR,
        EMPTY
    }
}
