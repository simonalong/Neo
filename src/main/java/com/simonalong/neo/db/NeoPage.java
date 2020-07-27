package com.simonalong.neo.db;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
public final class NeoPage {

    private Integer pageNo;
    private Integer pageSize;

    private NeoPage() {}

    /**
     * 创建
     * @param pageIndex 分页页码
     * @param pageSize 分页大小
     * @return 分页对象
     */
    public static NeoPage of(Integer pageIndex, Integer pageSize) {
        return new NeoPage().setPageNo(pageIndex).setPageSize(pageSize);
    }

    public NeoPage setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public NeoPage setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getStartIndex() {
        if (pageNo > 0) {
            return (pageNo - 1) * pageSize;
        }
        return 0;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
