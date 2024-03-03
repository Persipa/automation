package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessExecution;
import site.persipa.automation.process.mapper.ProcessExecutionMapper;
import site.persipa.automation.process.service.ProcessExecutionService;

/**
 * @author persipa
 */
@Service
public class ProcessExecutionServiceImpl extends ServiceImpl<ProcessExecutionMapper, ProcessExecution>
        implements ProcessExecutionService {
}
