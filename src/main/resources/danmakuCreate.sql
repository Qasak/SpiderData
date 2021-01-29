<!-- ALTER TABLE bilibili_danmaku  modify COLUMN content VARCHAR(250) -->

CREATE TABLE `bilibili_danmaku` ( 
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT '主键ID', 
  `bv` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '视频BV号', 
  `bili_db_id` bigint(20) unsigned NOT NULL COMMENT 'B站数据库ID', 
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '弹幕发送时间', 
  `content` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '弹幕内容', 
  `user_hash` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户Hash', 
  `appear_time` float NOT NULL COMMENT '弹幕在视频中出现时间', 


  PRIMARY KEY (`id`), 
  KEY `idx_bili_db_id` (`bili_db_id`) USING BTREE,
  KEY `idx_send_time` (`send_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='bilibili弹幕信息'; 