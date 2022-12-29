package org.little.control.center.model;


import net.xctec.framework.core.service.IErrorCode;
import net.xctec.framework.core.service.MyBaseErrorCode;

/**
 * 错误码定义。
 */
public class MyErrorCode extends MyBaseErrorCode {

    /** 执行目标任务发生错误 */
    public static final IErrorCode PROCESS_JOB_FAILED = new MyBaseErrorCode(8090001){};

    /** 管理任务调度发生错误 */
    public static final IErrorCode MANAGE_JOB_FAILED = new MyBaseErrorCode(8090002){};




    protected MyErrorCode(int code) {
        super(code);
    }
}
