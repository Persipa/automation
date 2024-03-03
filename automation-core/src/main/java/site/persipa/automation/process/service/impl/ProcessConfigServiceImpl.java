package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.enums.process.ProcessConfigStateEnum;
import site.persipa.automation.enums.process.ProcessNodeStatusEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.process.mapper.ProcessConfigMapper;
import site.persipa.automation.process.service.ProcessConfigService;
import site.persipa.automation.process.service.ProcessNodeService;

/**
 * @author persipa
 */
@Service
@RequiredArgsConstructor
public class ProcessConfigServiceImpl extends ServiceImpl<ProcessConfigMapper, ProcessConfig>
        implements ProcessConfigService {

    private final ProcessNodeService processNodeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void flushStatus(String configId) {
        long unsavedNodeCount = processNodeService.count(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .ne(ProcessNode::getNodeStatus, ProcessNodeStatusEnum.SAVED));
        if (unsavedNodeCount == 0) {
            this.update(Wrappers.lambdaUpdate(ProcessConfig.class)
                    .set(ProcessConfig::getConfigState, ProcessConfigStateEnum.SAVED)
                    .eq(ProcessConfig::getId, configId));
        }
    }
}
