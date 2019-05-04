CREATE TABLE `neo_id_generator` (
  `id` int(11) NOT NULL,
  `uuid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;