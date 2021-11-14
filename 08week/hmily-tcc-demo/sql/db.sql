-- 创建bank1数据库和account_info表
CREATE DATABASE IF NOT EXISTS `bank1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bank1`;

CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `account_balance` double DEFAULT NULL COMMENT '帐户余额',
  `frozen_balance` double DEFAULT NULL COMMENT '冻结金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 准备数据
insert into account_info values (1,'zs',10000,0);

CREATE DATABASE IF NOT EXISTS `bank2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bank2`;

CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `account_balance` double DEFAULT NULL COMMENT '帐户余额',
  `frozen_balance` double DEFAULT NULL COMMENT '冻结金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;


insert into account_info values (1,'ls',10000,0);
