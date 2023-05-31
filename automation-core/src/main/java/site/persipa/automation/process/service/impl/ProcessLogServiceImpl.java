package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessLog;
import site.persipa.automation.process.mapper.ProcessLogMapper;
import site.persipa.automation.process.service.ProcessLogService;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Service
public class ProcessLogServiceImpl extends ServiceImpl<ProcessLogMapper, ProcessLog>
        implements ProcessLogService {

    @Override
    public String saveLog(String configId, ProcessTypeEnum processType) {
        ProcessLog processLog = new ProcessLog();
        processLog.setConfigId(configId);
        processLog.setProcessType(processType);
        processLog.setExecuteTime(LocalDateTime.now());
        processLog.setProcessStatus(ProcessStatusEnum.EXECUTING);

        this.save(processLog);
        return processLog.getId();
    }

    @Override
    public void completeLog(String logId, ProcessStatusEnum processStatus) {
        this.update(Wrappers.lambdaUpdate(ProcessLog.class)
                .set(ProcessLog::getProcessStatus, processStatus)
                .set(ProcessLog::getCompleteTime, LocalDateTime.now())
                .eq(ProcessLog::getId, logId));
    }
}
