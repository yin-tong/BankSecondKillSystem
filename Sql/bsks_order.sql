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

 Date: 18/04/2022 00:31:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bsks_order
-- ----------------------------
DROP TABLE IF EXISTS `bsks_order`;
CREATE TABLE `bsks_order`  (
  `id` bigint(20) NOT NULL COMMENT '订单id',
  `account_id` bigint(20) NULL DEFAULT NULL COMMENT '账户id',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品id',
  `user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `number` int(11) NULL DEFAULT NULL COMMENT '购买数量',
  `product_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `limited_quantity` int(11) NULL DEFAULT NULL COMMENT '限购数量',
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单种类: 抢购成功，抢购失败',
  `pay_money` decimal(14, 4) NULL DEFAULT NULL COMMENT '支付金额',
  `order_time` datetime NULL DEFAULT NULL COMMENT '下单时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0 客户未删除 1 客户删除)',
  `version` bigint(20) NULL DEFAULT 1 COMMENT '乐观锁标识位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bsks_order
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
