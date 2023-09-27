package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.process.ProcessResultItem;
import site.persipa.automation.process.mapper.ProcessResultItemMapper;
import site.persipa.automation.process.service.ProcessResultItemService;

/**
 * @author persipa
 */
@Service
public class ProcessResultItemServiceImpl extends ServiceImpl<ProcessResultItemMapper, ProcessResultItem>
        implements ProcessResultItemService {
}
