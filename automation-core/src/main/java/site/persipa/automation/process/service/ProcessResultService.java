package site.persipa.automation.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessResult;

/**
 * @author persipa
 */
public interface ProcessResultService extends IService<ProcessResult> {

    String saveLog(String configId, ProcessTypeEnum processType);

    void completeLog(String logId, String processId, ProcessStatusEnum processStatus);

}
