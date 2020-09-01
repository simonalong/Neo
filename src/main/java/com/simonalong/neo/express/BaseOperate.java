package com.simonalong.neo.express;

import com.simonalong.neo.NeoConstant;
import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.util.CharSequenceUtil;
import com.simonalong.neo.util.LogicOperateUtil;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import static com.simonalong.neo.util.LogicOperateUtil.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/29 11:32 下午
 */
public abstract class BaseOperate implements Operate {

    /**
     * 操作符
     */
    @Getter
    @Setter
    private String operateSymbol;
    @Setter
    protected NeoQueue<Operate> childOperateQueue = NeoQueue.of();

    public BaseOperate() {}

    @Override
    public void offerOperate(Operate value) {
        this.childOperateQueue.offer(value);
    }

    @Override
    public Boolean needWhere() {
        for (Operate operate : childOperateQueue) {
            if (operate.needWhere()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getValueFromColumnOfOperate(String columnName, String operateSymbol) {
        for (Operate operate : childOperateQueue) {
            Object value = operate.getValueFromColumnOfOperate(columnName, operateSymbol);
            if (null != value) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据表达式获取表达式的第一个整体字符
     * <p>
     *     比如：根据=获取 得到{@code `a` = 12}
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    @Override
    public String getFirstOperateStr(String operateSymbol) {
        String currentOperate = this.getOperateSymbol();
        if (CharSequenceUtil.isNotEmpty(currentOperate) && currentOperate.equals(operateSymbol)) {
            return generateOperate();
        } else {
            for (Operate operate : childOperateQueue) {
                String operateStr = operate.getFirstOperateStr(operateSymbol);
                if (CharSequenceUtil.isNotEmpty(operateStr)) {
                    return operateStr;
                }
            }
        }
        return null;
    }

    /**
     * 根据表达式获取表达式所有的整体字符
     * <p>
     *     比如：根据=获取 得到{@code `a` = 12}
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    @Override
    public List<String> getAllOperateStr(String operateSymbol) {
        List<String> operateStrList = new ArrayList<>();
        for (Operate operate : childOperateQueue) {
            operateStrList.addAll(operate.getAllOperateStr(operateSymbol));
        }
        return operateStrList;
    }

    /**
     * 有括号的and
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate And(Object... objects) {
        return And(Operate.parse(Express.LogicEnum.AND_EM, objects));
    }

    /**
     * 有括号的and
     *
     * @param operateQueue 待处理操作队列
     * @return 操作类
     */
    public static Operate And(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.AND, operateQueue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate And(Operate operate) {
        return new LogicOperate(NeoConstant.AND, operate) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate And(String key, Object value) {
        return new LogicOperate(NeoConstant.AND, Equal(key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    private static String andGenerateOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " and (" + filterLogicHead(String.join(" and ", operateList)) + ")";
    }

    /**
     * 无括号的and
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate AndEm(Object... objects) {
        return AndEm(Operate.parse(Express.LogicEnum.AND_EM, objects));
    }

    public static Operate AndEm(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.AND, operateQueue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate AndEm(Operate operate) {
        return new LogicOperate(NeoConstant.AND, operate) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate AndEm(String key, Object value) {
        return new LogicOperate(NeoConstant.AND, Equal(key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    private static String andEmGenerateOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " and " + filterLogicHead(String.join(" and ", operateList));
    }

    /**
     * 有括号的or
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate Or(Object... objects) {
        return Or(Operate.parse(Express.LogicEnum.OR_EM, objects));
    }

    public static Operate Or(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.OR, operateQueue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate Or(Operate operate) {
        return new LogicOperate(NeoConstant.OR, operate) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param key   操作符左侧的key
     * @param value 操作符的值
     * @return 操作类
     */
    public static Operate Or(String key, Object value) {
        return new LogicOperate(NeoConstant.OR, Equal(key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param queue 操作队列
     * @return 操作类
     */
    public static String orGenerateOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " or (" + filterLogicHead(String.join(" or ", operateList)) + ")";
    }

    /**
     * 无括号的 OrEm
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate OrEm(Object... objects) {
        return OrEm(Operate.parse(Express.LogicEnum.OR_EM, objects));
    }

    public static Operate OrEm(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.OR, operateQueue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate OrEm(Operate operate) {
        return new LogicOperate(NeoConstant.OR, operate) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param key   操作符左侧的key
     * @param value 操作符的值
     * @return 操作类
     */
    public static Operate OrEm(String key, Object value) {
        return new LogicOperate(NeoConstant.OR, Equal(key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return orEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param queue 操作符队列
     * @return 操作类
     */
    public static String orEmGenerateOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " or " + filterLogicHead(String.join(" or ", operateList));
    }

    /**
     * 无符号的处理
     *
     * @param objects 对象
     * @return 操作符
     */
    public static Operate Em(Object... objects) {
        return OrEm(Operate.parse(Express.LogicEnum.EMPTY, objects));
    }

    public static Operate Em(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.EMPTY, operateQueue) {

            @Override
            public Boolean needWhere() {
                for (Operate operate : childOperateQueue) {
                    if (operate.needWhere()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return emGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate Em(Operate operate) {
        return new LogicOperate(NeoConstant.EMPTY, operate) {

            @Override
            public Boolean needWhere() {
                for (Operate operate : childOperateQueue) {
                    if (operate.needWhere()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return emGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param key   操作符左侧的key
     * @param value 操作符的值
     * @return 操作类
     */
    public static Operate Em(String key, Object value) {
        return new LogicOperate(NeoConstant.EMPTY, Equal(key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return emGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @param queue   操作符队列
     * @return 操作类
     */
    public static String emGenerateOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " " + filterLogicHead(String.join(" ", operateList));
    }

    /**
     * 等于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate Equal(String key, Object value) {
        return new RelationOperate(key, NeoConstant.EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " = ?";
            }
        };
    }

    /**
     * 不等于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate NotEqual(String key, Object value) {
        return new RelationOperate(key, NeoConstant.NOT_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " != ?";
            }
        };
    }

    /**
     * 大于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate GreaterThan(String key, Object value) {
        return new RelationOperate(key, NeoConstant.GREATER_THAN, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " > ?";
            }
        };
    }

    /**
     * 大于等于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate GreaterEqual(String key, Object value) {
        return new RelationOperate(key, NeoConstant.GREATER_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " >= ?";
            }
        };
    }

    /**
     * 小于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate LessThan(String key, Object value) {
        return new RelationOperate(key, NeoConstant.LESS_THAN, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " < ?";
            }
        };
    }

    /**
     * 小于等于操作符
     *
     * @param key   key
     * @param value value值
     * @return 等于操作
     */
    public static Operate LessEqual(String key, Object value) {
        return new RelationOperate(key, NeoConstant.LESS_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(super.getKey()) + " <= ?";
            }
        };
    }


    /**
     * like模糊搜索
     *
     * @param key   key
     * @param value value
     * @return 模糊匹配的字符串
     */
    public static Operate Like(String key, String value) {
        return new RelationOperate(key, NeoConstant.LIKE, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " like '" + value + "'";
            }
        };
    }

    /**
     * not like模糊搜索
     *
     * @param key   key
     * @param value value
     * @return 模糊匹配的字符串
     */
    public static Operate NotLike(String key, String value) {
        return new RelationOperate(key, NeoConstant.NOT_LIKE, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not like '" + value + "'";
            }
        };
    }

    /**
     * in集合处理
     *
     * @param key        key
     * @param collection 待匹配的集合
     * @return 模糊匹配的字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate In(String key, Collection collection) {
        return new RelationOperate(key, NeoConstant.IN, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " in " + SqlBuilder.buildIn(collection);
            }
        };
    }

    /**
     * not in集合处理
     *
     * @param key        key
     * @param collection 待匹配的集合
     * @return 模糊匹配的字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate NotIn(String key, Collection collection) {
        return new RelationOperate(key, NeoConstant.ONT_IN, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not in " + SqlBuilder.buildIn(collection);
            }
        };
    }

    /**
     * is null
     *
     * @param key key
     * @return 模糊匹配的字符串
     */
    public static Operate IsNull(String key) {
        return new RelationOperate(key, NeoConstant.IS_NULL, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " is null";
            }
        };
    }

    /**
     * is not null
     *
     * @param key key
     * @return 模糊匹配的字符串
     */
    public static Operate IsNotNull(String key) {
        return new RelationOperate(key, NeoConstant.IS_NOT_NULL, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " is not null";
            }
        };
    }

    /**
     * group by
     *
     * @param key key
     * @return group by字符串
     */
    public static Operate GroupBy(String key) {
        return new RelationOperate(key, NeoConstant.GROUP_BY, null) {

            @Override
            public Boolean needWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " group by " + SqlBuilder.toDbField(super.getKey());
            }
        };
    }

    /**
     * order by多个字符
     * <p>
     * 多种情况，如下：
     * <ul>
     *     <li>1.k1</li>
     *     <li>2.k1-desc</li>
     *     <li>3.k1-desc-k2</li>
     *     <li>4.k1-desc-k2-asc</li>
     *     <li>5.k1-desc-k2-k3</li>
     *     <li>6.k1-k2-k3-asc-k4-desc</li>
     * </ul>
     * 只要后面跟着desc或者asc，则修饰的是前面的列，如果后面不是desc或者asc，则表示当前列默认为升序，即asc
     *
     * @param kDescAsc 多个k和desc（或者asc）的类型
     * @return order by的操作符
     */
    public static Operate OrderBy(String... kDescAsc) {
        return new RelationOperate(null, NeoConstant.ORDER_BY, new LinkedList<>(Arrays.asList(kDescAsc))) {

            @Override
            public Boolean needWhere() {
                return false;
            }

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " order by " + getOrderBySql();
            }

            @SuppressWarnings("unchecked")
            private String getOrderBySql() {
                StringBuilder stringBuilder = new StringBuilder();
                LinkedList<String> valueList = (LinkedList<String>) getValue();
                List<String> outerList = new ArrayList<>();
                List<String> columnNameList = new ArrayList<>();
                for (String value : valueList) {
                    if ("".equals(value)) {
                        continue;
                    }
                    value = value.trim();
                    if (!NeoConstant.ASC.equals(value) && !NeoConstant.DESC.equals(value)) {
                        columnNameList.add(SqlBuilder.toDbField(value));
                    } else {
                        stringBuilder.append(String.join(", ", columnNameList)).append(" ").append(value);
                        outerList.add(stringBuilder.toString());
                        columnNameList.clear();
                        stringBuilder.delete(0, stringBuilder.length());
                    }
                }
                outerList.add(String.join(", ", columnNameList));
                columnNameList.clear();

                return outerList.stream().filter(e->!"".equals(e)).collect(Collectors.joining(", "));
            }
        };
    }

    /**
     * 这里只识别所有的column列，并让其全部降序
     *
     * @param key 列名
     * @return 操作符
     */
    public static Operate OrderByDesc(String key) {
        return new RelationOperate(key, null) {

            @Override
            public Boolean needWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " order by " + SqlBuilder.toDbField(super.getKey()) + " desc";
            }
        };
    }

    public static Operate BetweenAnd(String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(key, leftValue, rightValue) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " between ? and ? ";
            }
        };
    }

    public static Operate NotBetweenAnd(String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(key, leftValue, rightValue) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not between ? and ? ";
            }
        };
    }

    public static Operate Page(PageReq<Object> neoPageReq) {
        return new RelationOperate(null, null) {

            @Override
            public Boolean needWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " limit " + neoPageReq.getPageSize() + " offset " + neoPageReq.getPageIndex();
            }
        };
    }

    public static Operate Exists(String sql) {
        return new RelationOperate(null, null) {

            @Override
            public String generateOperate() {
                return " exists (" + sql + ") ";
            }
        };
    }

    public static Operate NotExists(String sql) {
        return new RelationOperate(null, null) {

            @Override
            public String generateOperate() {
                return " not exists (" + sql + ") ";
            }
        };
    }

    public static Operate NotExist(String sql) {
        return new RelationOperate(null, null) {

            @Override
            public String generateOperate() {
                return " not exist (" + sql + ") ";
            }
        };
    }

    /**
     * 生成sql部分代码
     *
     * @param queue 操作符队列
     * @return sql部分队列
     */
    private static NeoQueue<String> doGenerateSqlPart(NeoQueue<Operate> queue) {
        if (null == queue || queue.isEmpty()) {
            return NeoQueue.of();
        }
        Operate operate;
        NeoQueue<Operate> operateQueueCopy = queue.clone();
        NeoQueue<String> sqlPartQueue = NeoQueue.of();
        while ((operate = operateQueueCopy.poll()) != null) {
            sqlPartQueue.offer(operate.generateOperate());
        }
        return sqlPartQueue;
    }
}
