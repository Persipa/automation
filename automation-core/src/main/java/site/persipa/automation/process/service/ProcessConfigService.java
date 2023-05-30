package site.persipa.automation.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.process.ProcessConfig;

/**
 * @author persipa
 */
public interface ProcessConfigService extends IService<ProcessConfig> {

    void flushStatus(String configId);
}
