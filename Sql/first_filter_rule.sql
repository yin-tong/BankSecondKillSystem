/*
 Navicat Premium Data Transfer

 Source Server         : BankSecondKillSystem
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:33
 Source Schema         : sxfcds2103374

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 18/04/2022 00:33:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for first_filter_rule
-- ----------------------------
DROP TABLE IF EXISTS `first_filter_rule`;
CREATE TABLE `first_filter_rule`  (
  `id` int(11) NOT NULL,
  `limit_overdue_years` int(11) NULL DEFAULT NULL COMMENT '限制逾期年数',
  `limit_overdue_times` int(11) NULL DEFAULT NULL COMMENT '限制逾期次数',
  `limit_overdue_money` decimal(14, 4) NULL DEFAULT NULL COMMENT '限制逾期金额',
  `limit_payoff_days` int(11) NULL DEFAULT NULL COMMENT '限制逾期天数',
  `limit_age` int(11) NULL DEFAULT NULL COMMENT '限制年龄',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `version` bigint(20) NULL DEFAULT 1 COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '初筛规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of first_filter_rule
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
