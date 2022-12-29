package org.little.control.center.configuration;

import org.little.control.center.model.service.scheduler.SchedulerJobFactory;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class ScheduleConfig {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    @Autowired
    private SchedulerJobFactory schedulerJobFactory;

    @Bean
    @Qualifier("scheduleBean")
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        //schedulerFactoryBean.setSchedulerName("HOO_TASK_EXECUTOR");
        Properties properties = getQuartzProperties();
        if (properties != null) {
            schedulerFactoryBean.setQuartzProperties(properties);
        }

        // 延迟10秒启动
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        //schedulerFactoryBean.setDataSource(dataSource);
        // 将 JobFactory 改为自定义的，否则在 Job 中注入 Bean 会失败
        schedulerFactoryBean.setJobFactory(schedulerJobFactory);
        return schedulerFactoryBean;
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /**
     * 获取配置属性。
     * @return
     */
    private Properties getQuartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.xml"));
        try {
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } catch (IOException e) {
            logger.error("getQuartzProperties error:", e);
            return null;
        }

    }

}
