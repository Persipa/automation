package site.persipa.btbtt.common.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author persipa
 */
@Configuration
@EnableDiscoveryClient
@RefreshScope
public class NacosConfiguration {
}
