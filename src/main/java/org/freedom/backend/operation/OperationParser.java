package org.freedom.backend.operation;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiayx
 */
public interface OperationParser {

    @Nullable
    Operation parse(HttpServletRequest request, Object handler);

}
