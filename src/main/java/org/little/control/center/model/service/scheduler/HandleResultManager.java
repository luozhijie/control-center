package org.little.control.center.model.service.scheduler;


import org.little.control.center.model.MyErrorCode;
import org.little.control.center.model.entity.scheduler.HandleResult;
import org.little.control.center.model.entity.scheduler.JobInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理结果管理。
 */
public class HandleResultManager {

    private static final Map<Long,HandleResult> mapResult = new HashMap<>();

    /**
     * 添加任务处理结果。
     * @param jobInfo
     * @param code
     */
    public static void put(JobInfo jobInfo, int code) {
        HandleResult result = mapResult.get(jobInfo.getId());
        if (result == null) {
            result = new HandleResult();
        }
        result.setId(jobInfo.getId());
        result.setProcessTime(new Date());
        result.setSuccess(code == MyErrorCode.SUCCESS.getCode());
        mapResult.put(jobInfo.getId(), result);
    }

    /**
     * 获取一个任务的处理结果。
     * @param id 任务ID
     * @return
     */
    public static HandleResult get(Long id) {
        return mapResult.get(id);
    }

    /**
     * 获取所有结果。
     * @return
     */
    public static Map<Long,HandleResult> getAll() {
        return mapResult;
    }

}
