package com.simonalong.neo.uid;

import com.simonalong.neo.NeoBaseTest;
import org.junit.Test;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.simonalong.neo.uid.UuidConstant.*;

/**
 * @author shizi
 * @since 2020/3/23 下午1:58
 */
public class UuidBaseTest extends NeoBaseTest {

    private long symbolMark = 1 << (SYMBOL_LEFT_SHIFT);
    private long timeMark = (~(-1L << TIME_BITS)) << TIME_LEFT_SHIFT;
    private long seqMark = (~(-1L << SEQ_BITS)) << SEQ_LEFT_SHIFT;
    private long workerMark = ~(-1L << WORKER_BITS);

    public String parseUid(Long uid) {
        StringBuilder result = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        result.append("uuid：" + uid + "; ");
        result.append("符号位是：" + ((uid & symbolMark) >>> SYMBOL_LEFT_SHIFT) + "; ");
        result.append("time是：" + ((uid & timeMark) >> TIME_LEFT_SHIFT) + "; ");
        result.append("相对时间是：" + dateFormat.format(new Date(((uid & timeMark) >> TIME_LEFT_SHIFT) + START_TIME)) + "; ");
        result.append("seq是：" + ((uid & seqMark) >> SEQ_LEFT_SHIFT) + "; ");
        result.append("worker是：" + (uid & workerMark) + "; ");

        return result.toString();
    }

    @Test
    public void test1() {
        long symbol = 0;
        long time = 310651626;
        long seq = 328;
        long workerId = 1;
        long tem = (symbol << SYMBOL_LEFT_SHIFT | (time << TIME_LEFT_SHIFT) | (seq << SEQ_LEFT_SHIFT) | workerId);
        System.out.println(tem);
        System.out.println(parseUid(tem));
    }
}
