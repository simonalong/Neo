package com.simon.neo.util;

import com.simon.neo.NeoMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

/**
 * 时间的时区转换工具
 *
 * @author zhouzhenyong
 * @since 2019/5/14 下午9:24
 */
@UtilityClass
public class DateZoneUtil {

    /**
     * 时间类型的时区转换
     * @param dataMap 待转换的数据map
     * @param timeZoneId 时区的id，比如：UTC（世界标准时间），CST（北京时间），GMT（格林尼治平均时间）, 具体可看{@code ZoneId}
     * @param timeKeys 数据map中待转换的key
     */
    public void parseTimeWithZone(NeoMap dataMap, String timeZoneId, String... timeKeys){
        if (NeoMap.isEmpty(dataMap)) {
            return;
        }
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        parse.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        Stream.of(timeKeys).forEach(t -> {
            if (dataMap.containsKey(t)) {
                try {
                    dataMap.put(t, parse.parse(dataMap.getStr(t)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析带有时区时间为通用的时间
     */
    public void parseTimeUTC(NeoMap dataMap, String... timeKeys){
        parseTimeWithZone(dataMap, "UTC", timeKeys);
    }
}
