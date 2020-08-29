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

    protected Queue<Operate> operateQueue = new LinkedList<>();

    public BaseOperate() {}

    public BaseOperate(Queue<Operate> operateQueue) {
        this.operateQueue = operateQueue;
    }

    @Override
    public Boolean offerOperate(Operate value) {
        return this.operateQueue.offer(value);
    }

    @Override
    public Boolean offerOperateQueue(Queue<Operate> valueQueue){
        return this.operateQueue.addAll(valueQueue);
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate And(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.AND, objects)) {

            @Override
            public String generateOperate() {
                return andGenerateOperate(super.operateQueue);
            }
        };
    }

    public static Operate And(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return andGenerateOperate(super.operateQueue);
            }
        };
    }

    private static String andGenerateOperate(Queue<Operate> operateQueue) {
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            stringBuilder.append(" and ").append(filterLogicHead(operate.generateOperate().trim()));
        }

        String result = stringBuilder.toString().trim();
        return " and (" + filterLogicHead(result) + ")";
    }

    /**
     * 无括号的and
     *
     * @return 操作类
     */
    public static Operate AndEm(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.AND, objects)) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.operateQueue);
            }
        };
    }

    public static Operate AndEm(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.operateQueue);
            }
        };
    }

    private static String andEmGenerateOperate(Queue<Operate> operateQueue) {
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" and ").append(filterLogicHead(operateStr));
        }

        return stringBuilder.toString();
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate Or(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.OR, objects)) {

            @Override
            public String generateOperate() {
                return orGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate Or(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return orGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String orGenerateOperate(Queue<Operate> operateQueue) {
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            stringBuilder.append(" or ").append(filterLogicHead(operate.generateOperate().trim()));
        }

        String result = stringBuilder.toString().trim();
        return " or (" + filterLogicHead(result) + ")";
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate OrEm(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.OR, objects)) {

            @Override
            public String generateOperate() {
                return orEmGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate OrEm(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return orEmGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String orEmGenerateOperate(Queue<Operate> operateQueue) {
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" or ").append(filterLogicHead(operateStr));
        }

        return stringBuilder.toString();
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate Em(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.EMPTY, objects)) {

            @Override
            public String generateOperate() {
                return emGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate Em(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return emGenerateOperate(super.operateQueue);
            }
        };
    }

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static String emGenerateOperate(Queue<Operate> operateQueue) {
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" ").append(operateStr);
        }

        return stringBuilder.toString();
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
}
