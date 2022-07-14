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

 Date: 18/04/2022 00:33:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dishonest_person
-- ----------------------------
DROP TABLE IF EXISTS `dishonest_person`;
CREATE TABLE `dishonest_person`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `identity_id` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `execution_status` int(11) NULL DEFAULT NULL COMMENT '未执行完 0 执行完1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '失信人名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dishonest_person
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
