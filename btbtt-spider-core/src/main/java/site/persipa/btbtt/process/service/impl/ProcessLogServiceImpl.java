package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.btbtt.enums.process.ProcessStatusEnum;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;
import site.persipa.btbtt.pojo.process.ProcessLog;
import site.persipa.btbtt.process.mapper.ProcessLogMapper;
import site.persipa.btbtt.process.service.ProcessLogService;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Service
public class ProcessLogServiceImpl extends ServiceImpl<ProcessLogMapper, ProcessLog>
        implements ProcessLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public boolean completeLog(String logId, boolean success) {
        ProcessStatusEnum processStatus = success ? ProcessStatusEnum.SUCCESS : ProcessStatusEnum.FAIL;
        return this.update(Wrappers.lambdaUpdate(ProcessLog.class)
                .set(ProcessLog::getProcessStatus, processStatus)
                .set(ProcessLog::getCompleteTime, LocalDateTime.now())
                .eq(ProcessLog::getId, logId));
    }
}
