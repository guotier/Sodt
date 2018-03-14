DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `id` varchar(36) NOT NULL DEFAULT '',
  `status` varchar(16) DEFAULT NULL,
  `bean_name` varchar(64) DEFAULT NULL,
  `method_name` varchar(128) DEFAULT NULL,
  `cancel_method_name` varchar(64) DEFAULT NULL,
  `confirm_method_name` varchar(64) DEFAULT NULL,
  `parameter_types` varbinary(256) DEFAULT NULL,
  `parameters` varbinary(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;