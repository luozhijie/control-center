package org.little.control.center.model.entity.scheduler.param;


import org.little.control.center.model.constant.SchedulerConsts;

/**
 * 执行http请求的任务参数。
 */
public class HttpHandlerParam extends BaseHandlerParam {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;
    /**
     * 是否为异步请求。
     */
    private Boolean async = false;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    @Override
    public String getHandler() {
        return SchedulerConsts.Handler.HTTP;
    }
}
