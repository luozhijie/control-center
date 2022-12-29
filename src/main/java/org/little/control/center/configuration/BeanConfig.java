package org.little.control.center.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.xctec.framework.core.util.MyJsonHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 注册需要的beans
 */
@Configuration
public class BeanConfig {



    /**
     * 自定义ObjectMapper
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        MyJsonHelper.configure(objectMapper);//通用设置
        return objectMapper;
    }

}
