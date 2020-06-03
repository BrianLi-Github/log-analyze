/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : log_analyze

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-06-03 09:52:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `log_analyze_job`
-- ----------------------------
DROP TABLE IF EXISTS `log_analyze_job`;
CREATE TABLE `log_analyze_job` (
  `jobId` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `jobName` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户自定义的名称，命名规则为：业务简称_指标简称',
  `jobType` int NOT NULL COMMENT '1:浏览日志、2:点击日志、3:搜索日志、4:购买日志',
  `businessId` int NOT NULL COMMENT '所属业务线',
  `status` int NOT NULL COMMENT '0:下线 、1:在线',
  `createUser` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '创建用户',
  `updateUser` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '修改用户',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '淇敼鏃堕棿',
  PRIMARY KEY (`jobId`,`jobName`),
  KEY `jobId` (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_analyze_job
-- ----------------------------
INSERT INTO `log_analyze_job` VALUES ('1', 'spade_p1002', '1', '1', '1', 'Brian', 'Brian', '2020-06-01 15:42:27', '2020-06-01 15:42:23');

-- ----------------------------
-- Table structure for `log_analyze_job_condition`
-- ----------------------------
DROP TABLE IF EXISTS `log_analyze_job_condition`;
CREATE TABLE `log_analyze_job_condition` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `jobId` int NOT NULL COMMENT '任务编号',
  `field` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用来比较的字段名称',
  `value` varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '参与比较的字段值',
  `compare` int NOT NULL COMMENT '1:包含 2:等于',
  `createUser` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '创建用户',
  `updateUser` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '修改用户',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_analyze_job_condition
-- ----------------------------
INSERT INTO `log_analyze_job_condition` VALUES ('1', '1', 'requestUrl', 'http://www.spade.cn/product?id=1002', '1', 'Brian', 'Brian', '2020-06-01 16:19:25', '2020-06-01 15:44:21');
INSERT INTO `log_analyze_job_condition` VALUES ('2', '1', 'referrerUrl', 'http://www.spade.cn/', '2', 'Brian', 'Brian', '2020-06-01 16:23:56', '2020-06-01 15:46:14');

-- ----------------------------
-- Table structure for `log_analyze_job_minute_append`
-- ----------------------------
DROP TABLE IF EXISTS `log_analyze_job_minute_append`;
CREATE TABLE `log_analyze_job_minute_append` (
  `indexName` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '指标名称',
  `pv` int DEFAULT NULL COMMENT 'pv的值',
  `uv` bigint DEFAULT NULL COMMENT 'uv的值',
  `executeTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '写入数据库的时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_analyze_job_minute_append
-- ----------------------------
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '1544', '4', '2020-06-02 22:51:09', '2020-06-02 22:51:09');
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '0', '0', '2020-06-02 22:52:07', '2020-06-02 22:52:07');
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '34', '0', '2020-06-02 22:53:07', '2020-06-02 22:53:07');
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '52', '2', '2020-06-02 22:54:07', '2020-06-02 22:54:07');
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '59', '0', '2020-06-02 22:55:07', '2020-06-02 22:55:07');
INSERT INTO `log_analyze_job_minute_append` VALUES ('spade_p1002', '60', '0', '2020-06-02 22:56:07', '2020-06-02 22:56:07');
