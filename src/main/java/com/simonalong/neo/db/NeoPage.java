package com.simonalong.neo.db;

/**
 * 该类弃用，已经转移到类{@link PageReq}
 *
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
@Deprecated
public final class NeoPage {

    private Integer pageNo;
    private Integer pageSize;

    private NeoPage() {}

    private NeoPage(PageReq<?> neoPageReq) {
        this.pageNo = neoPageReq.getPageNo();
        this.pageSize = neoPageReq.getPageSize();
    }

    /**
     * 创建
     *
     * @param pageNo   分页页码
     * @param pageSize 分页大小
     * @return 分页对象
     */
    public static NeoPage of(Integer pageNo, Integer pageSize) {
        return new NeoPage().setPageNo(pageNo).setPageSize(pageSize);
    }

    public static NeoPage from(PageReq<?> neoPageReq) {
        return new NeoPage().setPageNo(neoPageReq.getPageNo()).setPageSize(neoPageReq.getPageSize());
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
