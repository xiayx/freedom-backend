package org.freedom.backend.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 操作自动配置
 *
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(OperationProperties.class)
public class OperationAutoConfiguration implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private OperationProperties operationProperties;

    public OperationAutoConfiguration(OperationProperties operationProperties) {
        this.operationProperties = operationProperties;
        logger.debug("取得操作属性: {}", operationProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "operationInterceptor")
    public HandlerInterceptor operationInterceptor() {
        return new OperationInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(OperationParser.class)
    public OperationParser operationParser() {
        return new OperationParserImpl();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor interceptor = operationInterceptor();
        logger.debug("取得操作拦截器: {}", interceptor);
        registry.addInterceptor(interceptor).addPathPatterns(operationProperties.getPatterns());
    }
}
