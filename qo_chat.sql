/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26)
 Source Host           : localhost:3306
 Source Schema         : qo_chat

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26)
 File Encoding         : 65001

 Date: 13/06/2024 11:17:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend`  (
  `from_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '请求发送用户',
  `to_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '需要接受用户',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `is_agree` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '2' COMMENT '0同意 1不同意 2待同意',
  PRIMARY KEY (`from_user`, `to_user`) USING BTREE,
  INDEX `to_user`(`to_user`) USING BTREE,
  CONSTRAINT `friend_ibfk_1` FOREIGN KEY (`from_user`) REFERENCES `user` (`qo_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `friend_ibfk_2` FOREIGN KEY (`to_user`) REFERENCES `user` (`qo_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friend
-- ----------------------------
INSERT INTO `friend` VALUES ('admin', 'mihoyo', '2024-06-05 10:50:09', '0');
INSERT INTO `friend` VALUES ('admin', '原生启动', '2024-06-05 10:43:58', '0');
INSERT INTO `friend` VALUES ('qzy', 'admin', '2024-06-03 22:50:39', '0');

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `group_num` bigint(20) NOT NULL COMMENT '群号',
  `group_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '群主',
  `group_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '群公告',
  `group_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '群头像',
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '群昵称',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`group_num`) USING BTREE,
  INDEX `group_ibfk_1`(`group_user`) USING BTREE,
  CONSTRAINT `group_ibfk_1` FOREIGN KEY (`group_user`) REFERENCES `user` (`qo_num`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES (7555564820, 'admin', '', 'http://localhost:5173/src/assets/cache/ec8edc87-1049-4a51-9eb4-0597d0ec39e81717555539857.jpg', '爱玩星铁', '2024-06-05 10:46:05');
INSERT INTO `group` VALUES (7555601707, 'admin', '', 'http://localhost:5173/src/assets/cache/4c755352-950d-48de-9798-4e05bd5e1d001717555597787.jpg', 'test', '2024-06-05 10:46:42');

-- ----------------------------
-- Table structure for gu
-- ----------------------------
DROP TABLE IF EXISTS `gu`;
CREATE TABLE `gu`  (
  `group_num` bigint(20) NOT NULL COMMENT '群号',
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '群内用户',
  `group_agree` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '2' COMMENT '0通过 1拒绝 2待处理',
  `user_agree` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '2' COMMENT '0通过 1拒绝 2待处理',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`group_num`, `user`) USING BTREE,
  INDEX `user`(`user`) USING BTREE,
  CONSTRAINT `gu_ibfk_2` FOREIGN KEY (`user`) REFERENCES `user` (`qo_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gu
-- ----------------------------
INSERT INTO `gu` VALUES (7555564820, 'admin', '0', '0', '2024-06-05 10:46:05', '2024-06-05 10:46:05');
INSERT INTO `gu` VALUES (7555564820, 'mihoyo', '0', '0', '2024-06-05 10:52:00', '2024-06-05 10:52:00');
INSERT INTO `gu` VALUES (7555564820, 'qzy', '0', '0', '2024-06-05 11:02:18', '2024-06-05 11:02:19');
INSERT INTO `gu` VALUES (7555564820, '原生启动', '0', '0', '2024-06-05 10:47:19', '2024-06-05 10:47:19');
INSERT INTO `gu` VALUES (7555601707, 'admin', '0', '0', '2024-06-05 10:46:42', '2024-06-05 10:46:42');

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sender_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_group` int(1) NULL DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  `send` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1798255511806971907 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (1797902382582251522, 'admin', 'admin', 'qzy', 'qzy', 'text', 0, 'test', 1717484692, 1);
INSERT INTO `messages` VALUES (1797902497854308353, 'admin', 'admin', 'qzy', 'qzy', 'text', 0, 'hello test', 1717484692, 1);
INSERT INTO `messages` VALUES (1798191034436718594, 'admin', 'admin', 'qzy', 'qzy', 'text', 0, '消息测试', 1717484692, 1);
INSERT INTO `messages` VALUES (1798191050144387073, 'admin', 'admin', 'qzy', 'qzy', 'text', 0, '你好', 1717484692, 1);
INSERT INTO `messages` VALUES (1798191085552701441, 'admin', 'admin', 'qzy', 'qzy', 'text', 0, 'this a test', 1717484692, 1);
INSERT INTO `messages` VALUES (1798255344374550529, 'admin', 'admin', 'mihoyo', 'mihoyo', 'text', 0, 'this is a test', 1717484692, 0);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `record_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息id',
  `record_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消息发送人',
  `record_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容',
  `record_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消息类型',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES ('08eca68fb369408a97852553faf7f55e', 'admin', '欢迎加入爱玩星铁', 'text', '2024-06-05 10:46:05');
INSERT INTO `record` VALUES ('0ba7e809e84748c28bdfeb0b1fcc585f', 'qzy', 'ukh', 'text', '2024-06-13 11:11:01');
INSERT INTO `record` VALUES ('109dadd3cf7a4e2093bd95379ac47cd4', 'admin', 'test', 'text', '2024-06-13 09:24:53');
INSERT INTO `record` VALUES ('16710fd464ed433f818597031a697683', 'qzy', 'ddd', 'text', '2024-06-13 10:41:37');
INSERT INTO `record` VALUES ('20eb2651d93c422ab90f81c4e762feb4', 'qzy', 'hello🤣', 'text', '2024-06-05 10:42:12');
INSERT INTO `record` VALUES ('24af70d0f5f646f7bd4afa17c8490573', 'admin', '😀', 'text', '2024-06-13 09:47:04');
INSERT INTO `record` VALUES ('255897420f9245f18db091f49daf8b99', 'admin', 'hhh', 'text', '2024-06-13 01:15:05');
INSERT INTO `record` VALUES ('31f0d37e40c34867bc7ea16785659778', 'qzy', 'pppp', 'text', '2024-06-13 10:47:35');
INSERT INTO `record` VALUES ('3826b4c0f25a4d7fa949dc03598a069d', 'admin', '🙃', 'text', '2024-06-13 10:19:35');
INSERT INTO `record` VALUES ('4027b96beedc4327939b98e8701e94de', 'qzy', 'uuuu', 'text', '2024-06-13 09:45:37');
INSERT INTO `record` VALUES ('43c201a702c54341813131b27243ec46', 'admin', 'aAA', 'text', '2024-06-13 10:21:49');
INSERT INTO `record` VALUES ('440cf6130f154b0793af18922b6824ad', 'admin', '欢迎加入test', 'text', '2024-06-05 10:46:42');
INSERT INTO `record` VALUES ('443198eef664450fac0c3300f33e7d4c', 'qzy', '😍', 'text', '2024-06-13 09:54:46');
INSERT INTO `record` VALUES ('49195f2168f840afb9e4d2cab2afce44', 'qzy', '😍😍😍', 'text', '2024-06-05 11:02:35');
INSERT INTO `record` VALUES ('4ba834bd29ac47c78db2e9577559bc55', 'admin', 'hello', 'text', '2024-06-13 10:08:35');
INSERT INTO `record` VALUES ('65e55b98f3fe49be847c62d5b077f094', 'qzy', 'aaa', 'text', '2024-06-13 11:10:17');
INSERT INTO `record` VALUES ('6a8104591b1c4b01b4860d8bcd2f3f54', 'admin', 'hello', 'text', '2024-06-03 23:24:00');
INSERT INTO `record` VALUES ('6f2fbf3bafff4a5380c8c9ef37f99bef', 'admin', 'hihi', 'text', '2024-06-13 10:07:14');
INSERT INTO `record` VALUES ('71642d4bcdbf489fb515ee82ef232c00', 'qzy', 'hhh', 'text', '2024-06-13 10:44:35');
INSERT INTO `record` VALUES ('716c1560fad84cc5ab3ae5fee9a24fa5', 'admin', 'aaa', 'text', '2024-06-13 08:52:06');
INSERT INTO `record` VALUES ('789d86b2a0d44aaa83d11411c933dd74', 'admin', '🤣', 'text', '2024-06-13 10:17:26');
INSERT INTO `record` VALUES ('810b2779e89a4fbfa6a18acb692ea95d', 'qzy', '😅', 'text', '2024-06-13 09:53:40');
INSERT INTO `record` VALUES ('82ace4c2fa6d486ba45cf2d07d8dd4cf', 'admin', '😀，你好呀！', 'text', '2024-06-04 23:18:19');
INSERT INTO `record` VALUES ('87443960e07c4caf9da1c3e5a42acc8a', 'admin', 'uuu', 'text', '2024-06-13 11:09:52');
INSERT INTO `record` VALUES ('9deac2795009496999e946aacfd90bef', 'admin', 'tttt', 'text', '2024-06-13 10:02:10');
INSERT INTO `record` VALUES ('a79d20fc0db147aea6b9f1687ec11146', 'qzy', 'hello', 'text', '2024-06-13 10:06:49');
INSERT INTO `record` VALUES ('b036512146694707a850bc5ca1ac2432', 'admin', '😀', 'text', '2024-06-13 01:17:05');
INSERT INTO `record` VALUES ('b32c76ee13114738953d892da45c5da9', 'admin', 'yyjccc', 'text', '2024-06-13 09:58:23');
INSERT INTO `record` VALUES ('c290bf8195a249a8ad12447cf5eb51d6', 'admin', 'hhh', 'text', '2024-06-13 01:15:13');
INSERT INTO `record` VALUES ('c75b6bd58d9346edb9b7c04667539fa6', 'qzy', 'uuuu', 'text', '2024-06-13 09:44:23');
INSERT INTO `record` VALUES ('c983a5b2624f42a6a50d2423c7fe5ff3', 'admin', 'aaa', 'text', '2024-06-13 10:20:34');
INSERT INTO `record` VALUES ('d71a211bf20c4a81ad2802df667f6737', 'admin', 'hkhk', 'text', '2024-06-13 11:10:31');
INSERT INTO `record` VALUES ('d7b919ec9d7d47d7a76b9d38d54120a0', 'mihoyo', '😜😍', 'text', '2024-06-05 10:52:12');
INSERT INTO `record` VALUES ('e6b8ec50574d4de5a8f5aa853539c9f7', 'admin', 'hello everyone😀', 'text', '2024-06-05 10:51:44');
INSERT INTO `record` VALUES ('ea11a7f203f84efb844a83f4a0e98874', 'admin', '😀，你好呀！', 'text', '2024-06-03 23:23:30');
INSERT INTO `record` VALUES ('ec491bd8b0f2460ea27179bf773d71d5', 'qzy', 'http://localhost:5173/src/assets/cache/17c6b12c-c421-42ba-aaf0-56cb670383151717555327569.jpg', 'image', '2024-06-05 10:42:08');
INSERT INTO `record` VALUES ('f1d8f3a04222415fb6ad5bfbaa140cf0', 'admin', '😀，你好呀！', 'text', '2024-06-13 10:47:55');

-- ----------------------------
-- Table structure for rg
-- ----------------------------
DROP TABLE IF EXISTS `rg`;
CREATE TABLE `rg`  (
  `r_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '记录id',
  `g_num` bigint(20) NOT NULL COMMENT '群号',
  PRIMARY KEY (`r_id`, `g_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rg
-- ----------------------------
INSERT INTO `rg` VALUES ('08eca68fb369408a97852553faf7f55e', 7555564820);
INSERT INTO `rg` VALUES ('16710fd464ed433f818597031a697683', 7555564820);
INSERT INTO `rg` VALUES ('255897420f9245f18db091f49daf8b99', 7555601707);
INSERT INTO `rg` VALUES ('31f0d37e40c34867bc7ea16785659778', 7555564820);
INSERT INTO `rg` VALUES ('43c201a702c54341813131b27243ec46', 7555564820);
INSERT INTO `rg` VALUES ('440cf6130f154b0793af18922b6824ad', 7555601707);
INSERT INTO `rg` VALUES ('49195f2168f840afb9e4d2cab2afce44', 7555564820);
INSERT INTO `rg` VALUES ('65e55b98f3fe49be847c62d5b077f094', 7555564820);
INSERT INTO `rg` VALUES ('71642d4bcdbf489fb515ee82ef232c00', 7555564820);
INSERT INTO `rg` VALUES ('87443960e07c4caf9da1c3e5a42acc8a', 7555601707);
INSERT INTO `rg` VALUES ('b036512146694707a850bc5ca1ac2432', 7555601707);
INSERT INTO `rg` VALUES ('c290bf8195a249a8ad12447cf5eb51d6', 7555601707);
INSERT INTO `rg` VALUES ('d71a211bf20c4a81ad2802df667f6737', 7555564820);
INSERT INTO `rg` VALUES ('d7b919ec9d7d47d7a76b9d38d54120a0', 7555564820);
INSERT INTO `rg` VALUES ('e6b8ec50574d4de5a8f5aa853539c9f7', 7555564820);

-- ----------------------------
-- Table structure for rt
-- ----------------------------
DROP TABLE IF EXISTS `rt`;
CREATE TABLE `rt`  (
  `r_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '记录id',
  `t_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '聊天id',
  PRIMARY KEY (`r_id`, `t_id`) USING BTREE,
  INDEX `t_id`(`t_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rt
-- ----------------------------
INSERT INTO `rt` VALUES ('0ba7e809e84748c28bdfeb0b1fcc585f', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('109dadd3cf7a4e2093bd95379ac47cd4', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('20eb2651d93c422ab90f81c4e762feb4', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('24af70d0f5f646f7bd4afa17c8490573', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('3826b4c0f25a4d7fa949dc03598a069d', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('4027b96beedc4327939b98e8701e94de', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('443198eef664450fac0c3300f33e7d4c', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('4ba834bd29ac47c78db2e9577559bc55', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('6a8104591b1c4b01b4860d8bcd2f3f54', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('6f2fbf3bafff4a5380c8c9ef37f99bef', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('716c1560fad84cc5ab3ae5fee9a24fa5', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('789d86b2a0d44aaa83d11411c933dd74', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('82ace4c2fa6d486ba45cf2d07d8dd4cf', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('9deac2795009496999e946aacfd90bef', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('a79d20fc0db147aea6b9f1687ec11146', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('b32c76ee13114738953d892da45c5da9', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('c983a5b2624f42a6a50d2423c7fe5ff3', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('ea11a7f203f84efb844a83f4a0e98874', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('ec491bd8b0f2460ea27179bf773d71d5', '0200105f45014289967b87e68bd67fd3');
INSERT INTO `rt` VALUES ('f1d8f3a04222415fb6ad5bfbaa140cf0', '5bd86694b5214c7daa8c0d1daff4e6dd');
INSERT INTO `rt` VALUES ('810b2779e89a4fbfa6a18acb692ea95d', 'ff52bbec3d354031a1ccf747a2b5ddb9');
INSERT INTO `rt` VALUES ('c75b6bd58d9346edb9b7c04667539fa6', 'ff52bbec3d354031a1ccf747a2b5ddb9');

-- ----------------------------
-- Table structure for talk
-- ----------------------------
DROP TABLE IF EXISTS `talk`;
CREATE TABLE `talk`  (
  `talk_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `talk_user1` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户1',
  `talk_user2` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户2',
  PRIMARY KEY (`talk_id`) USING BTREE,
  INDEX `talk_user1`(`talk_user1`) USING BTREE,
  INDEX `talk_user2`(`talk_user2`) USING BTREE,
  CONSTRAINT `talk_ibfk_1` FOREIGN KEY (`talk_user1`) REFERENCES `user` (`qo_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `talk_ibfk_2` FOREIGN KEY (`talk_user2`) REFERENCES `user` (`qo_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of talk
-- ----------------------------
INSERT INTO `talk` VALUES ('0200105f45014289967b87e68bd67fd3', 'admin', 'qzy');
INSERT INTO `talk` VALUES ('5bd86694b5214c7daa8c0d1daff4e6dd', 'admin', 'mihoyo');
INSERT INTO `talk` VALUES ('ff52bbec3d354031a1ccf747a2b5ddb9', 'qzy', 'qzy');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `qo_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '加密密码',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '昵称',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '个性签名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `del_logic` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`qo_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('admin', 'http://localhost:5173/src/assets/cachea19e8316-6f55-4582-b086-667fce33b81a1717427306358.jpg', '21232f297a57a5a743894a0e4a801fc3', 'admin', '', NULL, '0', '', '2024-06-03 21:25:48', '2024-06-03 23:08:30', '0');
INSERT INTO `user` VALUES ('mihoyo', 'http://localhost:5173/src/assets/cache/2656e1c0-d4ff-4a00-9205-fd20321a01c81717556115010.png', 'c52f8041a248570787576dd076d11e86', 'mihoyo', '', '', '1', '', '2024-06-05 10:48:41', '2024-06-05 10:55:17', '0');
INSERT INTO `user` VALUES ('qzy', 'http://localhost:5173/src//assets//cache//01ac3eb8-dc37-409c-b82d-c710d0359f351717425829192.png', 'e10adc3949ba59abbe56e057f20f883e', 'qzy', '', '', '0', '', '2024-06-03 22:32:02', '2024-06-03 22:44:31', '0');
INSERT INTO `user` VALUES ('yyjccc', NULL, 'a4fafb01243b79527d4d1bf9aab3d54c', 'yyjccc', NULL, '', NULL, NULL, '2024-06-03 22:29:33', '2024-06-03 22:31:01', '0');
INSERT INTO `user` VALUES ('原生启动', 'http://localhost:5173/src/assets/cache/7f507d1e-0e8a-47ac-825c-6e623439df731717555481311.jpg', '275fc0de05dd8c6976ac19b6ee1849dd', '原生启动', '', '', '0', '', '2024-06-05 10:30:07', '2024-06-05 10:44:43', '0');

SET FOREIGN_KEY_CHECKS = 1;
