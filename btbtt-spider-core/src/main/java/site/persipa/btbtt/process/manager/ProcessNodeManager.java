package site.persipa.btbtt.process.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeDto;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeService;

import java.util.List;

/**
 * @author persipa
 */
@Component
public class ProcessNodeManager {

    @Autowired
    private ProcessConfigService processConfigService;
    @Autowired
    private ProcessNodeService processNodeService;

    public String add(ProcessNodeDto processingDto) {
        String configId = processingDto.getConfigId();
        ProcessConfig processConfig = processConfigService.getById(configId);
        if (processConfig == null) {
            // todo 直接抛出异常
            return null;
        }
        List<ProcessNode> nodeList = processNodeService.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId));

        return null;
    }

}
