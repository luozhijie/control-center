package org.little.control.center.model.dao;


import org.apache.ibatis.annotations.Param;
import org.little.control.center.model.entity.scheduler.JobLog;

import java.util.Date;
import java.util.List;

public interface JobLogDao {

    /**
     * 插入信息
     * @param jobLog
     * @return
     */
    int insert(JobLog jobLog);

    /**
     * 获取创建时间，按照倒序排列
     * @param jobId
     * @return
     */
    List<Date> getCreateTime(@Param("jobId") Long jobId);

    /**
     * 查找指定任务的执行日志
     * @param jobId
     * @return
     */
    List<JobLog> listByJobId(@Param("jobId") Long jobId);

    /**
     * 删除在此时间以前的日志
     * @param jobId
     * @param createTime
     * @return
     */
    int deleteBefore(@Param("jobId") Long jobId, @Param("createTime") Date createTime);

    /**
     * 删除指定任务的执行日志
     * @param jobId
     * @return
     */
    int deleteByJobId(@Param("jobId") Long jobId);


}
