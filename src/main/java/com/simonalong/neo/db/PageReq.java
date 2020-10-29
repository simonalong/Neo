package com.simonalong.neo.db;

import lombok.Data;

/**
 * @author shizi
 * @since 2020/8/31 4:57 下午
 */
@Data
public class PageReq<T> {

    private Integer pageNo;
    private Integer pageSize;
    private T param;

    public Integer getStartIndex() {
        return pageNo > 1 ? (pageNo - 1) * pageSize : 0;
    }
}
