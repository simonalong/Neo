package com.simonalong.neo.express;

import com.simonalong.neo.NeoConstant;
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
     * 根据表达式获取表达式的第一个整体字符
     * <p>
     * 比如：根据=获取 得到{@code `a` = 12}
     *
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    public String getFirstOperateStr(String operateSymbol) {
        for (Operate operate : innerOperateQueue) {
            String value = operate.getFirstOperateStr(operateSymbol);
            if (null != value) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据表达式获取表达式所有的整体字符
     * <p>
     * 比如：根据=获取 得到{@code `a` = 12}
     *
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    public List<String> getAllOperateStr(String operateSymbol) {
        List<String> operateStrList = new ArrayList<>();
        for (Operate operate : innerOperateQueue) {
            operateStrList.addAll(operate.getAllOperateStr(operateSymbol));
        }
        return operateStrList;
    }

    /**
     * and操作：objects对应的是k-v-k-v...这样的结构，最后生成的sql是（k1=? and k2=? and k3=? ...）
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return this
     */
    public Express and(Object... objects) {
        and(Operate.parse(LogicEnum.AND_EM, objects));
        return this;
    }

    /**
     * and操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public Express and(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.And(queue));
        return this;
    }

    /**
     * and操作：objects对应的是k-v-k-v...这样的结构，最后生成的sql是：k1=? and k2=? and k3=? ...，跟{@link Express#and(Object...)}区别就是没有括号
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return this
     */
    public Express andEm(Object... objects) {
        andEm(Operate.parse(LogicEnum.AND_EM, objects));
        return this;
    }

    /**
     * and操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public Express andEm(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.AndEm(queue));
        return this;
    }

    /**
     * or操作：objects对应的是k-v-k-v...这样的结构，最后生成的sql是（k1=? or k2=? or k3=? ...）
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return this
     */
    public Express or(Object... objects) {
        or(Operate.parse(LogicEnum.OR_EM, objects));
        return this;
    }

    /**
     * or操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public Express or(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.Or(queue));
        return this;
    }

    /**
     * or操作
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return this
     */
    public Express orEm(Object... objects) {
        orEm(Operate.parse(LogicEnum.OR_EM, objects));
        return this;
    }

    /**
     * or操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public Express orEm(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.OrEm(queue));
        return this;
    }

    /**
     * 空操作
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return this
     */
    public Express append(Object... objects) {
        append(Operate.parse(LogicEnum.EMPTY, objects));
        return this;
    }

    /**
     * empty操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public Express append(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.Em(queue));
        return this;
    }

    /**
     * 获取某个列名对应的等号的值
     *
     * <p> 从包含的数据里面，找到第一个为"="的，而且列名为指定列名的
     *
     * @param columnName 列名
     * @return 列名对应的值
     */
    public Object getValue(String columnName) {
        for (Operate operate : innerOperateQueue) {
            Object value = operate.getValueFromColumnOfOperate(columnName, NeoConstant.EQUAL);
            if (null != value) {
                return value;
            }
        }
        return null;
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
            valueQueue.addAll(operate.getValueQueue());
        }
        return valueQueue.toList();
    }

    /**
     * 转化为带?的sql字段
     *
     * @return sql字段
     */
    public String toSql(Boolean needWhere) {
        StringBuilder stringBuilder = new StringBuilder();
        Operate operate;
        boolean needWhereFinal = needWhere;
        NeoQueue<Operate> queueCopy = innerOperateQueue.clone();
        while ((operate = queueCopy.poll()) != null) {
            // 如果有任何一个合法的值，即有搜索条件
            if (operate.needWhere()) {
                needWhereFinal = true;
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
            if (needWhereFinal) {
                return " where " + result;
            }
            return " " + result;
        }
        return stringBuilder.toString();
    }

    public String toSql() {
        return toSql(false);
    }

    enum LogicEnum {
        AND,
        AND_EM,
        OR,
        OR_EM,
        EMPTY
    }
}
