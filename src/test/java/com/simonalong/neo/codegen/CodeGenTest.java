package com.simonalong.neo.codegen;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.codegen.config.DbGeneratorConfig;
import com.simonalong.neo.codegen.config.SqlGeneratorConfig;
import com.simonalong.neo.codegen.generator.CreateSqlEntityCodeGenerator;
import com.simonalong.neo.codegen.generator.DbEntityCodeGenerator;

/**
 * @author zhouzhenyong
 * @since 2019/3/24 下午11:13
 */
public class CodeGenTest {

    /**
     * 基于db进行生成
     */
    //@Test
    public void test1() {
        DbEntityCodeGenerator codeGen = new DbEntityCodeGenerator();
        DbGeneratorConfig generatorConfig = new DbGeneratorConfig();
        generatorConfig.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        generatorConfig.setUserName("neo_test");
        generatorConfig.setPassword("neo@Test123");
        // 设置项目路径
        generatorConfig.setProjectPath("/Users/zhouzhenyong/project/private/Neo");
        // 设置实体位置
        generatorConfig.setEntityPath("com.simonalong.neo.entity");
        // 设置表前缀过滤
        // generatorConfig.setPreFix("t_");
        // 设置要排除的表
        // generatorConfig.setExcludes("xx_test");
        // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
        generatorConfig.setFieldNamingChg(NamingChg.UNDERLINE);
        // 设置要生成的表
        generatorConfig.setIncludes("neo_table4");

        codeGen.setConfig(generatorConfig);

        // 代码生成
        codeGen.generate();
    }

    /**
     * 基于建表语句进行生成
     */
    //@Test
    public void test2() {
        // 建表语句
        String createTableSql = "CREATE TABLE `neo_table4` (\n" + "  `id` int(11) unsigned NOT NULL,\n" + "  `group` char(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据来源组，外键关联lk_config_group',\n" + "  `name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '任务name',\n" + "  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',\n" + "  `age` int(11) DEFAULT NULL,\n" + "  `n_id` int(11) unsigned DEFAULT NULL,\n" + "  `sort` int(11) DEFAULT NULL,\n" + "  `enum1` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型：Y=成功；N=失败',\n" + "  `create_time` datetime DEFAULT NULL,\n" + "  `time` time DEFAULT NULL,\n" + "  `year` year(4) DEFAULT NULL,\n" + "  `date` date DEFAULT NULL,\n" + "  `datetime` datetime DEFAULT NULL,\n" + "  PRIMARY KEY (`id`),\n" + "  KEY `group_index` (`group`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci";

        CreateSqlEntityCodeGenerator codeGenerator = new CreateSqlEntityCodeGenerator();
        SqlGeneratorConfig generatorConfig = new SqlGeneratorConfig();
        generatorConfig.setCreateSql(createTableSql);
        generatorConfig.setProjectPath("/Users/zhouzhenyong/project/private/Neo");
        generatorConfig.setPreFix("neo_");
        generatorConfig.setEntityPath("com.simonalong.neo.entity");
        generatorConfig.setFieldNamingChg(NamingChg.UNDERLINE);
        codeGenerator.setConfig(generatorConfig);

        // 生成代码
        codeGenerator.generate();
    }

    /**
     * 适配postgresql的数据库建表
     */
    //@Test
    public void testPg(){
        DbEntityCodeGenerator codeGen = new DbEntityCodeGenerator();
        DbGeneratorConfig generatorConfig = new DbGeneratorConfig();
        generatorConfig.setUrl("jdbc:postgresql://localhost:54321/db");
        generatorConfig.setUserName("postgres");
        generatorConfig.setPassword("pg.123");
        // 设置项目路径
        generatorConfig.setProjectPath("/Users/zhouzhenyong/project/private/Heimdallr/neodb");
        // 设置实体位置
        generatorConfig.setEntityPath("com.simonalong.heimdallr.neodb");
        // 设置表前缀过滤
        // generatorConfig.setPreFix("t_");
        // 设置要排除的表
        // generatorConfig.setExcludes("xx_test");
        // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
        generatorConfig.setFieldNamingChg(NeoMap.NamingChg.UNDERLINE);
        // 设置要生成的表
        generatorConfig.setIncludes("demo_bytea");

        codeGen.setConfig(generatorConfig);

        // 代码生成
        codeGen.generate();
    }

    //@Test
    public void testPg2(){
        // 建表语句
        String createTableSql = "create table demo_bytea(bytea bytea);";

        CreateSqlEntityCodeGenerator codeGenerator = new CreateSqlEntityCodeGenerator();
        SqlGeneratorConfig generatorConfig = new SqlGeneratorConfig();
        generatorConfig.setCreateSql(createTableSql);
        generatorConfig.setProjectPath("/Users/zhouzhenyong/project/private/Heimdallr/neodb");
        generatorConfig.setPreFix("neo_");
        generatorConfig.setEntityPath("com.simonalong.heimdallr.neodb");
        generatorConfig.setFieldNamingChg(NamingChg.UNDERLINE);
        codeGenerator.setConfig(generatorConfig);

        // 生成代码
        codeGenerator.generate();
    }
}
