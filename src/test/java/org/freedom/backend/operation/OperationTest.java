package org.freedom.backend.operation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiayx
 */
@RunWith(SpringRunner.class)
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
//@PropertySource("classpath:org/freedom/backend/operation/operation.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OperationTest {

    @Controller
    public static class OutputOperationController {
        @Autowired
        private OperationProperties operationProperties;

        @ResponseBody
        @RequestMapping("/**")
        public Object index(HttpServletRequest request) {
            return request.getAttribute(operationProperties.getAttribute()).toString();
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    /** 基本功能测试 */
    @Test
    public void baseFunction() {

        Map<String, Operation> operations = new HashMap<>();
        operations.put("/user/add", OperationProperties.OPERATION_ADD);
        operations.put("/user/view", OperationProperties.OPERATION_VIEW);
        operations.put("/user/edit", OperationProperties.OPERATION_EDIT);
        operations.forEach((key, value) -> {
            String body = this.restTemplate.getForObject(key, String.class);
            assertThat(body).isEqualTo(value.toString());
        });
    }

}
