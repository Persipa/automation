package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessExecutionResult;
import site.persipa.automation.process.mapper.ProcessExecutionResultMapper;
import site.persipa.automation.process.service.ProcessExecutionResultService;

/**
 * @author persipa
 */
@Service
public class ProcessExecutionResultServiceImpl extends ServiceImpl<ProcessExecutionResultMapper, ProcessExecutionResult>
        implements ProcessExecutionResultService {
}
