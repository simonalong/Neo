create database neo DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
create user 'neo_test'@'localhost' identified by 'neo@Test123';
grant all privileges on neo.* to 'neo_test'@'localhost';

CREATE TABLE `neo_table1` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `sl` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `neo_table2` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `neo_table3` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `n_id` int(11) unsigned NOT NULL,
  `sort` int(11) NOT NULL,
  `enum1` enum('A','B') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型：A=成功；B=失败',
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `neo_table4` (
  `id` int(11) unsigned NOT NULL,
  `group` char(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `age` int(11) DEFAULT NULL,
  `n_id` int(11) unsigned DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `enum1` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型：Y=成功；N=失败',
  `create_time` datetime DEFAULT NULL,
  `time` time DEFAULT NULL,
  `year` year(4) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index` (`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `xx_test5` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
  `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
  `gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女',
  `biginta` bigint(20) NOT NULL,
  `binarya` binary(1) NOT NULL,
  `bit2` bit(1) NOT NULL,
  `blob2` blob NOT NULL,
  `boolean2` tinyint(1) NOT NULL,
  `char1` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `datetime1` datetime NOT NULL,
  `date2` date NOT NULL,
  `decimal1` decimal(10,0) NOT NULL,
  `double1` double NOT NULL,
  `enum1` enum('a','b') COLLATE utf8_unicode_ci NOT NULL,
  `float1` float NOT NULL,
  `geometry` geometry NOT NULL,
  `int2` int(11) NOT NULL,
  `linestring` linestring NOT NULL,
  `longblob` longblob NOT NULL,
  `longtext` longtext COLLATE utf8_unicode_ci NOT NULL,
  `medinumblob` mediumblob NOT NULL,
  `medinumint` mediumint(9) NOT NULL,
  `mediumtext` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `multilinestring` multilinestring NOT NULL,
  `multipoint` multipoint NOT NULL,
  `mutipolygon` multipolygon NOT NULL,
  `point` point NOT NULL,
  `polygon` polygon NOT NULL,
  `smallint` smallint(6) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  `time` time NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tinyblob` tinyblob NOT NULL,
  `tinyint` tinyint(4) NOT NULL,
  `tinytext` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `text1` text COLLATE utf8_unicode_ci NOT NULL,
  `text1123` text COLLATE utf8_unicode_ci NOT NULL,
  `time1` time NOT NULL,
  `timestamp1` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `tinyblob1` tinyblob NOT NULL,
  `tinyint1` tinyint(4) NOT NULL,
  `tinytext1` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `year2` year(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='配置项';

CREATE PROCEDURE `pro`()
BEGIN
   explain select * from neo_table1;
  select * from neo_table1;
END;