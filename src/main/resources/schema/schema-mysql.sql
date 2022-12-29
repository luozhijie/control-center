
-- 任务信息表
CREATE TABLE `t_job_info` (
  `id` bigint(20) NOT NULL  COMMENT '主键',
  `handler` varchar(32) NOT NULL COMMENT '处理器类型',
  `params` varchar(4000)   NULL COMMENT '处理器参数',
  `cron_expression` varchar(128)  NOT NULL COMMENT '任务执行表达式',
  `job_group` varchar(64)  NOT NULL COMMENT '任务分组',
  `job_name` varchar(64)  NOT NULL COMMENT '任务名称',
  `trigger_group` varchar(64)  NOT NULL COMMENT '触发器分组',
  `trigger_name` varchar(64)  NOT NULL COMMENT '触发器名称',
  `pause` tinyint(1)  NOT NULL default 0 COMMENT '是否暂停任务，1-是，0-否',
  `enable` tinyint(1)  NOT NULL default 1 COMMENT '是否启用任务，1-是，0-否',
  `description` varchar(1024)   NULL COMMENT '任务描述',
  `last_exec_code` int(6)  NULL COMMENT '最后执行的结果码' ,
  `last_exec_time` datetime  NULL COMMENT '最后执行时间' ,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  unique uk_job(`job_group`,`job_name`),
  unique uk_trigger(`trigger_group`,`trigger_name`),
  PRIMARY KEY (`id`)
)  COMMENT='任务信息表';

-- 任务执行（错误）日志
CREATE TABLE `t_job_log` (
  `id` varchar(64) NOT NULL  COMMENT '主键',
  `job_id` bigint(20) NOT NULL  COMMENT '任务ID',
  `code` int(6)  NULL  COMMENT '错误码',
  `message` text  NULL COMMENT '错误信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',

  PRIMARY KEY (`id`),
  index idx_create_time (create_time)
)  COMMENT='任务执行（错误）日志';
