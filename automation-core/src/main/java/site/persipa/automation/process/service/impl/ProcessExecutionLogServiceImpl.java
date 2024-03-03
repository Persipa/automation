package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessExecutionLog;
import site.persipa.automation.process.mapper.ProcessExecutionLogMapper;
import site.persipa.automation.process.service.ProcessExecutionLogService;

/**
 * @author persipa
 */
@Service
public class ProcessExecutionLogServiceImpl extends ServiceImpl<ProcessExecutionLogMapper, ProcessExecutionLog>
        implements ProcessExecutionLogService {
}
