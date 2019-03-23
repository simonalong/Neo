package com.simon.neo.codegen;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/23 下午11:06
 */
@Data
@Accessors(chain = true)
public class FieldInfo {

    private String fieldType;
    private String fieldName;
}
