
-- 任务信息表
CREATE TABLE `t_job_info` (
  `id` bigint(20) NOT NULL  ,
  `handler` varchar(32) NOT NULL ,
  `params` varchar(4000)   NULL,
  `cron_expression` varchar(128)  NOT NULL ,
  `job_group` varchar(64)  NOT NULL ,
  `job_name` varchar(64)  NOT NULL ,
  `trigger_group` varchar(64)  NOT NULL ,
  `trigger_name` varchar(64)  NOT NULL ,
  `pause` tinyint(1)  NOT NULL default 0 ,
  `enable` tinyint(1)  NOT NULL default 1 ,
  `description` varchar(1024)   NULL ,
  `last_exec_code` int(6)  NULL ,
  `last_exec_time` datetime  NULL ,
  `create_time` datetime NOT NULL ,
  `last_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ;

-- 任务执行（错误）日志
CREATE TABLE `t_job_log` (
  `id` varchar(64) NOT NULL  ,
  `job_id` bigint(20) NOT NULL  ,
  `code` int(6)  NULL   ,
  `message` text  NULL  ,
  `create_time` datetime NOT NULL ,

  PRIMARY KEY (`id`)
) ;

create index ix_create_time on t_job_log(create_time);
