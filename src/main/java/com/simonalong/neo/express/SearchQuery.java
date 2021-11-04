package com.simonalong.neo.express;

import com.simonalong.neo.NeoConstant;
import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;

import java.util.*;
import java.util.function.BiFunction;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;
import static com.simonalong.neo.express.BaseOperate.*;

/**
 * @author shizi
 * @since 2020/8/29 11:14 上午
 */
public class SearchQuery {

    NeoQueue<Operate> innerOperateQueue = NeoQueue.of();
    ThreadLocal<String> tableNameLocal = ThreadLocal.withInitial(() -> DEFAULT_TABLE);

    public SearchQuery() {
        tableNameLocal.remove();
        tableNameLocal = ThreadLocal.withInitial(() -> DEFAULT_TABLE);
    }

    public SearchQuery(Object... objects) {
        tableNameLocal.remove();
        tableNameLocal = ThreadLocal.withInitial(() -> DEFAULT_TABLE);

        init(Operate.parse(LogicEnum.AND_EM, DEFAULT_TABLE, objects));
    }

    public SearchQuery(NeoQueue<Operate> neoQueue) {
        tableNameLocal.remove();
        tableNameLocal = ThreadLocal.withInitial(() -> DEFAULT_TABLE);

        init(neoQueue);
    }

    private void init(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.AndEm(queue));
    }

    public Boolean containKey(String key) {
        for (Operate operate : innerOperateQueue) {
            if (null != operate.getColumn() && key.equals(operate.getColumn())) {
                return true;
            }
        }

        return false;
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

    public SearchQuery table(String tableName) {
        tableNameLocal.set(tableName);
        return this;
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
    public SearchQuery and(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        and(Operate.parse(LogicEnum.AND_EM, tableNameLocal.get(), objects));
        return this;
    }

    /**
     * and操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public SearchQuery and(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.And(queue));
        return this;
    }

    /**
     * and操作：objects对应的是k-v-k-v...这样的结构，最后生成的sql是：k1=? and k2=? and k3=? ...，跟{@link SearchQuery#and(Object...)}区别就是没有括号
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
    public SearchQuery andEm(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        andEm(Operate.parse(LogicEnum.AND_EM, tableNameLocal.get(), objects));
        return this;
    }

    /**
     * and操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public SearchQuery andEm(NeoQueue<Operate> queue) {
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
    public SearchQuery or(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        or(Operate.parse(LogicEnum.OR_EM, tableNameLocal.get(), objects));
        return this;
    }

    /**
     * or操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public SearchQuery or(NeoQueue<Operate> queue) {
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
    public SearchQuery orEm(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        orEm(Operate.parse(LogicEnum.OR_EM, tableNameLocal.get(), objects));
        return this;
    }

    /**
     * or操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public SearchQuery orEm(NeoQueue<Operate> queue) {
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
    public SearchQuery append(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), objects));
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
    public SearchQuery em(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        return append(objects);
    }

    /**
     * empty操作
     *
     * @param queue   操作符队列
     * @return this
     */
    public SearchQuery append(NeoQueue<Operate> queue) {
        innerOperateQueue.offer(BaseOperate.EmQueue(queue));
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

    public SearchQuery equal(Object... objects) {
        return doAppendKeyValue(BaseOperate::Equal, objects);
    }

    public SearchQuery notEqual(Object... objects) {
        return doAppendKeyValue(BaseOperate::NotEqual, objects);
    }

    public SearchQuery greaterThan(Object... objects) {
        return doAppendKeyValue(BaseOperate::GreaterThan, objects);
    }

    public SearchQuery greaterEqual(Object... objects) {
        return doAppendKeyValue(BaseOperate::GreaterEqual, objects);
    }

    public SearchQuery lessThan(Object... objects) {
        return doAppendKeyValue(BaseOperate::LessThan, objects);
    }

    public SearchQuery lessEqual(Object... objects) {
        return doAppendKeyValue(BaseOperate::LessEqual, objects);
    }

    public SearchQuery like(Object... objects) {
        return doAppendKeyValue(BaseOperate::Like, objects);
    }

    public SearchQuery notLike(Object... objects) {
        return doAppendKeyValue(BaseOperate::NotLike, objects);
    }

    public SearchQuery in(Object... objects) {
        return doAppendKeyValue(BaseOperate::In, objects);
    }

    public SearchQuery notIn(Object... objects) {
        return doAppendKeyValue(BaseOperate::NotIn, objects);
    }

    public SearchQuery isNull(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        for (Object parameter : objects) {
            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                operateQueue.add(IsNull(tableNameLocal.get(), (String) parameter));
            }
        }

        return and(Operate.parse(LogicEnum.AND_EM, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery isNotNull(Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }

        NeoQueue<Operate> operateQueue = NeoQueue.of();
        for (Object parameter : objects) {
            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                operateQueue.add(IsNotNull(tableNameLocal.get(), (String) parameter));
            }
        }

        return and(Operate.parse(LogicEnum.AND_EM, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery groupBy(String key) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(GroupBy(tableNameLocal.get(), key));
        return append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery orderBy(String... kDescAsc) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(OrderByTable(tableNameLocal.get(), kDescAsc));
        return append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery orderByDesc(String key) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(OrderByDesc(tableNameLocal.get(), key));
        return append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery exists(String sql) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(Exists(sql));
        return append(Operate.parse(LogicEnum.EMPTY, DEFAULT_TABLE, operateQueue));
    }

    public SearchQuery notExists(String sql) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(NotExists(sql));
        return append(Operate.parse(LogicEnum.EMPTY, DEFAULT_TABLE, operateQueue));
    }

    public SearchQuery page(PageReq<Object> pageReq) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(Page(pageReq));
        return append(Operate.parse(LogicEnum.EMPTY, DEFAULT_TABLE, operateQueue));
    }

    public SearchQuery page(NeoPage neoPage) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(Page(neoPage));
        return append(Operate.parse(LogicEnum.EMPTY, DEFAULT_TABLE, operateQueue));
    }

    public SearchQuery page(Integer pageNo, Integer pageSize) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(Page(pageNo, pageSize));
        return append(Operate.parse(LogicEnum.EMPTY, DEFAULT_TABLE, operateQueue));
    }

    public SearchQuery betweenAnd(String key, Object leftValue, Object rightValue) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(BetweenAnd(tableNameLocal.get(), key, leftValue, rightValue));
        return append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), operateQueue));
    }

    public SearchQuery notBetweenAnd(String key, Object leftValue, Object rightValue) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        operateQueue.add(NotBetweenAnd(tableNameLocal.get(), key, leftValue, rightValue));
        return append(Operate.parse(LogicEnum.EMPTY, tableNameLocal.get(), operateQueue));
    }

    /**
     * 转化为带?的sql字段
     * @param needWhere 是否需要where
     * @return sql字段
     */
    public String toSql(Boolean needWhere) {
        StringBuilder stringBuilder = new StringBuilder();
        Operate operate;
        boolean innerNeedWhere = false;
        NeoQueue<Operate> queueCopy = innerOperateQueue.clone();
        while ((operate = queueCopy.poll()) != null) {
            // 如果有任何一个合法的值，即有搜索条件
            if (operate.needWhere()) {
                innerNeedWhere = true;
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
            if (needWhere) {
                if (innerNeedWhere) {
                    return " where " + result;
                }
            }
            return " " + result;
        }
        return stringBuilder.toString();
    }

    public String toSql() {
        return toSql(true);
    }

    private SearchQuery doAppendKeyValue(MultiFunction<String, String, Object, Operate> operateBiFunction, Object... objects) {
        if (null == objects || objects.length == 0) {
            return this;
        }
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        List<Object> parameters = Arrays.asList(objects);

        for (int index = 0; index < parameters.size(); index++) {
            Object parameter = parameters.get(index);

            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                String key = (String) parameter;
                Object value = null;
                if(++index < parameters.size()) {
                    value = parameters.get(index);
                }
                operateQueue.add(operateBiFunction.apply(tableNameLocal.get(), key, value));
            }
        }

        return and(Operate.parse(LogicEnum.AND_EM, tableNameLocal.get(), operateQueue));
    }

    enum LogicEnum {
        /**
         * (xx and yy)
         */
        AND,
        /**
         * xx and yy
         */
        AND_EM,
        /**
         * (xx or yy)
         */
        OR,
        /**
         * xx or yy
         */
        OR_EM,
        /**
         * 空格
         */
        EMPTY
    }
}
