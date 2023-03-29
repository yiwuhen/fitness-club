-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.5.18-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 fitness_bms 的数据库结构
CREATE
DATABASE IF NOT EXISTS `fitness_bms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE
`fitness_bms`;

-- 导出  表 fitness_bms.bms_article 结构
DROP TABLE IF EXISTS bms_article;
CREATE TABLE IF NOT EXISTS `bms_article` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
    `category_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章类别id',
    `category_name` varchar(50) DEFAULT NULL COMMENT '文章类别名称',
    `title` varchar(255) DEFAULT NULL COMMENT '文章标题',
    `description` varchar(255) DEFAULT NULL COMMENT '文章简述',
    `content` text DEFAULT NULL COMMENT '正文',
    `view_count` bigint(20) unsigned DEFAULT NULL COMMENT '浏览量',
    `sort` tinyint(3) unsigned DEFAULT NULL COMMENT '排序序号',
    `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
    `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 数据导出被取消选择。

-- 导出  表 fitness_bms.bms_article_category 结构
DROP TABLE IF EXISTS bms_article_category;
CREATE TABLE IF NOT EXISTS `bms_article_category`
(
    `id` bigint
(
    20
) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
    `name` varchar
(
    50
) DEFAULT NULL COMMENT '类别名称',
    `parent_id` bigint
(
    20
) unsigned DEFAULT NULL COMMENT '父级类别id，如果无父级，则为0',
    `depth` tinyint
(
    3
) unsigned DEFAULT 1 COMMENT '深度，最顶级类别的深度为1，次级为2，以此类推',
    `sort` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '排序序号',
    `enable` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '是否启用，1=启用，0=未启用',
    `is_parent` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '是否为父级（是否包含子级），1=是父级，0=不是父级',
    `is_display` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '是否显示在导航栏中，1=启用，0=未启用',
    `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
    `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
    PRIMARY KEY
(
    `id`
) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_general_ci;

-- 数据导出被取消选择。

-- 导出  表 fitness_bms.bms_article_picture 结构
DROP TABLE IF EXISTS bms_article_picture;
CREATE TABLE IF NOT EXISTS `bms_article_picture`
(
    `id` bigint
(
    20
) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
    `article_id` bigint
(
    20
) unsigned DEFAULT NULL COMMENT '文章id',
    `url` varchar
(
    255
) DEFAULT NULL COMMENT '图片url',
    `description` varchar
(
    255
) DEFAULT NULL COMMENT '图片简介',
    `width` smallint
(
    5
) unsigned DEFAULT NULL COMMENT '图片宽度，单位：px',
    `height` smallint
(
    5
) unsigned DEFAULT NULL COMMENT '图片高度，单位：px',
    `is_cover` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '是否为封面图片，1=是，0=否',
    `sort` tinyint
(
    3
) unsigned DEFAULT NULL COMMENT '排序序号',
    `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
    `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
    PRIMARY KEY
(
    `id`
) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_general_ci;

-- 数据导出被取消选择。

--     id  name pid dep sort enable is_parent is_display gmt_create gmt_modified
INSERT INTO bms_article_category
VALUES -- 首页5个板块
       (1, '首页', 0, 1, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (2, '肌肉', 0, 1, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (3, '器械', 0, 1, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (4, '饮食', 0, 1, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (5, '资讯', 0, 1, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       -- 肌肉子板块
       (6, '颈部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (7, '肩部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (8, '胸部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (9, '臂部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (10, '背部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (11, '腹部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (12, '腰部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (13, '臀部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (14, '腿部', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (15, '全身', 2, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       -- 器械子板块
         -- 自由器械
         (16, '自由器械', 3, 2, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
         -- 固定器械
         (17, '固定器械', 3, 2, 0, 1, 1, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           -- 自由器械的子版块
           (18, '哑铃', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (19, '波速球', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (20, '瑞士球', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (21, '药球', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (22, '战绳', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (23, 'TRX绳', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (24, '弹力绳', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (25, '壶铃', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (26, '杠铃', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (27, '泡沫轴', 16, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           --  固定器械的子版块
           (28, '龙门架', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (29, '腿弯举机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (30, '腿屈伸机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (31, '提踵机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (32, '单杠', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (33, '双杠', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (34, '仰卧板', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (35, '罗马椅', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (36, '牧师凳', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (37, '哈克机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (38, '倒蹬机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (39, '史密斯机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (40, '推胸机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (41, '悍马机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (42, '夹胸器', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (43, '划船机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (44, '下拉机', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (45, '地雷架', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (46, '推肩器', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
           (47, '有氧器械', 17, 3, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       -- 饮食子板块
       (48, '健身食谱', 4, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (49, '蛋白粉', 4, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (50, '增肌粉', 4, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (51, '肌酸', 4, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (52, '维生素', 4, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       -- 资讯子版块
       (53, '心得', 5, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (54, '新闻', 5, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56'),
       (55, '视频', 5, 2, 0, 1, 0, 1, '2023-02-13 12:34:56', '2023-02-13 12:34:56');


/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */; 