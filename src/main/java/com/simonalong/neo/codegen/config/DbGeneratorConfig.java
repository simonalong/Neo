package com.simonalong.neo.codegen.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shizi
 * @since 2020/5/15 2:17 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DbGeneratorConfig extends GeneratorConfig {

    /**
     * url
     */
    private String url;
    /**
     * 库用户名
     */
    private String userName;
    /**
     * 库的用户密码
     */
    private String password;

    /**
     * 待生成的表
     */
    private List<String> includeTables = new ArrayList<>();
    /**
     * 不需要生成的表
     */
    private List<String> excludeTables = new ArrayList<>();

    /**
     * 设置那些只输出的表
     * @param tables 数据库中的表名
     */
    public void setIncludes(String... tables) {
        includeTables.addAll(Arrays.asList(tables));
    }

    /**
     * 设置排除的表
     *
     * @param tables 数据库中的表名
     */
    public void setExcludes(String... tables) {
        excludeTables.addAll(Arrays.asList(tables));
    }
}
