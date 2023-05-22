package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.enums.process.ProcessNodeStatusEnum;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.process.mapper.ProcessConfigMapper;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeService;

/**
 * @author persipa
 */
@Service
@RequiredArgsConstructor
public class ProcessConfigServiceImpl extends ServiceImpl<ProcessConfigMapper, ProcessConfig>
        implements ProcessConfigService {

    private final ProcessNodeService processNodeService;

    @Override
    public void flushStatus(String configId) {
        long unsavedNodeCount = processNodeService.count(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .ne(ProcessNode::getNodeStatus, ProcessNodeStatusEnum.SAVED));
        if (unsavedNodeCount == 0) {
            this.update(Wrappers.lambdaUpdate(ProcessConfig.class)
                    .set(ProcessConfig::getProcessStatus, ProcessConfigStatusEnum.SAVED)
                    .eq(ProcessConfig::getId, configId));
        }
    }
}
