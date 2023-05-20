package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.process.mapper.ProcessNodeMapper;
import site.persipa.btbtt.process.service.ProcessNodeService;

/**
 * @author persipa
 */
@Service
public class ProcessNodeServiceImpl extends ServiceImpl<ProcessNodeMapper, ProcessNode>
        implements ProcessNodeService {
}
