package org.freedom.backend.operation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

/**
 * 因为通过@ConfigurationProperties读取的属性名，特殊字符被忽略，
 * 所以测试不通过@ConfigurationProperties读取属性名，是否忽略属性名中的特殊字符，
 * 发现，没有忽略
 *
 * @author xiayx
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@PropertySource("classpath:org/freedom/backend/operation/operation.properties")
@Import(PropertyPlaceholderAutoConfiguration.class)
public class PropertiesTest {

    @Autowired
    private Environment environment;

    /** 使用原始方式直接读取 */
    @Test
    public void basic() throws Exception {
        Properties properties = new Properties();
        properties.load(PropertiesTest.class.getResourceAsStream("operation.properties"));
        Assert.assertEquals("add", properties.getProperty("freedom.operation.operations./**/add.code"));
    }

    /** 使用@PropertySource读取 */
    @Test
    public void properties() throws Exception {
        Assert.assertEquals("add", environment.getProperty("freedom.operation.operations./**/add.code"));
    }
}
