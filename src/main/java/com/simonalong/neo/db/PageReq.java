package com.simonalong.neo.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shizi
 * @since 2020/8/31 4:57 下午
 */
@Data
@AllArgsConstructor
public class PageReq<T> {

    private Integer pageNo;
    private Integer pageSize;
    private T param;

    public PageReq(){}

    public PageReq(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getStartIndex() {
        return pageNo > 1 ? (pageNo - 1) * pageSize : 0;
    }
}
