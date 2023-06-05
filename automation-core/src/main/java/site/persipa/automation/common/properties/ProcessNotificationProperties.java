package site.persipa.automation.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author persipa
 */
@Data
@ConfigurationProperties("custom.process.notification")
public class ProcessNotificationProperties {

    private String success;

    private String fail;
}
