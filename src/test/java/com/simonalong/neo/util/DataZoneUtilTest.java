package com.simonalong.neo.util;

import static com.simonalong.neo.util.DateZoneUtil.*;

import com.simonalong.neo.NeoMap;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/14 下午9:48
 */
public class DataZoneUtilTest {

    @Test
    public void parseTimeWithZone(){
        String time = "2019-05-14T13:11:07.769+0000";
        NeoMap data = NeoMap.of("a", time);
        DateZoneUtil.parseTimeUTC(data, "a");
    }
}
