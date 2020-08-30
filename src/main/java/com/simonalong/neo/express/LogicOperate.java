package com.simonalong.neo.express;

import java.util.Queue;

/**
 * 逻辑运算符
 * <p>{@code and、or、not（但是在数据库中，not暂时不作为处理）}
 *
 * @author shizi
 * @since 2020/8/30 12:52 上午
 */
public abstract class LogicOperate extends BaseOperate {

    private final Express.LogicEnum logicEnum;

    public LogicOperate(Express.LogicEnum logicEnum, Operate operate) {
        super();
        super.offerOperate(operate);
        this.logicEnum = logicEnum;
    }

    public LogicOperate(Express.LogicEnum logicEnum, Queue<Operate> operateQueue) {
        super(operateQueue);
        this.logicEnum = logicEnum;
    }

    @Override
    public Boolean valueLegal() {
        return true;
    }
}
