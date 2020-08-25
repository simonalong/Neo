package com.simonalong.neo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shizi
 * @since 2020/8/18 3:44 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeoPageRsp<T> {

    /**
     * 分页数据
     */
    private List<T> dataList;

    /**
     * 总个数
     */
    private Integer totalNum;
}
