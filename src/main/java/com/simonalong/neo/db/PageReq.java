package com.simonalong.neo.db;

import com.simonalong.mikilin.annotation.Matcher;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shizi
 * @since 2020/8/31 4:57 下午
 */
@Data
@AllArgsConstructor
public class PageReq<T> {

    @Matcher(isNull = "false")
    @Matcher(range = "[0,)")
    private Integer current;
    @Matcher(isNull = "false")
    @Matcher(range = "[0,)")
    private Integer size;
    private T param;

    public PageReq(){}

    public PageReq(Integer current, Integer size) {
        this.current = current;
        this.size = size;
    }

    public Integer getStartIndex() {
        return current > 1 ? (current - 1) * size : 0;
    }
}
