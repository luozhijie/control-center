package org.little.control.center;

import net.xctec.framework.core.web.MyWebMvcConfigurer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;


@EnableAsync
@EnableAutoConfiguration
@SpringBootApplication
@MapperScan(basePackages = "org.little.control.center.model.dao")
@ImportResource({"spring-httpclient.xml"})
public class ControlCenterApplication extends MyWebMvcConfigurer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ControlCenterApplication.class).run(args);
    }

    /**
     * 默认输出JSON格式
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON);
    }

}
