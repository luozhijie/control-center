package org.little.control.center.model.service.scheduler;


import org.little.control.center.model.MyErrorCode;
import org.little.control.center.model.entity.scheduler.JobInfo;
import net.xctec.framework.core.service.BaseService;
import net.xctec.framework.core.service.MyServiceException;
import net.xctec.framework.core.util.MyJsonHelper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务调度的常用管理。
 */
@Service
public class SchedulerManager extends BaseService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 创建任务
     * @param jobInfo 任务属性。
     */
    public void createJob(JobInfo jobInfo) {
        validateCronExpression(jobInfo);
        String data = MyJsonHelper.toJson(jobInfo);
        try {
            JobDataMap jobDataMap = MyJob.toJobDataMap(jobInfo);
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    .setJobData(jobDataMap)
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
                    .withDescription(jobInfo.getDescription())
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobInfo.getTriggerName(), jobInfo.getTriggerGroup())
                    .withDescription(jobInfo.getDescription())
                    .withSchedule(scheduleBuilder)
                    .startNow()
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

            logger.info("Create schedule job success, data is {}", data);

            if (jobInfo.getPause() != null && jobInfo.getPause()) {
                pauseJob(jobInfo);
            }
        } catch (Exception e) {
            logger.error("create job error(data = " + data + "):", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }//end createJob

    /**
     * 更新任务
     * @param jobInfo 任务属性。
     */
    public void updateJob(JobInfo jobInfo) {
        validateCronExpression(jobInfo);
        String data = MyJsonHelper.toJson(jobInfo);
        try {
            TriggerKey triggerKey = getTriggerKey(jobInfo);

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = getCronTrigger(jobInfo);

            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(jobInfo.getDescription())
                    .withSchedule(cronScheduleBuilder).build();

            scheduler.rescheduleJob(triggerKey, cronTrigger);

            logger.info("update schedule job success, data is {}", data);

            if (jobInfo.getPause() != null && jobInfo.getPause()) {
                pauseJob(jobInfo);
            }
        } catch (Exception e) {
            logger.error("update job error(data = " + data + "):", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }//end updateJob

    /**
     * 运行计划任务
     * @param jobInfo
     */
    public void runJob(JobInfo jobInfo) {
        try {
            scheduler.triggerJob(getJobKey(jobInfo));
            logger.info("run schedule job  success, job info is {}", MyJsonHelper.toJson(jobInfo));
        } catch (SchedulerException e) {
            logger.error("run schedule job failed", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }

    /**
     * 暂停计划任务
     * @param jobInfo
     */
    public void pauseJob(JobInfo jobInfo) {
        try {
            scheduler.pauseJob(getJobKey(jobInfo));
            logger.info("Pause schedule job  success, job info is {}", MyJsonHelper.toJson(jobInfo));
        } catch (SchedulerException e) {
            logger.error("Pause schedule job failed", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }

    /**
     * 重新激活计划任务
     * @param jobInfo
     */
    public void resumeJob(JobInfo jobInfo) {
        try {
            scheduler.resumeJob(getJobKey(jobInfo));
            logger.info("resume schedule job  success, job info is {}", MyJsonHelper.toJson(jobInfo));
        } catch (SchedulerException e) {
            logger.error("resume schedule job failed", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }

    /**
     * 获取CronTrigger
     * @param schedulerJob
     * @return
     */
    public CronTrigger getCronTrigger(JobInfo schedulerJob) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(schedulerJob));
        } catch (SchedulerException e) {
            logger.error("getCronTrigger error:", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }

    /**
     * 重删除计划任务
     * @param jobInfo
     */
    public void deleteJob(JobInfo jobInfo) {
        try {
            scheduler.deleteJob(getJobKey(jobInfo));
            logger.info("delete schedule job  success, job info is {}", MyJsonHelper.toJson(jobInfo));
        } catch (SchedulerException e) {
            logger.error("delete schedule job failed", e);
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED);
        }
    }

    /**
     * 构建TriggerKey
     * @param schedulerJob
     * @return
     */
    private TriggerKey getTriggerKey(JobInfo schedulerJob) {
        return TriggerKey.triggerKey(schedulerJob.getTriggerName(), schedulerJob.getTriggerGroup());
    }

    /**
     * 构建JobKey
     * @param schedulerJob
     * @return
     */
    private JobKey getJobKey(JobInfo schedulerJob) {
        return JobKey.jobKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());
    }



    /**
     * 校验Cron表达式
     */
    private void validateCronExpression(JobInfo schedulerJob) throws MyServiceException {
        if (!CronExpression.isValidExpression(schedulerJob.getCronExpression())) {
            String message = String.format("Job %s expression %s is not correct!", schedulerJob.getHandler(), schedulerJob.getCronExpression());
            throw new MyServiceException(MyErrorCode.MANAGE_JOB_FAILED, message);
        }
    }

}
