package com.simon.neo;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
public class NeoPage {

    private Integer pageIndex;
    private Integer pageSize;

    private NeoPage() {}

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

    public Integer startIndex() {
        if (pageIndex > 0) {
            return (pageIndex - 1) * pageSize;
        }
        return 0;
    }

    public Integer pageSize() {
        return pageSize;
    }
}
