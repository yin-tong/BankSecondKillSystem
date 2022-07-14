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

 Date: 18/04/2022 00:32:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bsks_product
-- ----------------------------
DROP TABLE IF EXISTS `bsks_product`;
CREATE TABLE `bsks_product`  (
  `id` bigint(20) NOT NULL COMMENT '产品id',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `price` decimal(14, 4) NULL DEFAULT NULL,
  `rate` decimal(14, 4) NULL DEFAULT NULL COMMENT '利率',
  `limited_quantity` int(11) NULL DEFAULT 1 COMMENT '限购数量',
  `quantity` bigint(20) NOT NULL COMMENT '产品数量',
  `describe1` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `kill_time` datetime NULL DEFAULT NULL COMMENT '秒杀时间',
  `status` int(11) NULL DEFAULT 1 COMMENT '产品对于用户是否可见  0 可见 1 不可见',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(20) NULL DEFAULT 1 COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bsks_product
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
