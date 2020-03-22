package com.simonalong.neo.map.table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午5:12
 */
@Data
@Accessors(chain = true)
public class TableMapInnerEntity {

    private String name;
    private String dataAddressPath;
}
