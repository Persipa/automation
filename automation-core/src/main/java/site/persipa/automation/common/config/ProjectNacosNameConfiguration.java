package site.persipa.automation.common.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author persipa
 */
@Configuration
public class ProjectNacosNameConfiguration implements EnvironmentAware {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        if (StrUtil.isBlank(System.getProperty("project.name"))) {
            System.setProperty("project.name", applicationName);
        }
    }
}
