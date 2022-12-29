package org.little.control.center.model.service.scheduler;

import org.apache.commons.lang3.StringUtils;
import org.little.control.center.model.entity.scheduler.HandlerParamFactory;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.param.BaseHandlerParam;
import net.xctec.framework.core.util.MyJsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 调度处理管理器。
 */
public class HandlerManager {

    private static Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private static final Map<String,Handler> handlerMap = new HashMap<>();

    /**
     * 注册一个处理器，以category和type的组合作为key。
     * @param handlerType 数据一级分类。
     * @param handler 处理器
     */
    public static void register(String handlerType, Handler handler) {
        handlerMap.put(handlerType, handler);
    }

    /**
     * 获取Handler。
     * @param handlerType 处理器类型。
     * @return 如果不存在则返回null。
     */
    public static Handler get(String handlerType) {
        return handlerMap.get(handlerType);
    }

    /**
     * 获取所有Handler的对应关系。
     * @return
     */
    public static Map<String, Handler> getHandlerMap() {
        return handlerMap;
    }

    /**
     * 处理入口。
     * @param job 任务属性。
     * @return 处理结果。
     */
    public static void process(JobInfo job) {
        if(StringUtils.isBlank(job.getParams())) {
            logger.info("BaseHandlerParam is null");
            return;
        }

        // 执行器类型不能为空
        if (StringUtils.isBlank(job.getHandler())) {
            logger.info("handler is empty");
            return;
        }

        Handler handler = get(job.getHandler());
        if (handler == null) {
            String message = "can't find handler for type = " + job.getHandler();
            logger.warn(message + ". then param is " + MyJsonHelper.toJson(job));
            return;
        }
        // 获取数据对象
        Class<?> cls = HandlerParamFactory.getClass(job.getHandler());
        if (cls == null) {
            String message = "can't find class for handler = " + job.getHandler();
            logger.warn(message + ". then param is " + MyJsonHelper.toJson(job));
            return;
        }

        Object object = MyJsonHelper.fromJson(job.getParams(), cls);
        if (object == null) {
            String message = "can't get data object for handler = " + job.getHandler();
            logger.warn(message + ". then param is " + MyJsonHelper.toJson(job));
            return;
        }

        // 根据服务操作名称选择对应的执行实现
        try {
            handler.process(job, (BaseHandlerParam) object);
        } catch (Exception e) {
            logger.error("process error:", e);
        }

    }//end process


}
