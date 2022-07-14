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

 Date: 18/04/2022 00:34:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL COMMENT '角色id',
  `role_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_desc` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(20) NULL DEFAULT NULL COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ADMIN', '管理员', '2022-03-26 05:46:47', '2022-03-26 05:46:47', NULL);
INSERT INTO `role` VALUES (2, 'USER', '角色', '2022-03-26 05:46:47', '2022-03-26 05:46:47', NULL);

SET FOREIGN_KEY_CHECKS = 1;
