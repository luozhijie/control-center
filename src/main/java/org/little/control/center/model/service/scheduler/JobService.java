package org.little.control.center.model.service.scheduler;

import org.little.control.center.model.dao.JobInfoDao;
import org.little.control.center.model.dao.JobLogDao;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.JobInfoQueryParam;
import net.xctec.framework.core.service.BaseService;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 任务管理。
 */
@Service
public class JobService extends BaseService {
    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private JobLogDao jobLogDao;
    @Autowired
    private SchedulerManager schedulerManager;

    /**
     * 获取所有启用的任务。
     * @return
     */
    public List<JobInfo> getAllEnableJobs() {
        JobInfoQueryParam param = new JobInfoQueryParam();
        param.setEnable(true);
        return getJobs(param);
    }

    /**
     * 获取所有任务。
     * @return
     */
    public List<JobInfo> getAllJobs() {
        return getJobs(null);
    }

    private List<JobInfo> getJobs(JobInfoQueryParam param) {
        return jobInfoDao.list(param);
    }//end getJobs

    /**
     * 获取任务信息。
     * @param id
     * @return
     */
    public JobInfo get(Long id) {
        return jobInfoDao.get(id);
    }

    /**
     * 插入一个任务。
     * @param jobInfo
     * @return
     */
    @Transactional
    public int insert(JobInfo jobInfo) {
        Long maxId = jobInfoDao.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }
        jobInfo.setId(maxId + 1);
        jobInfo.setCreateTime(new Date());
        jobInfo.setLastModified(new Date());
        int rows = jobInfoDao.insert(jobInfo);
        schedulerManager.createJob(jobInfo);
        return rows;
    }

    /**
     * 更新任务。
     * @param jobInfo
     * @return
     */
    @Transactional
    public int update(JobInfo jobInfo) {
        jobInfo.setLastModified(new Date());
        int rows = jobInfoDao.update(jobInfo);
        if (rows == 0) {
            return 0;
        }
        schedulerManager.updateJob(jobInfo);
        return rows;
    }

    /**
     * 删除任务。
     * @param id
     * @return
     */
    @Transactional
    public int delete(Long id) {
        JobInfo jobInfo = get(id);
        if (jobInfo == null) {
            return 0;
        }
        int rows = jobInfoDao.delete(id);
        if (rows == 0) {
            return 0;
        }
        jobLogDao.deleteByJobId(id);
        schedulerManager.deleteJob(jobInfo);
        return rows;
    }

    /**
     * 暂停任务。
     * @param id
     * @return
     */
    @Transactional
    public int pause(Long id) {
        return updatePause(id, true);
    }

    /**
     * 激活任务。
     * @param id
     * @return
     */
    @Transactional
    public int resume(Long id) {
        return updatePause(id, false);
    }

    /**
     * 启用任务。
     * @param id
     * @return
     */
    @Transactional
    public int enable(Long id) {
        return updateEnable(id, true);
    }

    /**
     * 禁用任务。
     * @param id
     * @return
     */
    @Transactional
    public int disable(Long id) {
        return updateEnable(id, false);
    }

    /**
     * 运行任务。
     * @param id
     * @return
     */
    public boolean run(Long id) {
        JobInfo jobInfo = get(id);
        if (jobInfo == null) {
            return false;
        }
        schedulerManager.runJob(jobInfo);
        return true;
    }//end run

    /**
     * 更新暂停状态
     * @param id
     * @param pause
     * @return
     */
    private int updatePause(Long id, boolean pause) {
        JobInfo jobInfo = get(id);
        if (jobInfo == null) {
            return 0;
        }
        jobInfo.setPause(pause);
        jobInfo.setLastModified(new Date());
        int rows = jobInfoDao.update(jobInfo);
        if (pause) {
            schedulerManager.pauseJob(jobInfo);
        } else {
            schedulerManager.resumeJob(jobInfo);
        }
        return rows;
    }

    /**
     * 更新启用状态
     * @param id
     * @param enable
     * @return
     */
    private int updateEnable(Long id, boolean enable) {
        JobInfo jobInfo = get(id);
        if (jobInfo == null) {
            return 0;
        }
        jobInfo.setEnable(enable);
        jobInfo.setLastModified(new Date());
        int rows = jobInfoDao.update(jobInfo);
        if (enable) {
            CronTrigger cronTrigger = schedulerManager.getCronTrigger(jobInfo);
            if (cronTrigger == null) {
                //不存在则创建
                schedulerManager.createJob(jobInfo);
            } else {
                schedulerManager.resumeJob(jobInfo);
            }
        } else {
            schedulerManager.pauseJob(jobInfo);
        }
        return rows;
    }

}
