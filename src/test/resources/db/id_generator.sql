CREATE TABLE `neo_id_generator` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 测试唯一性的表
CREATE TABLE `uuid_data` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`uuid` bigint(20) unsigned NOT NULL DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
