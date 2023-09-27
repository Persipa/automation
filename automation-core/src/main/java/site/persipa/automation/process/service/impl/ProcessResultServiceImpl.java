package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.process.mapper.ProcessResultMapper;
import site.persipa.automation.process.service.ProcessResultService;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Service
public class ProcessResultServiceImpl extends ServiceImpl<ProcessResultMapper, ProcessResult>
        implements ProcessResultService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveLog(String configId, ProcessTypeEnum processType) {
        ProcessResult processResult = new ProcessResult();
        processResult.setConfigId(configId);
//        processResult.setProcessType(processType);
//        processResult.setExecuteTime(LocalDateTime.now());
        processResult.setProcessStatus(ProcessStatusEnum.EXECUTING);

        this.save(processResult);
        return processResult.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeLog(String logId, String processId, ProcessStatusEnum processStatus) {
        this.update(Wrappers.lambdaUpdate(ProcessResult.class)
                .set(ProcessResult::getProcessStatus, processStatus)
                .set(ProcessResult::getProcessId, processId)
                .set(ProcessResult::getCompleteTime, LocalDateTime.now())
                .eq(ProcessResult::getId, logId));
    }
}
