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

 Date: 18/04/2022 00:33:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for first_filter_record
-- ----------------------------
DROP TABLE IF EXISTS `first_filter_record`;
CREATE TABLE `first_filter_record`  (
  `id` bigint(20) NOT NULL COMMENT '记录id',
  `identity_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '身份证号码',
  `result` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '初筛结果 拒绝，通过',
  `reject_reason` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间/初筛时间',
  `version` bigint(20) NULL DEFAULT 1 COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `identity_id_UNIQUE`(`identity_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '初筛记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of first_filter_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
