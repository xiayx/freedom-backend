package org.freedom.backend.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiayx
 */
public class OperationParserImpl implements OperationParser {

    @Autowired
    private OperationProperties operationProperties;
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Operation parse(HttpServletRequest request, Object handler) {
        return operationProperties.getOperations()
                .entrySet().stream()
                .filter(entry -> pathMatcher.match(entry.getKey(), request.getRequestURI()))
                .findFirst().map(Map.Entry::getValue).orElse(null);


    }
}
