package com.simonalong.neo.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/31 4:57 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRsp<T> {

    /**
     * 分页数据
     */
    private List<T> dataList;

    /**
     * 总个数
     */
    private Integer totalNum;

    public <R> PageRsp<R> convert(Function<T, R> function) {
        PageRsp<R> pageRsp = new PageRsp<>();
        pageRsp.setDataList(dataList.stream().map(function).collect(Collectors.toList()));
        pageRsp.setTotalNum(totalNum);
        return pageRsp;
    }
}
