package site.persipa.btbtt.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author persipa
 */
@Configuration
public class XxlJobConfig {

    @Value("${xxl.job.admin.address}")
    private String adminAddress;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddress);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        return xxlJobSpringExecutor;
    }
}
