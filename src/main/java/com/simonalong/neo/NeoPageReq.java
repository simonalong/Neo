package com.simonalong.neo;

import lombok.Data;

/**
 * @author shizi
 * @since 2020/8/22 4:34 PM
 */
@Data
public class NeoPageReq<T> {

    private Integer pageNo;
    private Integer pageSize;
    private T param;

    public Integer getPageIndex() {
        return pageNo > 1 ? (pageNo - 1) * pageSize : 0;
    }
}

