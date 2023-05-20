package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.process.mapper.ProcessConfigMapper;
import site.persipa.btbtt.process.service.ProcessConfigService;

/**
 * @author persipa
 */
@Service
public class ProcessConfigServiceImpl extends ServiceImpl<ProcessConfigMapper, ProcessConfig>
        implements ProcessConfigService {
}
