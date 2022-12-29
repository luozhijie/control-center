package org.little.control.center.model.service.scheduler;

import org.little.control.center.model.entity.scheduler.JobInfo;
import net.xctec.framework.core.service.MyServiceException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务调度辅助工具类。
 */
public class SchedulerHelper {

    private static Logger logger = LoggerFactory.getLogger(SchedulerHelper.class);

    /**
     * 构建TriggerKey
     * @param schedulerJob
     * @return
     */
    public static TriggerKey getTriggerKey(JobInfo schedulerJob) {
        return TriggerKey.triggerKey(schedulerJob.getTriggerName(), schedulerJob.getTriggerGroup());
    }

    /**
     * 构建JobKey
     * @param schedulerJob
     * @return
     */
    public static JobKey getJobKey(JobInfo schedulerJob) {
        return JobKey.jobKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());
    }

    /**
     * 获取CronTrigger
     * @param scheduler
     * @param schedulerJob
     * @return
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, JobInfo schedulerJob) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(schedulerJob));
        } catch (SchedulerException e) {
            logger.error("getCronTrigger error:", e);
            throw new MyServiceException("Get Cron trigger failed", e);
        }
    }

}
