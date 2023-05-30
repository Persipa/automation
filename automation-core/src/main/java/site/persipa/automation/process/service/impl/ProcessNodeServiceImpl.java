package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.process.mapper.ProcessNodeMapper;
import site.persipa.automation.process.service.ProcessNodeService;

import java.util.List;

/**
 * @author persipa
 */
@Service
public class ProcessNodeServiceImpl extends ServiceImpl<ProcessNodeMapper, ProcessNode>
        implements ProcessNodeService {
    @Override
    public List<ProcessNode> listByConfigId(String configId, Boolean isAsc) {
        return this.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .orderBy(isAsc != null, Boolean.TRUE.equals(isAsc), ProcessNode::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cloneNodeList(String sourceConfigId, String targetConfigId) {
        List<ProcessNode> nodeList = this.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, sourceConfigId));
        nodeList.forEach(node -> {
            node.setId(null);
            node.setConfigId(targetConfigId);
        });
        return this.saveBatch(nodeList);
    }

}
