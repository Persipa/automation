package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.process.ProcessNodeEntity;
import site.persipa.btbtt.process.mapper.mybatis.ProcessNodeEntityMapper;
import site.persipa.btbtt.process.service.ProcessNodeEntityService;

/**
 * @author persipa
 */
@Service
public class ProcessNodeEntityServiceImpl extends ServiceImpl<ProcessNodeEntityMapper, ProcessNodeEntity>
        implements ProcessNodeEntityService {
}
