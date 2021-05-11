package com.simonalong.neo.db;

/**
 * 该类弃用，已经转移到类{@link PageReq}
 *
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
@Deprecated
public final class NeoPage {

    private Integer current;
    private Integer size;

    private NeoPage() {}

    private NeoPage(PageReq<?> neoPageReq) {
        this.current = neoPageReq.getPageNo();
        this.size = neoPageReq.getPageSize();
    }

    /**
     * 创建
     *
     * @param pageNo   分页页码
     * @param pageSize 分页大小
     * @return 分页对象
     */
    public static NeoPage of(Integer pageNo, Integer pageSize) {
        return new NeoPage().setCurrent(pageNo).setSize(pageSize);
    }

    public static NeoPage from(PageReq<?> neoPageReq) {
        return new NeoPage().setCurrent(neoPageReq.getPageNo()).setSize(neoPageReq.getPageSize());
    }

    public NeoPage setCurrent(Integer current) {
        this.current = current;
        return this;
    }

    public NeoPage setSize(Integer size) {
        this.size = size;
        return this;
    }

    public Integer getStartIndex() {
        if (current > 0) {
            return (current - 1) * size;
        }
        return 0;
    }

    public Integer getSize() {
        return size;
    }
}
