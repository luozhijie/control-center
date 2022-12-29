package org.little.control.center.controller;


import org.little.control.center.model.MyErrorCode;
import org.little.control.center.model.entity.scheduler.HandleResult;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.service.scheduler.HandleResultManager;
import org.little.control.center.model.service.scheduler.HandlerManager;
import org.little.control.center.model.service.scheduler.JobService;
import net.xctec.framework.core.entity.MyResponse;
import net.xctec.framework.core.util.MyJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务调度控制器
 */
@RestController
public class JobController extends BaseController {

    @Autowired
    private JobService jobService;

    /**
     * 获取所有任务
     * @return
     */
    @RequestMapping(value = "/job/list")
    public MyResponse<List<JobInfo>> list() {
        List<JobInfo> jobInfos = jobService.getAllJobs();
        if (jobInfos != null) {
            for (JobInfo jobInfo : jobInfos) {
                HandleResult result = HandleResultManager.get(jobInfo.getId());
                if (result != null) {
                    jobInfo.setLastExecCode(result.isSuccess() ? MyErrorCode.SUCCESS.getCode() : -1);
                    jobInfo.setLastExecTime(result.getProcessTime());
                }
            }
        }
        return new MyResponse<>(jobInfos);
    }

    /**
     * 获取指定任务的详细信息。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/get")
    public MyResponse<JobInfo> get(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.get(id));
    }

    /**
     * 插入一个任务。
     * @param jobInfo
     * @return
     */
    @RequestMapping(value = "/job/insert")
    public MyResponse<JobInfo> insert(JobInfo jobInfo) {
        jobService.insert(jobInfo);
        return new MyResponse<>(jobInfo);
    }

    /**
     * 更新任务。
     * @param jobInfo
     * @return
     */
    @RequestMapping(value = "/job/update")
    public MyResponse<JobInfo> update(JobInfo jobInfo) {
        jobService.update(jobInfo);
        return new MyResponse<>(jobInfo);
    }

    /**
     * 删除任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/delete")
    public MyResponse delete(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.delete(id));
    }

    /**
     * 暂停任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/pause")
    public MyResponse pause(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.pause(id));
    }

    /**
     * 重新运行任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/resume")
    public MyResponse resume(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.resume(id));
    }

    /**
     * 立即执行任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/process")
    public MyResponse process(@RequestParam("id") Long id) {
        MyResponse response = new MyResponse();
        JobInfo jobInfo = jobService.get(id);
        if (jobInfo == null) {
            response.setErrorCode(MyErrorCode.NOT_FOUND);
        }
        logger.info("process job {}", MyJsonHelper.toJson(jobInfo));
        HandlerManager.process(jobInfo);
        return response;
    }

    /**
     * 运行任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/run")
    public MyResponse run(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.run(id));
    }

    /**
     * 启用任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/enable")
    public MyResponse enable(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.enable(id));
    }

    /**
     * 禁用任务。
     * @param id
     * @return
     */
    @RequestMapping(value = "/job/disable")
    public MyResponse disable(@RequestParam("id") Long id) {
        return new MyResponse<>(jobService.disable(id));
    }

}
