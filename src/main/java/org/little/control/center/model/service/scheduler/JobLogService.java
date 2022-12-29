package org.little.control.center.model.service.scheduler;

import org.little.control.center.model.dao.JobLogDao;
import org.little.control.center.model.entity.scheduler.JobLog;
import net.xctec.framework.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务日志处理。
 */
@Service
public class JobLogService extends BaseService {

    @Autowired
    private JobLogDao jobLogDao;

    /**
     * 查询指定任务的执行日志
     * @param jobId
     * @return
     */
    public List<JobLog> listByJobId(Long jobId) {
        return jobLogDao.listByJobId(jobId);
    }

}
