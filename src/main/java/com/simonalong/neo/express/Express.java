package com.simonalong.neo.express;

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
    public Express em(Object... objects) {
        operateQueue.addAll(Operate.parse(LogicOperate.EMPTY, objects));
        return this;
    }

    /**
     * 转换为sql对应的数据
     *
     * @return 顺序化的数据
     */
    List<Object> toValue() {
        // todo
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
        while ((operate = operateQueue.poll()) != null) {
            if (!operate.valueLegal()) {
                continue;
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

    enum LogicOperate{
        AND,
        OR,
        EMPTY
    }
}
