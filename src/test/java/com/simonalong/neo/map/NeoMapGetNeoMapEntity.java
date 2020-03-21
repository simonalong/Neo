package com.simonalong.neo.map;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 上午12:31
 */
@Data
@Accessors(chain = true)
public class NeoMapGetNeoMapEntity {

    private String name;
    private String userName;
    private Integer age;
}
