package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Getter;

/**
 * 关系运算符
 * <p>{@code <、>、<>、!=、=、>=、<=}
 *
 * @author shizi
 * @since 2020/8/30 12:50 上午
 */
public abstract class RelationOperate extends BaseOperate {

    @Getter
    private final String key;
    @Getter
    private final Object value;

    public RelationOperate(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 判断value是否满足要求
     *
     * @return true：满足，false：不满足
     */
    @Override
    public Boolean valueLegal() {
        return ObjectUtil.isNotEmpty(value);
    }

    @Override
    public NeoQueue<Object> getValueQueue() {
        if (!valueLegal()) {
            return NeoQueue.of();
        }
        NeoQueue<Object> result = NeoQueue.of();
        if (null != value) {
            result.addLast(value);
        }
        return result;
    }
}
