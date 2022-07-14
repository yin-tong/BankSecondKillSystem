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

 Date: 18/04/2022 00:33:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for repay_record
-- ----------------------------
DROP TABLE IF EXISTS `repay_record`;
CREATE TABLE `repay_record`  (
  `id` bigint(20) NOT NULL COMMENT '记录id',
  `identity_id` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `repay_amount` decimal(14, 4) NULL DEFAULT NULL COMMENT '还款金额',
  `repay_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '规定还款日期',
  `true_repay_time` datetime NULL DEFAULT NULL COMMENT '实际还款时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '还款记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repay_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
