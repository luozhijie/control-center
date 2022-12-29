
/*==============================================================*/
/* Table: t_job_info                                            */
/*==============================================================*/
create table t_job_info (
   id                   numeric(20,0)        not null,
   handler              varchar(32)          null,
   params               varchar(4000)        null,
   cron_expression      varchar(128)         null,
   job_group            varchar(64)          null,
   job_name             varchar(64)          null,
   trigger_group        varchar(64)          null,
   trigger_name         varchar(64)          null,
   pause                numeric(1,0)         null,
   enable               numeric(1,0)         null,
   description          varchar(1024)        null,
   last_exec_code       numeric(6,0)         null,
   last_exec_time       datetime             null,
   create_time          datetime             null,
   last_modified        datetime             null,
   constraint PK_T_JOB_INFO primary key (id)
)

;

/*==============================================================*/
/* Table: t_job_log                                             */
/*==============================================================*/
create table t_job_log (
   id                   varchar(64)          not null,
   job_id               numeric(20,0)        null,
   code                 numeric(6,0)         null,
   message              text                 null,
   create_time          datetime             null,
   constraint PK_T_JOB_LOG primary key (id)
);

create index ix_create_time on t_job_log(create_time);




