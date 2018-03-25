package org.freedom.backend.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器拦截请求后，将请求对应的操作存储在请求中
 *
 * @author xiayx
 */
public class OperationInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OperationParser operationParser;
    @Autowired
    private OperationProperties operationProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("解析并存储操作信息[{}]", request.getRequestURI());
        Operation operation = operationParser.parse(request, handler);
        request.setAttribute(operationProperties.getAttribute(), operation);
        logger.debug("设置请求操作属性: {} = {}", operationProperties.getAttribute(), operation);
        return true;
    }
}
