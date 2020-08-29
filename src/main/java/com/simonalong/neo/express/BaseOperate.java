package com.simonalong.neo.express;

import com.simonalong.neo.sql.builder.SqlBuilder;

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

    /**
     * 有括号的and
     *
     * @return 操作类
     */
    public static Operate And(Object... objects) {
        return new LogicOperate(Operate.parse(Express.LogicOperate.AND, objects)) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.operateQueue);
            }
        };
    }

    public static Operate And(String key, Object value) {
        return new LogicOperate(Equal(key, value)) {

            @Override
            public String generateOperate() {
                return andEmGenerateOperate(super.operateQueue);
            }
        };
    }

    private static String andGenerateOperate(Queue<Operate> operateQueue){
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" and ").append(operateStr);
        }

        return stringBuilder.toString();
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

    private static String andEmGenerateOperate(Queue<Operate> operateQueue){
        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" and ").append(operateStr);
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
                Queue<Operate> operateQueue = super.operateQueue;
                Operate operate;
                StringBuilder stringBuilder = new StringBuilder();
                while ((operate = operateQueue.poll()) != null) {
                    String operateStr = operate.generateOperate();
                    stringBuilder.append(" or ").append("(").append(operateStr).append(") ");
                }

                String result = stringBuilder.toString();
                if (result.startsWith(" or ")) {
                    return result.substring(" or ".length());
                }
                return stringBuilder.toString();
            }
        };
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
                Queue<Operate> operateQueue = super.operateQueue;
                Operate operate;
                StringBuilder stringBuilder = new StringBuilder();
                while ((operate = operateQueue.poll()) != null) {
                    String operateStr = operate.generateOperate();
                    stringBuilder.append(" or ").append(operateStr);
                }

                return stringBuilder.toString();
            }
        };
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
                Queue<Operate> operateQueue = super.operateQueue;
                Operate operate;
                StringBuilder stringBuilder = new StringBuilder();
                while ((operate = operateQueue.poll()) != null) {
                    String operateStr = operate.generateOperate();
                    stringBuilder.append(" ").append(operateStr);
                }

                return stringBuilder.toString();
            }
        };
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
                return " " + SqlBuilder.toDbField(super.getKey()) + " = ? ";
            }
        };
    }
}
