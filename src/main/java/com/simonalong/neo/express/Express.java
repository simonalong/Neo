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
        List<Object> parameters = Arrays.asList(objects);

        for (int index = 0; index < parameters.size(); index++) {
            Object parameter = parameters.get(index);

            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                Operate operate = Operate.AndEm();
                operate.setKey((String) parameter);
                if (index + 1 > parameters.size()) {
                    throw new NumberOfValueException("operate参数个数key-value有误");
                }
                operate.setValue(parameters.get(index++));
                operateQueue.add(operate);
                continue;
            }

            // Operate类型处理
            if (parameter instanceof Operate) {
                operateQueue.add((Operate) parameter);
                continue;
            }

            // todo 后面处理NeoMap的类型
            // NeoMap处理
            //            if (parameter instanceof NeoMap) {
            //                 NeoMap转换到Operate
            //                operateList.add((NeoMap)parameter);
            //            }
        }
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express and(Object... objects) {
        List<Object> parameters = Arrays.asList(objects);
        for (int index = 0; index < parameters.size(); index++) {
            Object parameter = parameters.get(index);

            // key-value处理：key必须为String类型，key后面必须为对应的value
            if (parameter instanceof String) {
                Operate operate = Operate.And();
                operate.setKey((String) parameter);
                if (index + 1 > parameters.size()) {
                    throw new NumberOfValueException("operate参数个数key-value有误");
                }
                operate.setValue(parameters.get(index++));
                operateQueue.add(operate);
                continue;
            }

            // Operate类型处理
            if (parameter instanceof Operate) {
                operateQueue.add((Operate) parameter);
                continue;
            }

            // todo 后面处理NeoMap的类型
            // NeoMap处理
            //            if (parameter instanceof NeoMap) {
            //                 NeoMap转换到Operate
            //                operateList.add((NeoMap)parameter);
            //            }
        }
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express or(Object... expresses) {
        return this;
    }

    /**
     * 空操作
     *
     * @return this
     */
    public Express em(Express... expresses) {
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
     * 转化为sql字段
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
            stringBuilder.append(operate.toSql());
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
}
