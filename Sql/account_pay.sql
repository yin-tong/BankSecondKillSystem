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

 Date: 18/04/2022 00:31:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_pay
-- ----------------------------
DROP TABLE IF EXISTS `account_pay`;
CREATE TABLE `account_pay`  (
  `id` bigint(20) NOT NULL COMMENT '支付信息id',
  `account_id` bigint(20) NULL DEFAULT NULL COMMENT '对应的账户id',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `pay_money` decimal(14, 4) NULL DEFAULT 0.0000 COMMENT '账户支付总数额',
  `synchronous_money` decimal(20, 4) NULL DEFAULT NULL COMMENT '账户已同步金额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(20) NULL DEFAULT 1 COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_pay
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
