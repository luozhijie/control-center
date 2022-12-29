package org.little.control.center;

import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.service.scheduler.JobService;
import org.little.control.center.model.service.scheduler.SchedulerManager;
import net.xctec.framework.core.service.MyServiceException;
import net.xctec.framework.core.util.MyJsonHelper;
import org.quartz.CronTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统启动加载所有的任务。
 */
@Component
public class QuartzInitListener implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(QuartzInitListener.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private SchedulerManager schedulerManager;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<JobInfo> jobs = jobService.getAllEnableJobs();
        if (jobs == null || jobs.size() == 0) {
            logger.info("no job");
            return;
        }
        for (JobInfo job : jobs) {
            try {
                CronTrigger cronTrigger = schedulerManager.getCronTrigger(job);
                String json = MyJsonHelper.toJson(job);
                if (cronTrigger == null) {
                    try {
                        schedulerManager.createJob(job);
                        logger.info("create job = {}", json);
                    } catch (MyServiceException ex) {
                        //创建失败则更新
                        schedulerManager.updateJob(job);
                        logger.info("update job = {}", json);
                    }

                } else {
                    schedulerManager.updateJob(job);
                    logger.info("update job = {}", json);
                }
            } catch (Exception e) {
                logger.error("run error:", e);
            }

        }

    }//end run
}
