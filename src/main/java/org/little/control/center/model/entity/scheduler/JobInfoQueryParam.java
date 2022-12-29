package org.little.control.center.model.entity.scheduler;

/**
 * 任务信息查询参数
 */
public class JobInfoQueryParam {
    private Boolean pause;
    private Boolean enable;

    public Boolean getPause() {
        return pause;
    }

    public void setPause(Boolean pause) {
        this.pause = pause;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
