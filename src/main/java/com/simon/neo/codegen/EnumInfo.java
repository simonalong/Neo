package com.simon.neo.codegen;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/28 下午2:43
 */
@Data
@Accessors(chain = true)
public class EnumInfo {

    private String remark;
    private String enumTypeName;
    private List<InnerEnum> enumList;

    @Data
    private class InnerEnum{
        private String metaRemark;
        private String metaName;
    }
}
