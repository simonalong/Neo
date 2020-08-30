package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Getter;

/**
 * @author shizi
 * @since 2020/8/30 2:11 下午
 */
public abstract class BiRelationOperate extends RelationOperate {

    @Getter
    private final Object leftValue;
    @Getter
    private final Object rightValue;

    public BiRelationOperate(String key, Object leftValue, Object rightValue) {
        super(key, null);
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    /**
     * 判断value是否满足要求
     * @return true：满足，false：不满足
     */
    @Override
    public Boolean valueLegal() {
        return ObjectUtil.isNotEmpty(leftValue) && ObjectUtil.isNotEmpty(leftValue);
    }

    @Override
    public NeoQueue<Object> getValueQueue() {
        NeoQueue<Object> result = NeoQueue.of();
        result.addLast(leftValue);
        result.addLast(rightValue);
        return result;
    }
}
