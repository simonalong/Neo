package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;

import java.util.*;

/**
 * @author shizi
 * @since 2020/8/29 11:14 上午
 */
public class Express {

    NeoQueue<Operate> innerOperateQueue = NeoQueue.of();

    public Express() {}

    public Express(Object... objects) {
        init(Operate.parse(LogicEnum.AND_EM, objects));
    }

    public Express(NeoQueue<Operate> neoQueue) {
        init(neoQueue);
    }

    private void init(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.AndEm(queue));
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express and(Object... objects) {
        and(Operate.parse(LogicEnum.AND_EM, objects));
        return this;
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express and(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.And(queue));
        return this;
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express andEm(Object... objects) {
        andEm(Operate.parse(LogicEnum.AND_EM, objects));
        return this;
    }

    /**
     * and操作
     *
     * @return this
     */
    public Express andEm(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.AndEm(queue));
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express or(Object... objects) {
        or(Operate.parse(LogicEnum.OR_EM, objects));
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express or(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.Or(queue));
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express orEm(Object... objects) {
        orEm(Operate.parse(LogicEnum.OR_EM, objects));
        return this;
    }

    /**
     * or操作
     *
     * @return this
     */
    public Express orEm(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.OrEm(queue));
        return this;
    }

    /**
     * 空操作
     *
     * @return this
     */
    public Express append(Object... objects) {
        append(Operate.parse(LogicEnum.EMPTY, objects));
        return this;
    }

    /**
     * empty操作
     *
     * @return this
     */
    public Express append(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.Em(queue));
        return this;
    }

    /**
     * 转换为sql对应的数据
     *
     * @return 顺序化的数据
     */
    public List<Object> toValue() {
        NeoQueue<Object> valueQueue = NeoQueue.of();

        Operate operate;
        NeoQueue<Operate> operateCopy = innerOperateQueue.clone();
        while ((operate = operateCopy.poll()) != null) {
            if (!operate.valueLegal()) {
                continue;
            }
            valueQueue.addAll(operate.getValueQueue());
        }
        return valueQueue.toList();
    }

    /**
     * 转化为带?的sql字段
     *
     * @return sql字段
     */
    public String toSql() {
        StringBuilder stringBuilder = new StringBuilder();
        Operate operate;
        NeoQueue<Operate> queueCopy = innerOperateQueue.clone();
        while ((operate = queueCopy.poll()) != null) {
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

    enum LogicEnum {
        AND,
        AND_EM,
        OR,
        OR_EM,
        EMPTY
    }
}
