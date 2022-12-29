package org.little.control.center.model.entity.scheduler;

import net.xctec.framework.core.util.MyJsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.little.control.center.model.entity.scheduler.param.BaseHandlerParam;
import net.xctec.framework.core.util.MyClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供获取执行器参数的方法。
 */
public class HandlerParamFactory {

    private static final Map<String,Class<?>> classMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(HandlerParamFactory.class);

    static {
        mapClass();
    }


    /**
     * 根据执行器类型获取对应参数类。
     * @param handler 执行器类型
     * @return class，如果不存在则返回null。
     */
    public static Class<?> getClass(String handler) {
        return classMap.get(handler);
    }


    /**
     * 根据执行器类型和JSON数据提取执行器参数对象
     * @param handler 执行器类型
     * @param json 原始参数JSON数据
     * @return 如果不存在则返回null。
     */
    public static BaseHandlerParam getHandlerParam(String handler, String json) {
        if (StringUtils.isBlank(handler)) {
            logger.debug("handler is empty");
            return null;
        }
        if (StringUtils.isBlank(json)) {
            logger.debug("json is empty");
            return null;
        }
        Class<?> cls = getClass(handler);
        if (cls == null) {
            logger.debug("can't get class from handler = {}", handler);
            return null;
        }
        Object object = MyJsonHelper.fromJson(json, cls);
        if (object == null) {
            return null;
        }
        if (object instanceof BaseHandlerParam) {
            return (BaseHandlerParam)object;
        }
        logger.debug("data is not instanceof BaseHandlerParam");
        return null;
    }//end getHandlerParam


    /**
     * 建立数据类型与类的对应关系。
     */
    private static void mapClass() {
        List<Class<?>> classList = MyClassUtils.getAssignedClasses(BaseHandlerParam.class);
        if (classList == null || classList.size() == 0) {
            return;
        }
        try {
            for (Class<?> cls : classList) {
                Object o = cls.newInstance();
                BaseHandlerParam param = (BaseHandlerParam)o;
                classMap.put(param.getHandler(), cls);
                //logger.info("key = {}", buildKey(param.getDspCategory(), param.getDspType()));
            }
        } catch (Exception e) {
            logger.error("mapClass error:", e);
        }

    }//end mapClass

    public static void main(String[] args) {
        mapClass();
    }

}
