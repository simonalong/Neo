package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Getter;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

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
        super(DEFAULT_TABLE, key, null, null);
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public BiRelationOperate(String tableName, String key, Object leftValue, Object rightValue) {
        super(tableName, key, null, null);
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public Boolean doNeedWhere() {
        return true;
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
        if (!valueLegal()) {
            return NeoQueue.of();
        }
        NeoQueue<Object> result = NeoQueue.of();
        result.addLast(leftValue);
        result.addLast(rightValue);
        return result;
    }
}
