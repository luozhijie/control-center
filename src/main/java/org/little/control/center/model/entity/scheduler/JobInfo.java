package org.little.control.center.model.entity.scheduler;

import java.util.Date;

/**
 * 任务描述的定义。
 */
public class JobInfo {

    private Long id;
    /**任务执行器分类*/
    private String handler;

    /**与任务类型相关的参数，比如对于HTTP请求的任务，需要制定method、async等*/
    private String params;

    /**任务执行表达式*/
    private String cronExpression;
    private String jobName;

    private String jobGroup;

    private String triggerName;

    private String triggerGroup;

    private Boolean pause = false;

    private Boolean enable = true;

    private String description;

    private Integer lastExecCode; //最后执行结果码

    private Date lastExecTime; //最后执行时间

    private Date createTime;

    private Date lastModified;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLastExecCode() {
        return lastExecCode;
    }

    public void setLastExecCode(Integer lastExecCode) {
        this.lastExecCode = lastExecCode;
    }

    public Date getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

}
