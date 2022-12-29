package org.little.control.center.model.service.scheduler;

import org.apache.commons.lang3.StringUtils;
import org.little.control.center.model.AppConfig;
import org.little.control.center.model.MyErrorCode;
import org.little.control.center.model.dao.JobInfoDao;
import org.little.control.center.model.dao.JobLogDao;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.JobLog;
import net.xctec.framework.core.dao.PageHelperUtils;
import net.xctec.framework.core.dao.StartRowsPageParameterImpl;
import net.xctec.framework.core.entity.MyPageData;
import net.xctec.framework.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class BaseHandler extends BaseService {

    @Autowired
    private JobLogDao jobLogDao;
    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private AppConfig appConfig;

    /**
     * 处理任务完成。
     * @param jobInfo 任务信息
     * @param code 执行结果码，0表示成功，其它值失败。
     * @param message 结果信息。
     */
    public void processComplete(JobInfo jobInfo, int code, String message) {
        Date now = new Date();
        HandleResultManager.put(jobInfo, code);
        jobInfoDao.updateLastExec(jobInfo.getId(), code, now);
        StartRowsPageParameterImpl pageParameter = new StartRowsPageParameterImpl();
        pageParameter.setStart(appConfig.getLogStoreLimit());
        pageParameter.setRows(1);
        PageHelperUtils.offsetPage(pageParameter);
        List<Date> list = jobLogDao.getCreateTime(jobInfo.getId());
        MyPageData<Date> pageData = PageHelperUtils.toPageData(list);
        if (pageData.getTotal() > appConfig.getLogStoreLimit()
                && pageData.getRows() != null
                && pageData.getRows().size() > 0) {
            // 记录数大于保留的行数，删除不需保留的数据
            Date date = pageData.getRows().get(0);
            logger.debug("任务记录数为" + pageData.getTotal() + "，大于保留的行数" + appConfig.getLogStoreLimit() + "，删除" + date + "时间以前的数据");
            jobLogDao.deleteBefore(jobInfo.getId(), date);
        }

        //如果执行错误则记录日志
        if (code != MyErrorCode.SUCCESS.getCode()) {
            if (StringUtils.isBlank(message)) {
                message = "未知错误";
            }
        } else {
            if (StringUtils.isBlank(message)) {
                message = "执行成功";
            }
        }
        JobLog log = new JobLog();
        log.setId(UUID.randomUUID().toString());
        log.setJobId(jobInfo.getId());
        log.setCode(code);
        log.setMessage(message);
        log.setCreateTime(now);
        try {
            jobLogDao.insert(log);
        } catch (Exception e) {
            logger.error("insert log error:", e);
        }
    }//end processFailed

}
