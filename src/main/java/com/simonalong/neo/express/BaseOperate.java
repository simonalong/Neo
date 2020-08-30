package com.simonalong.neo.express;

import com.simonalong.neo.NeoPageReq;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.util.LogicOperateUtil;
import com.simonalong.neo.util.ObjectUtil;

import static com.simonalong.neo.util.LogicOperateUtil.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/29 11:32 下午
 */
public abstract class BaseOperate implements Operate {

    protected Queue<Operate> childOperateQueue = new LinkedList<>();

    public BaseOperate() {}

    public BaseOperate(Queue<Operate> operateQueue) {
        this.childOperateQueue = operateQueue;
    }

    @Override
    public void offerOperate(Operate value) {
        this.childOperateQueue.offer(value);
    }

    @Override
    public Boolean offerOperateQueue(Queue<Operate> valueQueue) {
        return this.childOperateQueue.addAll(valueQueue);
    }

    @Override
    public Queue<Operate> getChildQueue() {
        return this.childOperateQueue;
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
    public static Operate And(Queue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.AND, operateQueue) {

            @Override
            public String generateOperate() {
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate And(Operate operate) {
        return new LogicOperate(Express.LogicEnum.AND, operate) {

            @Override
            public String generateOperate() {
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate And(String key, Object value) {
        return new LogicOperate(Express.LogicEnum.AND, Equal(key, value)) {

            @Override
            public String generateOperate() {
                return andGenerateOperate(super.childOperateQueue);
            }
        };
    }

    private static String andGenerateOperate(Queue<Operate> queue) {
        Queue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).collect(Collectors.toList());
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

    public static Operate AndEm(Queue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.AND_EM, operateQueue) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate AndEm(Operate operate) {
        return new LogicOperate(Express.LogicEnum.AND_EM, operate) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate AndEm(String key, Object value) {
        return new LogicOperate(Express.LogicEnum.AND_EM, Equal(key, value)) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    private static String andEmGenerateOperate(Queue<Operate> queue) {
        Queue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateStrList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).collect(Collectors.toList());
        return " and " + filterLogicHead(String.join(" and ", operateStrList));
    }

    /**
     * 有括号的or
     *
     * @return 操作类
     */
    public static Operate Or(Object... objects) {
        return Or(Operate.parse(Express.LogicEnum.OR_EM, objects));
    }

    public static Operate Or(Queue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.OR, operateQueue) {

            @Override
            public String generateOperate() {
                return orGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate Or(Operate operate) {
        return new LogicOperate(Express.LogicEnum.OR, operate) {

            @Override
            public String generateOperate() {
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
                return orGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String orGenerateOperate(Queue<Operate> queue) {
        Queue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).collect(Collectors.toList());
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

    public static Operate OrEm(Queue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.OR_EM, operateQueue) {

            @Override
            public String generateOperate() {
                return orEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate OrEm(Operate operate) {
        return new LogicOperate(Express.LogicEnum.OR_EM, operate) {

            @Override
            public String generateOperate() {
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
                return orEmGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String orEmGenerateOperate(Queue<Operate> queue) {
        Queue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).collect(Collectors.toList());
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

    public static Operate Em(Queue<Operate> operateQueue) {
        return new LogicOperate(Express.LogicEnum.EMPTY, operateQueue) {

            @Override
            public String generateOperate() {
                return emGenerateOperate(super.childOperateQueue);
            }
        };
    }

    public static Operate Em(Operate operate) {
        return new LogicOperate(Express.LogicEnum.EMPTY, operate) {

            @Override
            public String generateOperate() {
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
                return emGenerateOperate(super.childOperateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String emGenerateOperate(Queue<Operate> queue) {
        Queue<String> sqlPartQueue = doGenerateSqlPart(queue);
        if (sqlPartQueue.isEmpty()) {
            return "";
        }

        List<String> operateList = sqlPartQueue.stream().map(LogicOperateUtil::filterLogicHead).collect(Collectors.toList());
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
        return new RelationOperate(key, value) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " like '" + super.getValue() + "'";
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
        return new RelationOperate(key, value) {

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not like '" + super.getValue() + "'";
            }
        };
    }

    /**
     * in集合处理
     * @param key key
     * @param collection 待匹配的集合
     * @return 模糊匹配的字符串
     */
    public static Operate In(String key, Collection<Object> collection) {
        return new RelationOperate(key, collection) {

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
    public static Operate NotIn(String key, Collection<Object> collection) {
        return new RelationOperate(key, collection) {

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
            public Boolean valueLegal() {
                return true;
            }

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
            public Boolean valueLegal() {
                return true;
            }

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
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return " group by" + SqlBuilder.toDbField(super.getKey());
            }
        };
    }

    public static Operate OrderBy(String key) {
        return new RelationOperate(key, null) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return " group by" + SqlBuilder.toDbField(super.getKey());
            }
        };
    }

    public static Operate OrderBy(String key, String descOrAsc) {
        return new RelationOperate(key, null) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return " order by" + SqlBuilder.toDbField(super.getKey()) + " " + descOrAsc;
            }
        };
    }

    public static Operate OrderByDesc(String key) {
        return new RelationOperate(key, null) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return " order by" + SqlBuilder.toDbField(super.getKey()) + " desc";
            }
        };
    }

    public static Operate BetweenAnd(String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(key, leftValue, rightValue) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " between ? and ? ";
            }
        };
    }

    public static Operate NotBetweenAnd(String key, Object leftValue, Object rightValue) {
        return new BiRelationOperate(key, leftValue, rightValue) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " not between ? and ? ";
            }
        };
    }

    public static Operate Page(NeoPageReq<Object> neoPageReq) {
        return new RelationOperate(null, null) {

            @Override
            public Boolean valueLegal() {
                return true;
            }

            @Override
            public String generateOperate() {
                return SqlBuilder.toDbField(super.getKey()) + " limit " + neoPageReq.getPageSize() + " offset " + neoPageReq.getPageIndex();
            }
        };
    }

    private static Queue<String> doGenerateSqlPart(Queue<Operate> queue) {
        if (null == queue || queue.isEmpty()) {
            return new LinkedList<>();
        }
        Operate operate;
        Queue<String> sqlPartQueue = new LinkedList<>();
        while ((operate = queue.poll()) != null) {
            sqlPartQueue.offer(operate.generateOperate());
        }
        return sqlPartQueue;
    }
}
