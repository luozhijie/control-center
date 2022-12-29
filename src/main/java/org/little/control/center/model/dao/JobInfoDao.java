package org.little.control.center.model.dao;


import org.apache.ibatis.annotations.Param;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.JobInfoQueryParam;

import java.util.Date;
import java.util.List;

public interface JobInfoDao {

    /**
     * 插入信息
     * @param jobInfo
     * @return
     */
    int insert(JobInfo jobInfo);

    /**
     * 更新信息
     * @param jobInfo
     * @return
     */
    int update(JobInfo jobInfo);

    /**
     * 更新最后执行情况
     * @param id 任务ID
     * @param lastExecCode 最后只
     * @param lastExecTime
     * @return
     */
    int updateLastExec(@Param("id") Long id, @Param("lastExecCode") Integer lastExecCode, @Param("lastExecTime") Date lastExecTime);

    /**
     * 根据ID查询
     * @param id 任务ID
     * @return
     */
    JobInfo get(@Param("id") Long id);

    /**
     * 获取最大的ID值
     * @return
     */
    Long getMaxId();

    /**
     * 查询多条记录。
     * @param param
     * @return
     */
    List<JobInfo> list(JobInfoQueryParam param);

    /**
     * 删除信息
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);
}
