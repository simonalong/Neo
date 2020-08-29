package com.simonalong.neo.express;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.NumberOfValueException;

import static com.simonalong.neo.express.BaseOperate.*;

import java.util.*;

/**
 * 运算符
 *
 * @author shizi
 * @since 2020/8/29 4:36 下午
 */
public interface Operate {

    /**
     * 生成运算符
     *
     * @return 运算符表达式
     */
    String generateOperate();

    /**
     * 添加运算符数据
     *
     * @param value 待加入的运算
     * @return true: 加入成功呢，false：加入失败
     */
    Boolean offerOperate(Operate value);

    /**
     * 添加运算符队列
     *
     * @param valueQueue 队列
     * @return true: 加入成功呢，false：加入失败
     */
    Boolean offerOperateQueue(Queue<Operate> valueQueue);

    /**
     * 值是否合法
     *
     * @return true：合法，false：不合法
     */
    Boolean valueLegal();

    /**
     * 解析数据为操作符队列
     *
     * @param logicOperate 逻辑操作符
     * @param objects      对象
     * @return 操作符队列
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static Queue<Operate> parse(Express.LogicOperate logicOperate, Object... objects) {
        Queue<Operate> operateQueue = new LinkedList<>();
        List<Object> parameters = Arrays.asList(objects);

        for (int index = 0; index < parameters.size(); index++) {
            Object parameter = parameters.get(index);

            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                Operate operate;
                String key = (String) parameter;
                Object value = parameters.get(index++);

                if (Express.LogicOperate.AND.equals(logicOperate)) {
                    operate = AndEm(key, value);
                } else if (Express.LogicOperate.OR.equals(logicOperate)) {
                    operate = OrEm(key, value);
                } else {
                    operate = Em(key, value);
                }

                if (index + 1 > parameters.size()) {
                    throw new NumberOfValueException("operate参数个数key-value有误");
                }

                operateQueue.add(operate);
                continue;
            }

            // Operate类型处理
            if (parameter instanceof Operate) {
                operateQueue.add((Operate) parameter);
            }

            // NeoQueue
            if(parameter instanceof Collection) {
                operateQueue.addAll((Collection) parameter);
            }
        }
        return operateQueue;
    }
}
