package com.simonalong.neo.xa;

import java.util.UUID;
import javax.transaction.xa.Xid;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhenyong
 * @since 2020/1/20 下午7:10
 */
@EqualsAndHashCode
public class NeoXid implements Xid {

    /**
     * 全局事务id
     */
    private final String gTxId;
    /**
     * 分支
     */
    private final String branchQualifier;
    /**
     * 在这里我们不使用这个字段
     */
    private final int formatId = 1;

    public NeoXid() {
        gTxId = UUID.randomUUID().toString();
        branchQualifier = String.valueOf(System.currentTimeMillis());
    }

    @Override
    public int getFormatId(){
        return formatId;
    }

    @Override
    public byte[] getGlobalTransactionId(){
        return gTxId.getBytes();
    }

    @Override
    public byte[] getBranchQualifier(){
        return branchQualifier.getBytes();
    }
}
