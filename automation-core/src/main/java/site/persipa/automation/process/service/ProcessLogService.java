package site.persipa.automation.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessLog;

/**
 * @author persipa
 */
public interface ProcessLogService extends IService<ProcessLog> {

    String saveLog(String configId, ProcessTypeEnum processType);

    void completeLog(String logId, ProcessStatusEnum processStatus);

}
