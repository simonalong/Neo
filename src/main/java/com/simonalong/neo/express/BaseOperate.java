package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.util.LogicOperateUtil;

import static com.simonalong.neo.util.LogicOperateUtil.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/29 11:32 下午
 */
public abstract class BaseOperate implements Operate {

    protected NeoQueue<Operate> childOperateQueue = NeoQueue.of();

    public BaseOperate() {}

    public BaseOperate(NeoQueue<Operate> operateQueue) {
        this.childOperateQueue = operateQueue;
    }

    @Override
    public void offerOperate(Operate value) {
        this.childOperateQueue.offer(value);
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate And(Object... objects) {
        return And(Operate.parse(Express.LogicEnum.AND_EM, objects));
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate And(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.AND, operateQueue) {

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
        return new LogicOperate(Express.LogicEnum.AND, operate) {

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
        return new LogicOperate(Express.LogicEnum.AND, Equal(key, value)) {

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
     * @return 操作类
     */
    public static Operate AndEm(Object... objects) {
        return AndEm(Operate.parse(Express.LogicEnum.AND_EM, objects));
    }

    public static Operate AndEm(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.AND_EM, operateQueue) {

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
        return new LogicOperate(Express.LogicEnum.AND_EM, operate) {

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
        return new LogicOperate(Express.LogicEnum.AND_EM, Equal(key, value)) {

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
     * @return 操作类
     */
    public static Operate Or(Object... objects) {
        return Or(Operate.parse(Express.LogicEnum.OR_EM, objects));
    }

    public static Operate Or(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.OR, operateQueue) {

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
        return new LogicOperate(Express.LogicEnum.OR, operate) {

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
     * @return 操作类
     */
    public static Operate Or(String key, Object value) {
        return new LogicOperate(Express.LogicEnum.OR, Equal(key, value)) {

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
     * @return 操作类
     */
    public static Operate OrEm(Object... objects) {
        return OrEm(Operate.parse(Express.LogicEnum.OR_EM, objects));
    }

    public static Operate OrEm(NeoQueue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.OR_EM, operateQueue) {

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
        return new LogicOperate(Express.LogicEnum.OR_EM, operate) {

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
     * @return 操作类
     */
    public static Operate OrEm(String key, Object value) {
        return new LogicOperate(Express.LogicEnum.OR_EM, Equal(key, value)) {

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
        return new LogicOperate(Express.LogicEnum.EMPTY, operateQueue) {

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
        return new LogicOperate(Express.LogicEnum.EMPTY, operate) {

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
     * @return 操作类
     */
    public static Operate Em(String key, Object value) {
        return new LogicOperate(Express.LogicEnum.EMPTY, Equal(key, value)) {

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
        return new RelationOperate(key, value) {

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
        return new RelationOperate(key, value) {

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
        return new RelationOperate(key, value) {

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
        return new RelationOperate(key, value) {

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
        return new RelationOperate(key, value) {

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
        return new RelationOperate(key, value) {

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
     * @param key key
     * @param value value
     * @return 模糊匹配的字符串
     */
    public static Operate Like(String key, String value) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " like '" + value + "'";
            }
        };
    }

    /**
     * not like模糊搜索
     * @param key key
     * @param value value
     * @return 模糊匹配的字符串
     */
    public static Operate NotLike(String key, String value) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not like '" + value + "'";
            }
        };
    }

    /**
     * in集合处理
     * @param key key
     * @param collection 待匹配的集合
     * @return 模糊匹配的字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate In(String key, Collection collection) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " in " + SqlBuilder.buildIn(collection);
            }
        };
    }

    /**
     * not in集合处理
     * @param key key
     * @param collection 待匹配的集合
     * @return 模糊匹配的字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Operate NotIn(String key, Collection collection) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not in " + SqlBuilder.buildIn(collection);
            }
        };
    }

    /**
     * is null
     * @param key key
     * @return 模糊匹配的字符串
     */
    public static Operate IsNull(String key) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " is null";
            }
        };
    }

    /**
     * is not null
     * @param key key
     * @return 模糊匹配的字符串
     */
    public static Operate IsNotNull(String key) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " is not null";
            }
        };
    }

    /**
     * group by
     * @param key key
     * @return group by字符串
     */
    public static Operate GroupBy(String key) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return " group by " + SqlBuilder.toDbField(super.getKey());
            }
        };
    }

    public static Operate OrderBy(String key) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return " order by " + SqlBuilder.toDbField(super.getKey());
            }
        };
    }

    public static Operate OrderBy(String key, String descOrAsc) {
        return new RelationOperate(key, null) {

            @Override
            public String generateOperate() {
                return " order by " + SqlBuilder.toDbField(super.getKey()) + " " + descOrAsc;
            }
        };
    }

    public static Operate OrderByDesc(String key) {
        return new RelationOperate(key, null) {

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
