package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessLog;
import site.persipa.automation.process.mapper.ProcessLogMapper;
import site.persipa.automation.process.service.ProcessLogService;

/**
 * @author persipa
 */
@Service
public class ProcessLogServiceImpl extends ServiceImpl<ProcessLogMapper, ProcessLog>
        implements ProcessLogService {
}
