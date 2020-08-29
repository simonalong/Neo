package com.simonalong.neo.express;

import com.simonalong.neo.exception.NumberOfValueException;

import static com.simonalong.neo.express.BaseOperate.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    //

    /**
     * 添加运算符数据
     *
     * @param value 待加入的运算
     * @return true: 加入成功呢，false：加入失败
     */
    Boolean offerOperate(Operate value);
    //
    //    boolean offerOperateQueue(Queue<Operate> valueQueue);

    Boolean haveCondition();

    //    String toSql();

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
                } else if(Express.LogicOperate.OR.equals(logicOperate)){
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
                continue;
            }

            // todo 后面处理NeoMap的类型
            // NeoMap处理
            //            if (parameter instanceof NeoMap) {
            //                 NeoMap转换到Operate
            //                operateList.add((NeoMap)parameter);
            //            }
        }
        return operateQueue;
    }
}
