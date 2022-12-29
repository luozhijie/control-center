package org.little.control.center.model.service.scheduler;


import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.param.BaseHandlerParam;

/**
 * 调度执行接口。
 * @param <T>
 */
public interface Handler<T extends BaseHandlerParam> {

    /**
     * 处理调度逻辑。
     * @param jobInfo 任务信息
     * @param handlerParam 执行器参数。
     * @return
     */
    void process(JobInfo jobInfo, T handlerParam);

    /**
     * 处理任务完成。
     * @param jobInfo 任务信息
     * @param code 执行结果码，0表示成功，其它值失败。
     * @param message 结果信息。
     */
    void processComplete(JobInfo jobInfo, int code, String message);

}
