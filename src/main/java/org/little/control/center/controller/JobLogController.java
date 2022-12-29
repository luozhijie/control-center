package org.little.control.center.controller;


import org.little.control.center.model.entity.scheduler.JobLog;
import org.little.control.center.model.service.scheduler.JobLogService;
import net.xctec.framework.core.entity.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务日志控制器
 */
@RestController
public class JobLogController extends BaseController {

    @Autowired
    private JobLogService jobLogService;

    /**
     * 获取指定任务日志
     * @return
     */
    @RequestMapping(value = "/job/log/listByJobId")
    public MyResponse<List<JobLog>> listByJobId(@RequestParam("jobId") Long jobId) {
        List<JobLog> jobLogs = jobLogService.listByJobId(jobId);
        return new MyResponse<>(jobLogs);
    }



}
