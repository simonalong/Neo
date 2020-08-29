package com.simonalong.neo.express;

import lombok.Getter;

import java.util.Collection;

/**
 * 关系运算符
 * <p>{@code <、>、<>、!=、=、>=、<=}
 *
 * @author shizi
 * @since 2020/8/30 12:50 上午
 */
public abstract class RelationOperate extends BaseOperate {

    @Getter
    private String key;
    @Getter
    private Object value;

    public RelationOperate(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Boolean haveCondition() {
        if (null == value) {
            return false;
        } if(value instanceof String) {
            String valueStr = (String) value;
            return !"".equals(valueStr);
        } if(value instanceof Collection) {
            Collection collection = (Collection) value;
            return !collection.isEmpty();
        }
        return true;
    }
}
