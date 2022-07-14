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

 Date: 18/04/2022 00:34:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for return_product_record
-- ----------------------------
DROP TABLE IF EXISTS `return_product_record`;
CREATE TABLE `return_product_record`  (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `number` int(11) NULL DEFAULT NULL COMMENT '回退库存的数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回退产品库存的记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of return_product_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
