package org.little.control.center.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * 应用配置。
 */
@Repository
@Data
public class AppConfig {

    /**
     * 每个任务保留的执行日志条数
     */
    @Value("${app.logStoreLimit}")
    private int logStoreLimit;



}
