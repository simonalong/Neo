package com.simonalong.neo;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CREATE TABLE `neo_table1` (
 *   `id` int unsigned NOT NULL AUTO_INCREMENT,
 *   `group` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
 *   `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
 *   `user_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
 *   `age` int DEFAULT NULL,
 *   `sl` bigint DEFAULT NULL,
 *   `desc` mediumtext COLLATE utf8_unicode_ci COMMENT '描述',
 *   `desc1` text COLLATE utf8_unicode_ci COMMENT '描述',
 *   PRIMARY KEY (`id`),
 *   KEY `group_index` (`group`),
 *   KEY `k_group` (`group`),
 *   FULLTEXT KEY `fk_desc` (`desc`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 *
 * @author zhouzhenyong
 * @since 2019/3/15 下午6:34
 */
public class NeoBaseTest extends BaseTest {

    protected static final String URL = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true";
    protected static final String USER = "neo_test";
    protected static final String PASSWORD = "neo@Test123";

//    protected static final String URL = "jdbc:mariadb://127.0.0.1:23316/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true";
//    protected static final String USER = "root";
//    protected static final String PASSWORD = "root";

    public static final String TABLE_NAME = "neo_table1";

    public static Neo neo;

    static {
        System.setProperty("LOG_LEVEL", "debug");
    }

    @BeforeClass
    public static void start(){
        neo = Neo.connect(URL, USER, PASSWORD).initDb("neo", "xx", "uuid");
    }
}
