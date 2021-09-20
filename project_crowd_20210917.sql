/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.23 : Database - project_crowd
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`project_crowd` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `project_crowd`;

/*Table structure for table `inner_admin_role` */

DROP TABLE IF EXISTS `inner_admin_role`;

CREATE TABLE `inner_admin_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `inner_admin_role` */

insert  into `inner_admin_role`(`id`,`admin_id`,`role_id`) values 
(3,1,1),
(4,1,2),
(5,1,3),
(6,1,4);

/*Table structure for table `inner_role_auth` */

DROP TABLE IF EXISTS `inner_role_auth`;

CREATE TABLE `inner_role_auth` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `auth_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `inner_role_auth` */

insert  into `inner_role_auth`(`id`,`role_id`,`auth_id`) values 
(1,1,1),
(2,1,8),
(3,2,4),
(4,2,5);

/*Table structure for table `t_address` */

DROP TABLE IF EXISTS `t_address`;

CREATE TABLE `t_address` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `receive_name` char(100) DEFAULT NULL COMMENT '收件人',
  `phone_num` char(100) DEFAULT NULL COMMENT '手机号',
  `address` char(200) DEFAULT NULL COMMENT '收货地址',
  `member_id` int DEFAULT NULL COMMENT '用户 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_address` */

insert  into `t_address`(`id`,`receive_name`,`phone_num`,`address`,`member_id`) values 
(1,'zouzhihui','963852741','大中华',2),
(2,'123','123123','123123123',1);

/*Table structure for table `t_admin` */

DROP TABLE IF EXISTS `t_admin`;

CREATE TABLE `t_admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login_acct` varchar(255) NOT NULL,
  `user_pswd` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `create_time` char(19) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_acct` (`login_acct`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_admin` */

insert  into `t_admin`(`id`,`login_acct`,`user_pswd`,`user_name`,`email`,`create_time`) values 
(1,'admin','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','admin','admin@qq.com',NULL),
(2,'peter','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','peter','peter@peter.com',NULL),
(3,'admin01','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','admin01','admin02@qq.com',NULL),
(4,'role','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','role','role@qq.com',NULL),
(5,'role01','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','role01','role01@qq.com',NULL);

/*Table structure for table `t_auth` */

DROP TABLE IF EXISTS `t_auth`;

CREATE TABLE `t_auth` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `t_auth` */

insert  into `t_auth`(`id`,`name`,`title`,`category_id`) values 
(1,'','用户模块',NULL),
(2,'user:delete','删除',1),
(3,'user:get','查询',1),
(4,'','角色模块',NULL),
(5,'role:delete','删除',4),
(6,'role:get','查询',4),
(7,'role:add','新增',4),
(8,'user_save','保存',1);

/*Table structure for table `t_member` */

DROP TABLE IF EXISTS `t_member`;

CREATE TABLE `t_member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `loginacct` varchar(255) NOT NULL,
  `userpswd` char(200) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `authstatus` int DEFAULT NULL COMMENT '实名认证状态 0 - 未实名认证， 1 - 实名认证申\r\n请中， 2 - 已实名认证',
  `usertype` int DEFAULT NULL COMMENT ' 0 - 个人， 1 - 企业',
  `realname` varchar(255) DEFAULT NULL,
  `cardnum` varchar(255) DEFAULT NULL,
  `accttype` int DEFAULT NULL COMMENT '0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_member` */

insert  into `t_member`(`id`,`loginacct`,`userpswd`,`username`,`email`,`authstatus`,`usertype`,`realname`,`cardnum`,`accttype`) values 
(1,'admin','$2a$10$xrOPsj7p0r0fdMAxqfitHOPSpX9jiWiB8mkj0pX.p080nopz1Rr1O','admin','admin@qq.com',NULL,NULL,NULL,NULL,NULL),
(2,'xiao','$2a$10$Lcu4vX9Nrm4SScSqUs1LD.H3HChK3rezvfuat1khfKpSkTojx/Vie','xiao','xiao',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_member_confirm_info` */

DROP TABLE IF EXISTS `t_member_confirm_info`;

CREATE TABLE `t_member_confirm_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `memberid` int DEFAULT NULL COMMENT '会员 id',
  `paynum` varchar(200) DEFAULT NULL COMMENT '易付宝企业账号',
  `cardnum` varchar(200) DEFAULT NULL COMMENT '法人身份证号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_member_confirm_info` */

insert  into `t_member_confirm_info`(`id`,`memberid`,`paynum`,`cardnum`) values 
(1,1,'11111111111111','123123123123'),
(2,2,'11111111111111','123123123123'),
(3,2,'11111111111111','123123123123'),
(4,2,'11111111111111','123123123123');

/*Table structure for table `t_member_launch_info` */

DROP TABLE IF EXISTS `t_member_launch_info`;

CREATE TABLE `t_member_launch_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `memberid` int DEFAULT NULL COMMENT '会员 id',
  `description_simple` varchar(255) DEFAULT NULL COMMENT '简单介绍',
  `description_detail` varchar(255) DEFAULT NULL COMMENT '详细介绍',
  `phone_num` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `service_num` varchar(255) DEFAULT NULL COMMENT '客服电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `t_member_launch_info` */

insert  into `t_member_launch_info`(`id`,`memberid`,`description_simple`,`description_detail`,`phone_num`,`service_num`) values 
(1,1,'i am mao','我是猫哥','123456','654321'),
(4,2,'i am mao','我是猫哥','123456','654321'),
(5,2,'i am mao','我是猫哥','123456','654321'),
(6,2,'i am mao','我是猫哥','123456','654321');

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `icon` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`pid`,`name`,`url`,`icon`) values 
(1,NULL,'系统权限菜单',NULL,'glyphicon\r\nglyphicon-th-list'),
(2,1,' 控 制 面 板 ','main.htm','glyphicon\r\nglyphicon-dashboard'),
(3,1,'权限管理',NULL,'glyphicon glyphicon\r\nglyphicon-tasks'),
(4,3,' 用 户 维 护 ','user/index.htm','glyphicon\r\nglyphicon-user'),
(5,3,' 角 色 维 护 ','role/index.htm','glyphicon\r\nglyphicon-king'),
(6,3,' 菜 单 维 护 ','permission/index.htm','glyphicon\r\nglyphicon-lock'),
(7,1,' 业 务 审 核 ',NULL,'glyphicon\r\nglyphicon-ok'),
(8,7,' 实 名 认 证 审 核 ','auth_cert/index.htm','glyphicon\r\nglyphicon-check'),
(9,7,' 广 告 审 核 ','auth_adv/index.htm','glyphicon\r\nglyphicon-check'),
(10,7,' 项 目 审 核 ','auth_project/index.htm','glyphicon\r\nglyphicon-check'),
(11,1,' 业 务 管 理 ',NULL,'glyphicon\r\nglyphicon-th-large'),
(12,11,' 资 质 维 护 ','cert/index.htm','glyphicon\r\nglyphicon-picture'),
(13,11,' 分 类 管 理 ','certtype/index.htm','glyphicon\r\nglyphicon-equalizer'),
(14,11,' 流 程 管 理 ','process/index.htm','glyphicon\r\nglyphicon-random'),
(15,11,' 广 告 管 理 ','advert/index.htm','glyphicon\r\nglyphicon-hdd'),
(16,11,' 消 息 模 板 ','message/index.htm','glyphicon\r\nglyphicon-comment'),
(17,11,' 项 目 分 类 ','projectType/index.htm','glyphicon\r\nglyphicon-list'),
(18,11,' 项 目 标 签 ','tag/index.htm','glyphicon\r\nglyphicon-tags'),
(19,1,' 参 数 管 理 ','param/index.htm','glyphicon\r\nglyphicon-list-alt');

/*Table structure for table `t_order` */

DROP TABLE IF EXISTS `t_order`;

CREATE TABLE `t_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_num` char(100) DEFAULT NULL COMMENT '订单号',
  `pay_order_num` char(100) DEFAULT NULL COMMENT '支付宝流水号',
  `order_amount` double(10,5) DEFAULT NULL COMMENT '订单金额',
  `invoice` int DEFAULT NULL COMMENT '是否开发票（0 不开， 1 开） ',
  `invoice_title` char(100) DEFAULT NULL COMMENT '发票抬头',
  `order_remark` char(100) DEFAULT NULL COMMENT '订单备注',
  `address_id` char(100) DEFAULT NULL COMMENT '收货地址 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_order` */

insert  into `t_order`(`id`,`order_num`,`pay_order_num`,`order_amount`,`invoice`,`invoice_title`,`order_remark`,`address_id`) values 
(1,'202109161513471FB5581C74634B22A7F380C1A28BBF1A','2021091622001417480501967168',50.00000,0,'','egewgwgwgw',NULL),
(2,'202109161522121C55B29A90C64CC789CB1E667324C077','2021091622001417480501967488',50.00000,1,'','以身相许',NULL),
(3,'20210916155614BD56A9F3FCFC4A9CB9B3018277E21E58','2021091622001417480501968162',50.00000,1,'','qfqewfqefqewfeq','2'),
(4,'20210916155906478D3631844F48DC88F87B4DA57D47A7','2021091622001417480501967170',50.00000,1,'个人人人人人','个人人人人人','2');

/*Table structure for table `t_order_project` */

DROP TABLE IF EXISTS `t_order_project`;

CREATE TABLE `t_order_project` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_name` char(200) DEFAULT NULL COMMENT '项目名称',
  `launch_name` char(100) DEFAULT NULL COMMENT '发起人',
  `return_content` char(200) DEFAULT NULL COMMENT '回报内容',
  `return_count` int DEFAULT NULL COMMENT '回报数量',
  `support_price` int DEFAULT NULL COMMENT '支持单价',
  `freight` int DEFAULT NULL COMMENT '配送费用',
  `order_id` int DEFAULT NULL COMMENT '订单表的主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_order_project` */

insert  into `t_order_project`(`id`,`project_name`,`launch_name`,`return_content`,`return_count`,`support_price`,`freight`,`order_id`) values 
(1,'brotherMao','i am mao','以身相许',5,10,0,1),
(2,'brotherMao','i am mao','以身相许',5,10,0,2),
(3,'brotherMao','i am mao','以身相许 mybatis mybatis',5,10,0,3),
(4,'brotherMao','i am mao','以身相许',5,10,0,4);

/*Table structure for table `t_project` */

DROP TABLE IF EXISTS `t_project`;

CREATE TABLE `t_project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `project_description` varchar(255) DEFAULT NULL COMMENT '项目描述',
  `money` bigint DEFAULT NULL COMMENT '筹集金额',
  `day` int DEFAULT NULL COMMENT '筹集天数',
  `status` int DEFAULT NULL COMMENT '0-即将开始， 1-众筹中， 2-众筹成功， 3-众筹失败',
  `deploydate` varchar(10) DEFAULT NULL COMMENT '项目发起时间',
  `supportmoney` bigint DEFAULT NULL COMMENT '已筹集到的金额',
  `supporter` int DEFAULT NULL COMMENT '支持人数',
  `completion` int DEFAULT NULL COMMENT '百分比完成度',
  `memberid` int DEFAULT NULL COMMENT '发起人的会员 id',
  `createdate` varchar(19) DEFAULT NULL COMMENT '项目创建时间',
  `follower` int DEFAULT NULL COMMENT '关注人数',
  `header_picture_path` varchar(255) DEFAULT NULL COMMENT '头图路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `t_project` */

insert  into `t_project`(`id`,`project_name`,`project_description`,`money`,`day`,`status`,`deploydate`,`supportmoney`,`supporter`,`completion`,`memberid`,`createdate`,`follower`,`header_picture_path`) values 
(1,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(2,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(3,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(4,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(5,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(6,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(7,'brotherMao','就是帅！',100000,30,0,NULL,123456,123,NULL,1,'2021-09-16',123,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/1dcdfed3a0754d4ba73939552f38b3c0.png'),
(11,'brotherMao','就是帅！',100000,30,0,NULL,NULL,NULL,NULL,2,'2021-09-16',NULL,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/8fea16bc2b3a42d2afb835e59213ddc1.png'),
(12,'brotherMao','就是帅！mybatis mybatis mybatis',100000,30,0,NULL,NULL,NULL,NULL,2,'2021-09-16',NULL,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/f7a6f51968ab497bb5732b821ac681f6.png'),
(13,'brotherMao','就是帅！',100000,30,0,NULL,NULL,NULL,NULL,2,'2021-09-16',NULL,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/0ed3799bbd8e40ae97152fb2d998180e.png');

/*Table structure for table `t_project_item_pic` */

DROP TABLE IF EXISTS `t_project_item_pic`;

CREATE TABLE `t_project_item_pic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `projectid` int DEFAULT NULL,
  `item_pic_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `t_project_item_pic` */

insert  into `t_project_item_pic`(`id`,`projectid`,`item_pic_path`) values 
(1,1,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/b39a884819f544708dba2c262493ff80.png'),
(4,11,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/8f88a1ba8e184e1299700bd3fa324fe4.png'),
(5,12,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/fb871534e5c045a5854775d4f7befc24.png'),
(6,13,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/3f06973e324547128ab5084451eefd52.png');

/*Table structure for table `t_project_tag` */

DROP TABLE IF EXISTS `t_project_tag`;

CREATE TABLE `t_project_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `projectid` int DEFAULT NULL,
  `tagid` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Data for the table `t_project_tag` */

insert  into `t_project_tag`(`id`,`projectid`,`tagid`) values 
(1,1,4),
(2,1,7),
(3,1,10),
(4,1,6),
(13,11,4),
(14,11,7),
(15,11,9),
(16,12,4),
(17,12,7),
(18,12,9),
(19,13,4),
(20,13,7),
(21,13,10);

/*Table structure for table `t_project_type` */

DROP TABLE IF EXISTS `t_project_type`;

CREATE TABLE `t_project_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `projectid` int DEFAULT NULL,
  `typeid` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Data for the table `t_project_type` */

insert  into `t_project_type`(`id`,`projectid`,`typeid`) values 
(1,1,1),
(2,1,2),
(3,1,3),
(4,1,4),
(17,11,1),
(18,11,2),
(19,11,3),
(20,11,4),
(21,12,1),
(22,12,2),
(23,12,3),
(24,12,4),
(25,13,1),
(26,13,2),
(27,13,3),
(28,13,4);

/*Table structure for table `t_return` */

DROP TABLE IF EXISTS `t_return`;

CREATE TABLE `t_return` (
  `id` int NOT NULL AUTO_INCREMENT,
  `projectid` int DEFAULT NULL,
  `type` int DEFAULT NULL COMMENT '0 - 实物回报， 1 虚拟物品回报',
  `supportmoney` int DEFAULT NULL COMMENT '支持金额',
  `content` varchar(255) DEFAULT NULL COMMENT '回报内容',
  `count` int DEFAULT NULL COMMENT '回报产品限额， “0” 为不限回报数量',
  `signalpurchase` int DEFAULT NULL COMMENT '是否设置单笔限购',
  `purchase` int DEFAULT NULL COMMENT '具体限购数量',
  `freight` int DEFAULT NULL COMMENT '运费， “0” 为包邮',
  `invoice` int DEFAULT NULL COMMENT '0 - 不开发票， 1 - 开发票',
  `returndate` int DEFAULT NULL COMMENT '项目结束后多少天向支持者发送回报',
  `describ_pic_path` varchar(255) DEFAULT NULL COMMENT '说明图片路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_return` */

insert  into `t_return`(`id`,`projectid`,`type`,`supportmoney`,`content`,`count`,`signalpurchase`,`purchase`,`freight`,`invoice`,`returndate`,`describ_pic_path`) values 
(1,1,0,10,'以身相许',5,0,8,0,0,15,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/3dbe219d5aed493b86caab85284d5f7f.png'),
(2,11,0,10,'以身相许',5,0,8,0,0,15,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/730ffa496ac646268c2ecba6e41f9579.png'),
(3,12,0,10,'以身相许 mybatis mybatis',5,0,8,0,0,15,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/331695da5842401a891e77db05d22de2.png'),
(4,13,0,10,'以身相许',5,0,8,0,0,15,NULL),
(5,13,0,10,'以身相许',5,0,8,0,0,15,'http://atguigu190830.oss-cn-shenzhen.aliyuncs.com/20210916/0903198437df47e1823b25f0ce44f9f6.png');

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` char(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`name`) values 
(1,'经理'),
(2,'部长'),
(3,'经理操作者'),
(4,'部长操作者');

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_tag` */

/*Table structure for table `t_type` */

DROP TABLE IF EXISTS `t_type`;

CREATE TABLE `t_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '分类介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_type` */

insert  into `t_type`(`id`,`name`,`remark`) values 
(1,'科技','开启智慧未来'),
(2,'设计','创意改变生活'),
(3,'农业','网络天下肥美'),
(4,'公益','点点爱心汇集');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
