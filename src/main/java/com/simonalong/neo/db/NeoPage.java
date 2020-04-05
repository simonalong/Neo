package com.simonalong.neo.db;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
public final class NeoPage {

    private Integer pageIndex;
    private Integer pageSize;

    private NeoPage() {}

    /**
     * 创建
     * @param pageIndex 分页页码
     * @param pageSize 分页大小
     * @return 分页对象
     */
    public static NeoPage of(Integer pageIndex, Integer pageSize) {
        return new NeoPage().setPageIndex(pageIndex).setPageSize(pageSize);
    }

    public NeoPage setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public NeoPage setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getStartIndex() {
        if (pageIndex > 0) {
            return (pageIndex - 1) * pageSize;
        }
        return 0;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
