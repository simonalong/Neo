package com.simonalong.neo.express;

import com.simonalong.neo.sql.builder.SqlBuilder;

import static com.simonalong.neo.util.LogicOperateUtil.*;

import java.util.LinkedList;
import java.util.Queue;

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
        return " and (" + filterLogicHead(String.join(" and ", sqlPartQueue)) + ")";
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
        return " and " + filterLogicHead(String.join(" and ", sqlPartQueue));
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
        return " or (" + filterLogicHead(String.join(" or ", sqlPartQueue)) + ")";
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
        return " or " + filterLogicHead(String.join(" or ", sqlPartQueue));
    }

    /**
     * 无符号的处理
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
        return " " + filterLogicHead(String.join(" ", sqlPartQueue));
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

    private static Queue<String> doGenerateSqlPart(Queue<Operate> queue) {
        if (null == queue || queue.isEmpty()) {
            return new LinkedList<>();
        }
        Operate operate, operateInner;
        Queue<String> sqlPartQueue = new LinkedList<>();
        while ((operate = queue.poll()) != null) {
            Queue<Operate> childOperateQueue = operate.getChildQueue();
            if (null == childOperateQueue || childOperateQueue.isEmpty()) {
                sqlPartQueue.offer(filterLogicHead(operate.generateOperate()));
            } else {
                while ((operateInner = childOperateQueue.poll()) != null) {
                    sqlPartQueue.offer(filterLogicHead(operateInner.generateOperate()));
                }
            }
        }
        return sqlPartQueue;
    }
}
