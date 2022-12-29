package org.little.control.center.model.entity.scheduler;

import java.util.Date;

/**
 * 处理结果。
 */
public class HandleResult {

    private Long id;
    private Date processTime;//处理时间
    private boolean success = true;//处理是否成功

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
