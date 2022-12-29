package org.little.control.center.model.service.scheduler;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.little.control.center.model.entity.scheduler.JobInfo;
import net.xctec.framework.core.util.MyJsonHelper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 执行任务
 */
public class MyJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        if (map == null) {
            logger.warn("can't get JobDataMap");
            return;
        }
        JobInfo jobInfo = fromJobDataMap(map);
        logger.info("start execute job, info is {}", MyJsonHelper.toJson(jobInfo));
        HandlerManager.process(jobInfo);

    }//end execute


    /**
     * 将JobInfo转换为JobDataMap
     * @param jobInfo 任务属性。
     * @return
     */
    public static JobDataMap toJobDataMap(JobInfo jobInfo) {
        if (jobInfo == null) {
            return null;
        }
        JobDataMap map = new JobDataMap();
        try {
            Field[] fields = jobInfo.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                Object value = PropertyUtils.getProperty(jobInfo, name);
                if (value == null) {
                    continue;
                }
                if (value instanceof Date) {
                    map.put(name, String.valueOf(((Date)value).getTime()));
                } else {
                    map.put(name, value.toString());
                }
            }
        } catch (Exception e) {
            logger.error("toJobDataMap error:", e);
        }
        return map;
    }//end toJobDataMap

    /**
     * 将JobDataMap转换为JobInfo
     * @param map。
     * @return
     */
    public static JobInfo fromJobDataMap(JobDataMap map) {
        if (map == null) {
            return null;
        }
        JobInfo jobInfo = new JobInfo();
        try {
            String[] keys = map.getKeys();
            for (String key : keys) {
                String value = map.getString(key);
                Object v = convertValue(jobInfo, key, value);
                PropertyUtils.setProperty(jobInfo, key, v);
            }
        } catch (Exception e) {
            logger.error("toJobDataMap error:", e);
        }
        return jobInfo;
    }//end toJobDataMap

    /**
     * 转换为适合JobInfo的数据
     * @param jobInfo
     * @param name
     * @param value
     * @return
     */
    private static Object convertValue(JobInfo jobInfo, String name, String value) throws Exception {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Class type = PropertyUtils.getPropertyType(jobInfo, name);
        if (type == Date.class) {
            try {
                long v = Long.parseLong(value);
                return new Date(v);
            } catch (Exception e) {
                logger.error("convertValue error:", e);
                return null;
            }
        }

        return ConvertUtils.convert(value, type);
    }//end convertValue


}
