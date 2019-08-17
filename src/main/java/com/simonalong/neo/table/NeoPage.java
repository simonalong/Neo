package com.simonalong.neo.table;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午3:31
 */
public final class NeoPage {

    private static final String PAGER = "pager";
    private static final String PAGE_NO = "pageNo";
    private static final String PAGE_SIZE = "pageSize";
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

    /**
     * 从NeoMap中获取分页数据
     * @param dataMap 数据map，其中包含key为'pager'，对应的值为NeoMap，里面包含'pageNo'和'pageSize'
     * @return 分页数据
     */
    public static NeoPage from(NeoMap dataMap){
        if (dataMap.containsKey(PAGER)){
            NeoMap pager = NeoMap.fromMap(dataMap.getNeoMap(PAGER), NamingChg.DEFAULT);
            return NeoPage.of(pager.getInteger(PAGE_NO), pager.getInteger(PAGE_SIZE));
        }
        // 不存在则返回默认的
        return NeoPage.of(1, 20);
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
