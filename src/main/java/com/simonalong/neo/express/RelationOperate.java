package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

/**
 * 关系运算符
 * <p>{@code <、>、<>、!=、=、>=、<=}
 *
 * @author shizi
 * @since 2020/8/30 12:50 上午
 */
public abstract class RelationOperate extends BaseOperate {

    private final String column;
    private final String tableName;
    @Getter
    @Setter
    private Object value;

    public RelationOperate(String column, Object value) {
        this.tableName = DEFAULT_TABLE;
        this.column = column;
        this.value = value;
    }

    public RelationOperate(String column, String operateSymbol, Object value) {
        this.setOperateSymbol(operateSymbol);
        this.tableName = DEFAULT_TABLE;
        this.column = column;
        this.value = value;
    }

    public RelationOperate(String tableName, String column, String operateSymbol, Object value) {
        this.setOperateSymbol(operateSymbol);
        this.tableName = tableName;
        this.column = column;
        this.value = value;
    }

    @Override
    public String getTable() {
        return tableName;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Boolean doNeedWhere() {
        return true;
    }

    @Override
    public Object getValueFromColumnOfOperate(String columnName, String operateSymbol) {
        if (null != getOperateSymbol()) {
            if (getOperateSymbol().equals(operateSymbol) && getColumn().equals(columnName)) {
                return getValue();
            }
        }

        return null;
    }

    @Override
    public Object getValueFromColumnOfOperate(String tableName, String columnName, String operateSymbol) {
        if (null != getOperateSymbol()) {
            if (getOperateSymbol().equals(operateSymbol) && getColumn().equals(columnName)) {
                return getValue();
            }
        }

        return null;
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
