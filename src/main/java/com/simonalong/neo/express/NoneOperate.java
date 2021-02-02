package com.simonalong.neo.express;

import com.simonalong.neo.NeoQueue;
import com.simonalong.neo.util.CharSequenceUtil;

/**
 * 无操作，只是部分sql拼接
 *
 * @author shizi
 * @since 2020/9/2 5:25 下午
 */
public class NoneOperate extends BaseOperate{

    private String partSql;

    public NoneOperate(String operateSymbol, String partSql) {
        super();
        super.setOperateSymbol(operateSymbol);
        this.partSql = partSql;
    }

    @Override
    public String generateOperate() {
        return " " + partSql;
    }

    @Override
    public Boolean doNeedWhere() {
        return false;
    }

    @Override
    public Boolean valueLegal() {
        return CharSequenceUtil.isNotEmpty(partSql);
    }

    @Override
    public NeoQueue<Object> getValueQueue() {
        return null;
    }
}
