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

    /**
     * 属性的类的类型
     */
    private String fieldType;
    /**
     * 属性的描述
     */
    private String fieldRemark;
    /**
     * 属性的名字
     */
    private String fieldName;
}
