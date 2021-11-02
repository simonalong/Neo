package com.simonalong.neo.uid;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import org.junit.Assert;
import org.junit.Test;

import static com.simonalong.neo.uid.UuidConstant.*;

/**
 * @author shizi
 * @since 2020/3/23 下午1:58
 */
public class UuidBaseTest extends NeoBaseTest {

    public String parseUid(Long uid) {
        return UuidGenerator.parseUUid(uid).toString();
    }

    @Test
    public void test1() {
        int workerId = 0;
        long seq = 1;
        long time = System.currentTimeMillis();

        NeoMap valueMap1 = UuidGenerator.parseUUid(getData(time, seq, workerId));
        Assert.assertEquals(valueMap1.getLong("sequence").longValue(), seq);

        NeoMap valueMap2 = UuidGenerator.parseUUid(getData(time, seq + 1, workerId));
        Assert.assertEquals(valueMap2.getLong("sequence").longValue(), seq + 1);

        NeoMap valueMap3 = UuidGenerator.parseUUid(getData(time, seq + 2, workerId));
        Assert.assertEquals(valueMap3.getLong("sequence").longValue(), seq + 2);

        NeoMap valueMap4 = UuidGenerator.parseUUid(getData(time, seq + 3, workerId));
        Assert.assertEquals(valueMap4.getLong("sequence").longValue(), seq + 3);

        NeoMap valueMap5 = UuidGenerator.parseUUid(getData(time, seq + 4, workerId));
        Assert.assertEquals(valueMap5.getLong("sequence").longValue(), seq + 4);
    }

    private long getData(long time, long seq, long workerId) {
        return (time << ((SEQ_HIGH_BITS + WORKER_BITS + SEQ_LOW_BITS)) | (((seq << WORKER_BITS) & SEQ_HIGH_MARK)) | ((workerId << SEQ_LOW_BITS) & WORKER_MARK) | (seq & SEQ_LOW_MARK));
    }
}
