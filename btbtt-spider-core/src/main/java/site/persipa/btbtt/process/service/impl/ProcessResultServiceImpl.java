package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.process.ProcessResult;
import site.persipa.btbtt.process.mapper.ProcessResultMapper;
import site.persipa.btbtt.process.service.ProcessResultService;

/**
 * @author persipa
 */
@Service
public class ProcessResultServiceImpl extends ServiceImpl<ProcessResultMapper, ProcessResult>
        implements ProcessResultService {
}
