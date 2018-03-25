package org.freedom.backend.operation;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 操作配置
 *
 * @author xiayx
 */
@ConfigurationProperties(prefix = "freedom.operation")
public class OperationProperties {

    /** 操作信息存储在请求中的属性名 */
    public static final String ATTRIBUTE = Operation.class.getName() + ".attribute";
    /** 常用的操作信息 */
    public static final Operation
            OPERATION_ADD = new Operation("add", "新增", "/add"),
            OPERATION_VIEW = new Operation("view", "查看", "/view"),
            OPERATION_EDIT = new Operation("edit", "编辑", "/edit");

    /** 操作拦截器 */
    private String[] patterns = {"/**"};
    private Map<String, Operation> operations = new LinkedHashMap<>();
    private String attribute = ATTRIBUTE;

    public OperationProperties() {
        operations.put("/**/add", OPERATION_ADD);
        operations.put("/**/view", OPERATION_VIEW);
        operations.put("/**/edit", OPERATION_EDIT);
    }

    @Override
    public String toString() {
        return "OperationProperties{" +
                "patterns=" + Arrays.toString(patterns) +
                ", operations=" + operations +
                ", attribute='" + attribute + '\'' +
                '}';
    }

    public Map<String, Operation> getOperations() {
        return operations;
    }

    public void setOperations(Map<String, Operation> operations) {
        this.operations = operations;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }
}