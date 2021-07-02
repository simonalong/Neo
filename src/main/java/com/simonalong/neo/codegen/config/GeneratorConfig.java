package com.simonalong.neo.codegen.config;

import com.simonalong.neo.NeoMap;
import lombok.Data;

/**
 * @author shizi
 * @since 2020/5/15 2:09 PM
 */
@Data
public class GeneratorConfig {

    /**
     * 项目路径
     */
    private String projectPath;
    /**
     * 表名前缀
     */
    private String preFix = "";
    /**
     * 属性名字的字符转换，默认为不转换
     */
    private NeoMap.NamingChg fieldNamingChg = NeoMap.NamingChg.UNDERLINE;
    /**
     * entity实体的包路径
     */
    private String entityPath;
    /**
     * 实体名字的后缀，默认为DO
     */
    private String entityPostFix = "DO";
}
