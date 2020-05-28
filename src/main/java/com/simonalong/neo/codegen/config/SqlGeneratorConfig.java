package com.simonalong.neo.codegen.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/5/15 2:40 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlGeneratorConfig extends GeneratorConfig {

    private String createSql;
}

