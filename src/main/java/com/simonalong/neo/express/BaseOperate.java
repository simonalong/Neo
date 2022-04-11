package com.simonalong.neo.express;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoConstant;
import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.db.DbType;
import com.simonalong.neo.db.NeoContext;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.util.CharSequenceUtil;
import com.simonalong.neo.util.LogicOperateUtil;
import lombok.Getter;
import lombok.Setter;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;
import static com.simonalong.neo.util.LogicOperateUtil.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/29 11:32 下午
 */
@SuppressWarnings("unused")
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
    public String getTable() {
        return DEFAULT_TABLE;
    }

    @Override
    public String getColumn() {
        return null;
    }

    @Override
    public Boolean doNeedWhere() {
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

    @Override
    public Object getValueFromColumnOfOperate(String tableName, String columnName, String operateSymbol) {
        for (Operate operate : childOperateQueue) {
            Object value = operate.getValueFromColumnOfOperate(tableName, columnName, operateSymbol);
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
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate And(Object... objects) {
        return AndTable(DEFAULT_TABLE, objects);
    }

    public static Operate AndTable(String tableName, Object... objects) {
        return And(Operate.parse(SearchQuery.LogicEnum.AND_EM, tableName, objects));
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
        return AndTable(DEFAULT_TABLE, key, value);
    }

    public static Operate AndTable(String tableName, String key, Object value) {
        return new LogicOperate(NeoConstant.AND, Equal(tableName, key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static String andGenerateOperate(NeoQueue<Operate> queue) {
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

    public static String emGenerateAndOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return filterLogicHead(String.join(" and ", operateList)) + ")";
    }

    public static String generateAndOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " (" + filterLogicHead(String.join(" and ", operateList)) + ")";
    }

    /**
     * 无括号的and
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate AndEm(Object... objects) {
        return AndEmTable(DEFAULT_TABLE, objects);
    }

    public static Operate AndEmTable(String tableName, Object... objects) {
        return AndEm(Operate.parse(SearchQuery.LogicEnum.AND_EM, tableName, objects));
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
        return AndEmTable(DEFAULT_TABLE, key, value);
    }

    public static Operate AndEmTable(String tableName, String key, Object value) {
        return new LogicOperate(NeoConstant.AND, Equal(tableName, key, value)) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static String andEmGenerateOperate(NeoQueue<Operate> queue) {
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
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate Or(Object... objects) {
        return OrTable(DEFAULT_TABLE, objects);
    }

    public static Operate OrTable(String tableName, Object... objects) {
        return Or(Operate.parse(SearchQuery.LogicEnum.OR_EM, tableName, objects));
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
        return OrTable(DEFAULT_TABLE, key, value);
    }

    public static Operate OrTable(String tableName, String key, Object value) {
        return new LogicOperate(NeoConstant.OR, Equal(tableName, key, value)) {

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
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 待处理对象
     * @return 操作类
     */
    public static Operate OrEm(Object... objects) {
        return OrEmTable(DEFAULT_TABLE, objects);
    }

    public static Operate OrEmTable(String tableName, Object... objects) {
        return OrEm(Operate.parse(SearchQuery.LogicEnum.OR_EM, tableName, objects));
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
        return OrEmTable(DEFAULT_TABLE, key, value);
    }

    public static Operate OrEmTable(String tableName, String key, Object value) {
        return new LogicOperate(NeoConstant.OR, Equal(tableName, key, value)) {

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

    public static String generateOrOperate(NeoQueue<Operate> queue) {
        NeoQueue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).filter(Objects::nonNull).collect(Collectors.toList());
        if (operateList.isEmpty()) {
            return "";
        }
        return " (" + filterLogicHead(String.join(" or ", operateList)) + ")";
    }

    /**
     * 无符号的处理
     * <p>
     *     参数类型可以为三种类型：
     *     <ul>
     *         <li>1.kvkvkv类型：String-Object-String-Object-...</li>
     *         <li>2.Operate类型(BaseOperate内部子类)</li>
     *         <li>3.集合类型：对应的内部元素为Operate类型</li>
     *     </ul>
     *
     * @param objects 对象
     * @return 操作符
     */
    public static Operate Em(Object... objects) {
        return EmTable(DEFAULT_TABLE, objects);
    }

    public static Operate EmTable(String tableName, Object... objects) {
        return OrEm(Operate.parse(SearchQuery.LogicEnum.EMPTY, tableName, objects));
    }

    public static Operate EmQueue(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(NeoConstant.EMPTY, operateQueue) {

            @Override
            public Boolean doNeedWhere() {
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
            public Boolean doNeedWhere() {
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
     * 只是对数据的拼接
     *
     * @param key   操作符左侧的key
     * @param value 操作符的值
     * @return 操作类
     */
    public static Operate Em(String key, Object value) {
        return EmTable(DEFAULT_TABLE, key, value);
    }

    public static Operate EmTable(String tableName, String key, Object value) {
        return new LogicOperate(NeoConstant.EMPTY, Space(tableName, key, value)) {

            @Override
            public Boolean doNeedWhere() {
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

    public static Operate EmSql(String partSql) {
        return new NoneOperate(NeoConstant.SPACE, partSql);
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
        return Equal(DEFAULT_TABLE, key, value);
    }

    public static Operate Equal(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " = ?";
            }
        };
    }

    /**
     * 就是空操作符介于key和value之间
     *
     * @param key key
     * @param value value
     * @return 操作符
     */
    public static Operate Space(String key, Object value) {
        return Space(DEFAULT_TABLE, key, value);
    }

    public static Operate Space(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.SPACE, value) {

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), getColumn()) + NeoConstant.SPACE;
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
        return NotEqual(DEFAULT_TABLE, key, value);
    }

    public static Operate NotEqual(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.NOT_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " != ?";
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
        return GreaterThan(DEFAULT_TABLE, key, value);
    }

    public static Operate GreaterThan(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.GREATER_THAN, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " > ?";
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
        return GreaterEqual(DEFAULT_TABLE, key, value);
    }

    public static Operate GreaterEqual(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.GREATER_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " >= ?";
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
        return LessThan(DEFAULT_TABLE, key, value);
    }

    public static Operate LessThan(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.LESS_THAN, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " < ?";
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
        return LessEqual(DEFAULT_TABLE, key, value);
    }

    public static Operate LessEqual(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.LESS_EQUAL, value) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " <= ?";
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
    public static Operate Like(String key, Object value) {
        return Like(DEFAULT_TABLE, key, value);
    }

    public static Operate Like(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.LIKE, value) {

            private boolean leftFix = false;
            private boolean rightFix = false;
            private DbType dbType;

            @Override
            public Boolean valueLegal() {
                Neo db = NeoContext.getNeo();
                if (null == db) {
                    dbType = DbType.MYSQL;
                } else {
                    dbType = db.getDbType();
                }

                Object valueObj = getValue();
                if (!(valueObj instanceof String)) {
                    return false;
                }
                String value = ((String) valueObj).trim();
                if (value.startsWith("%")) {
                    value = value.substring(1).trim();
                    leftFix = true;
                }

                if (value.endsWith("%")) {
                    value = value.substring(0, value.length() - 1).trim();
                    rightFix = true;
                }

                if ("null".equals(value)) {
                    return false;
                }

                if (CharSequenceUtil.isNotEmpty(value)) {
                    if (leftFix) {
                        value = "%" + value;
                    }

                    if (rightFix) {
                        value = value + "%";
                    }
                    setValue(value);
                    return true;
                }
                return false;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                if (dbType == DbType.MYSQL || dbType == DbType.MARIADB || dbType == DbType.ORACLE) {
                    if (!leftFix && !rightFix) {
                        return NeoQueue.of();
                    }
                    return super.getValueQueue();
                } else {
                    return NeoQueue.of();
                }
            }

            @Override
            public String generateOperate() {
                Object valueObj = getValue();
                String value;

                Neo db = NeoContext.getNeo();
                DbType dbType;
                if (null == db) {
                    dbType = DbType.MYSQL;
                } else {
                    dbType = db.getDbType();
                }
                if (dbType == DbType.MYSQL || dbType == DbType.MARIADB) {
                    if (leftFix && rightFix) {
                        value = "concat('%', ?, '%')";
                    } else if (leftFix) {
                        value = "concat('%', ?)";
                    } else if (rightFix) {
                        value = "concat(?, '%')";
                    } else {
                        value = "'" + valueObj + "'";
                    }
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " like " + value;
                } else if (dbType == DbType.ORACLE) {
                    if (leftFix && rightFix) {
                        value = "'%' || ? || '%'";
                    } else if (leftFix) {
                        value = "'%' || ?";
                    } else if (rightFix) {
                        value = "? || '%'";
                    } else {
                        value = "'" + valueObj + "'";
                    }
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " like " + value;
                } else {
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " like '" + getValue() + "'";
                }
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
    public static Operate NotLike(String key, Object value) {
        return NotLike(DEFAULT_TABLE, key, value);
    }

    public static Operate NotLike(String tableName, String key, Object value) {
        return new RelationOperate(tableName, key, NeoConstant.NOT_LIKE, value) {

            private boolean leftFix = false;
            private boolean rightFix = false;
            private DbType dbType;

            @Override
            public Boolean valueLegal() {
                Neo db = NeoContext.getNeo();
                if (null == db) {
                    dbType = DbType.MYSQL;
                } else {
                    dbType = db.getDbType();
                }

                Object valueObj = getValue();
                if (!(valueObj instanceof String)) {
                    return false;
                }
                String value = ((String) valueObj).trim();
                if (value.startsWith("%")) {
                    value = value.substring(1).trim();
                    leftFix = true;
                }

                if (value.endsWith("%")) {
                    value = value.substring(0, value.length() - 1).trim();
                    rightFix = true;
                }

                if ("null".equals(value)) {
                    return false;
                }

                if (CharSequenceUtil.isNotEmpty(value)) {
                    if (leftFix) {
                        value = "%" + value;
                    }

                    if (rightFix) {
                        value = value + "%";
                    }
                    setValue(value);
                    return true;
                }
                return false;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                if (dbType == DbType.MYSQL || dbType == DbType.MARIADB || dbType == DbType.ORACLE) {
                    if (!leftFix && !rightFix) {
                        return NeoQueue.of();
                    }
                    return super.getValueQueue();
                } else {
                    return NeoQueue.of();
                }
            }

            @Override
            public String generateOperate() {
                Object valueObj = getValue();
                String value;

                Neo db = NeoContext.getNeo();
                DbType dbType;
                if (null == db) {
                    dbType = DbType.MYSQL;
                } else {
                    dbType = db.getDbType();
                }
                if (dbType == DbType.MYSQL || dbType == DbType.MARIADB) {
                    if (leftFix && rightFix) {
                        value = "concat('%', ?, '%')";
                    } else if (leftFix) {
                        value = "concat('%', ?)";
                    } else if (rightFix) {
                        value = "concat(?, '%')";
                    } else {
                        value = "'" + valueObj + "'";
                    }
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " not like " + value;
                } else if (dbType == DbType.ORACLE) {
                    if (leftFix && rightFix) {
                        value = "'%' || ? || '%'";
                    } else if (leftFix) {
                        value = "'%' || ?";
                    } else if (rightFix) {
                        value = "? || '%'";
                    } else {
                        value = "'" + valueObj + "'";
                    }
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " not like " + value;
                } else {
                    return SqlBuilder.toDbField(getTable(), getColumn()) + " not like '" + getValue() + "'";
                }
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
    public static Operate In(String key, Object collection) {
        return In(DEFAULT_TABLE, key, collection);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate In(String tableName, String key, Object collection) {
        return new RelationOperate(tableName, key, NeoConstant.IN, collection) {

            @Override
            public Boolean valueLegal() {
                Object valueObj = getValue();
                if (!(valueObj instanceof Collection)) {
                    return false;
                }
                return null != getValue() && !((Collection)valueObj).isEmpty();
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), super.getColumn()) + " in " + SqlBuilder.buildIn((Collection)getValue());
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
    public static Operate NotIn(String key, Object collection) {
        return NotIn(DEFAULT_TABLE, key, collection);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate NotIn(String tableName, String key, Object collection) {
        return new RelationOperate(tableName, key, NeoConstant.ONT_IN, collection) {

            @Override
            public Boolean valueLegal() {
                Object valueObj = getValue();
                if (!(valueObj instanceof Collection)) {
                    return false;
                }
                return null != getValue() && !((Collection)valueObj).isEmpty();
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), getColumn()) + " not in " + SqlBuilder.buildIn((Collection)getValue());
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
        return IsNull(DEFAULT_TABLE, key);
    }

    public static Operate IsNull(String tableName, String key) {
        return new RelationOperate(tableName, key, NeoConstant.IS_NULL, null) {

            @Override
            public Boolean valueLegal() {
                return CharSequenceUtil.isNotEmpty(super.getColumn());
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(getTable(), getColumn()) + " is null";
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
        return IsNotNull(DEFAULT_TABLE, key);
    }

    public static Operate IsNotNull(String tableName, String key) {
        return new RelationOperate(tableName, key, NeoConstant.IS_NOT_NULL, null) {

            @Override
            public Boolean valueLegal() {
                return CharSequenceUtil.isNotEmpty(super.getColumn());
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(getTable(), getColumn()) + " is not null";
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
        return GroupBy(DEFAULT_TABLE, key);
    }

    public static Operate GroupBy(String tableName, String key) {
        return new RelationOperate(tableName, key, NeoConstant.GROUP_BY, null) {

            @Override
            public Boolean valueLegal() {
                return CharSequenceUtil.isNotEmpty(key);
            }

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return " group by " + SqlBuilder.toDbField(getTable(), getColumn());
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
        return OrderByTable(DEFAULT_TABLE, kDescAsc);
    }

    public static Operate OrderByTable(String tableName, String... kDescAsc) {
        return new RelationOperate(tableName, null, NeoConstant.ORDER_BY, new LinkedList<>(Arrays.asList(kDescAsc))) {

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
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
                        columnNameList.add(SqlBuilder.toDbField(getTable(), value));
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
        return OrderByDesc(DEFAULT_TABLE, key);
    }

    public static Operate OrderByDesc(String tableName, String key) {
        return new RelationOperate(tableName, key, null, null) {

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " order by " + SqlBuilder.toDbField(getTable(), getColumn()) + " desc";
            }
        };
    }

    public static Operate BetweenAnd(String key, Object leftValue, Object rightValue) {
        return BetweenAnd(DEFAULT_TABLE, key, leftValue, rightValue);
    }

    public static Operate BetweenAnd(String tableName, String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(tableName, key, leftValue, rightValue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), getColumn()) + " between ? and ? ";
            }
        };
    }

    public static Operate NotBetweenAnd(String key, Object leftValue, Object rightValue) {
        return NotBetweenAnd(DEFAULT_TABLE, key, leftValue, rightValue);
    }

    public static Operate NotBetweenAnd(String tableName, String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(tableName, key, leftValue, rightValue) {

            @Override
            public String generateOperate() {
                if (!valueLegal()) {
                    return "";
                }
                return SqlBuilder.toDbField(getTable(), getColumn()) + " not between ? and ? ";
            }
        };
    }

    public static Operate Page(PageReq<Object> pageReq) {
        return new RelationOperate(null, null) {

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " limit " + pageReq.getSize() + " offset " + pageReq.getStartIndex();
            }
        };
    }

    @Deprecated
    public static Operate Page(NeoPage neoPage) {
        return new RelationOperate(null, null) {

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                return " limit " + neoPage.getSize() + " offset " + neoPage.getStartIndex();
            }
        };
    }

    public static Operate Page(Integer pageNo, Integer pageSize) {
        return new RelationOperate(null, null) {

            /**
             * 不需要value
             * @return false
             */
            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public Boolean doNeedWhere() {
                return false;
            }

            @Override
            public String generateOperate() {
                int startIndex = 0;
                if (pageNo > 0) {
                    startIndex = (pageNo - 1) * pageSize;
                }
                return " limit " + pageSize + " offset " + startIndex;
            }
        };
    }

    public static Operate Exists(String sql) {
        return new RelationOperate(null, sql) {

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                return " exists (" + sql + ") ";
            }
        };
    }

    public static Operate NotExists(String sql) {
        return new RelationOperate(null, sql) {

            @Override
            public NeoQueue<Object> getValueQueue() {
                return NeoQueue.of();
            }

            @Override
            public String generateOperate() {
                return " not exists (" + sql + ") ";
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


