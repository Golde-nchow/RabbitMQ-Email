/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50627
 Source Host           : localhost:3306
 Source Schema         : rabbitmq-email

 Target Server Type    : MySQL
 Target Server Version : 50627
 File Encoding         : 65001

 Date: 16/06/2020 12:47:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for msg_log
-- ----------------------------
DROP TABLE IF EXISTS `msg_log`;
CREATE TABLE `msg_log`  (
  `msg_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息唯一标识',
  `msg_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `msg_exchange` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'exchange交换器',
  `msg_routingKey` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '路由键',
  `msg_status` int(11) DEFAULT 0 COMMENT '0投递中，1投递成功，2投递失败，3已消费',
  `msg_retryCount` int(11) NOT NULL DEFAULT 0 COMMENT '重试次数',
  `next_retry_time` datetime(0) DEFAULT NULL COMMENT '下次重试时间',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of msg_log
-- ----------------------------
INSERT INTO `msg_log` VALUES ('3c50200831a24377b535d6ae240cb636', '{\"msgId\":\"3c50200831a24377b535d6ae240cb636\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:33:16', '2020-06-16 12:32:16', '2020-06-16 12:32:17');
INSERT INTO `msg_log` VALUES ('4c5190400ab8456ab1c4e82b84f70df7', '{\"msgId\":\"4c5190400ab8456ab1c4e82b84f70df7\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:33:17', '2020-06-16 12:32:17', '2020-06-16 12:32:18');
INSERT INTO `msg_log` VALUES ('9040da16a7e84709a4504be7c8917e28', '{\"msgId\":\"9040da16a7e84709a4504be7c8917e28\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:33:15', '2020-06-16 12:32:15', '2020-06-16 12:32:16');
INSERT INTO `msg_log` VALUES ('c5765c3347bf4edc8a165446459917db', '{\"msgId\":\"c5765c3347bf4edc8a165446459917db\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:40:03', '2020-06-16 12:39:03', '2020-06-16 12:39:05');
INSERT INTO `msg_log` VALUES ('e7422112e1b24a5889b839408f59d8f4', '{\"msgId\":\"e7422112e1b24a5889b839408f59d8f4\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:33:16', '2020-06-16 12:32:16', '2020-06-16 12:32:16');
INSERT INTO `msg_log` VALUES ('f2dd3ecbfc134ec89cba9b25b0fdac8f', '{\"msgId\":\"f2dd3ecbfc134ec89cba9b25b0fdac8f\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 1, 0, '2020-06-11 13:16:27', '2020-06-11 13:15:27', '2020-06-11 13:15:27');
INSERT INTO `msg_log` VALUES ('f9bfdbea97af450691fdb26f324ea0a2', '{\"msgId\":\"f9bfdbea97af450691fdb26f324ea0a2\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:30:15', '2020-06-16 12:29:15', '2020-06-16 12:29:16');
INSERT INTO `msg_log` VALUES ('fce58b67613d471d81162c9e1f5fa344', '{\"msgId\":\"fce58b67613d471d81162c9e1f5fa344\",\"title\":\"测试邮件\",\"content\":\"rabbitMQ测试\",\"to\":\"kamchou@foxmail.com\"}', 'mail.exchange', 'mail.routing.key', 3, 0, '2020-06-16 12:29:32', '2020-06-16 12:28:32', '2020-06-16 12:28:37');

SET FOREIGN_KEY_CHECKS = 1;
