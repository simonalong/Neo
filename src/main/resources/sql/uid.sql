create table `neo_id_generator` (
    `id` int(11) not null,
    `uuid` bigint(20) not null default 0,
    primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `neo_id_generator`(`id`, `uuid`) values(1, 0);
