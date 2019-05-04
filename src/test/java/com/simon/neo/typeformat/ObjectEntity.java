package com.simon.neo.typeformat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/5/4 下午10:39
 */
@Data
@Accessors(chain = true)
public class ObjectEntity {

    private String name;
    private Integer age;
}
