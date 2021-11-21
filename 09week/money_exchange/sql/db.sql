
-- 创建A库，账户表和资产冻结表
CREATE DATABASE IF NOT EXISTS `bankA` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bankA`;

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `account_no` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '账户号',
  `balance` double NOT NULL COMMENT '用户余额',
  `type` tinyint(4) NOT NULL COMMENT '账户类型',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='账户表';

CREATE TABLE `frozen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `in_account_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '冻结转入账户号',
  `out_account_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '冻结转出账户号',
  `balance` double NOT NULL COMMENT '冻结余额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='冻结资产表';

insert into account values (1,'ls','LS_RMB',100000,0,now(),now());
insert into account values (2,'ls','LS_MY',100000,1,now(),now());

-- 创建B库，账户表和资产冻结表
CREATE DATABASE IF NOT EXISTS `bankB` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bankB`;

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `account_no` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '账户号',
  `balance` double NOT NULL COMMENT '用户余额',
  `type` tinyint(4) NOT NULL COMMENT '账户类型',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='账户表';

CREATE TABLE `frozen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `in_account_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '冻结转入账户号',
  `out_account_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '冻结转出账户号',
  `balance` double NOT NULL COMMENT '冻结余额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='冻结资产表';

insert into account values (1,'zs','ZS_RMB',100000,0,now(),now());
insert into account values (2,'zs','ZS_MY',100000,1,now(),now());