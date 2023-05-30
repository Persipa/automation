package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.process.mapper.ProcessResultMapper;
import site.persipa.automation.process.service.ProcessResultService;

/**
 * @author persipa
 */
@Service
public class ProcessResultServiceImpl extends ServiceImpl<ProcessResultMapper, ProcessResult>
        implements ProcessResultService {
}
