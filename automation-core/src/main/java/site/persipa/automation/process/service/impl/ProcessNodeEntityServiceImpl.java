package site.persipa.automation.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.pojo.process.ProcessNodeEntity;
import site.persipa.automation.process.mapper.ProcessNodeEntityMapper;
import site.persipa.automation.process.service.ProcessNodeEntityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
public class ProcessNodeEntityServiceImpl extends ServiceImpl<ProcessNodeEntityMapper, ProcessNodeEntity>
        implements ProcessNodeEntityService {

    @Override
    public List<ProcessNodeEntity> listByNodeId(String nodeId, Boolean argOrderAsc) {
        return this.list(Wrappers.lambdaQuery(ProcessNodeEntity.class)
                .eq(ProcessNodeEntity::getNodeId, nodeId)
                .orderBy(argOrderAsc != null, Boolean.TRUE.equals(argOrderAsc), ProcessNodeEntity::getArgOrder));
    }

    @Override
    public List<ProcessNodeEntity> listByEntityId(String entityId){
        return this.list(Wrappers.lambdaQuery(ProcessNodeEntity.class)
                .eq(ProcessNodeEntity::getEntityId, entityId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cloneNodeEntityList(Map<String, String> entityCloneMap) {
        // 查询所有节点的参数实例
        List<ProcessNodeEntity> nodeEntityList = this.list(Wrappers.lambdaQuery(ProcessNodeEntity.class)
                .in(ProcessNodeEntity::getNodeId, entityCloneMap.keySet()));
        if (nodeEntityList.isEmpty()) {
            return true;
        }
        // 参数实例按节点id分组
        Map<String, List<ProcessNodeEntity>> nodeIdEntityListMap = nodeEntityList.stream()
                .collect(Collectors.groupingBy(ProcessNodeEntity::getNodeId));

        // 遍历节点 复制参数实例
        List<ProcessNodeEntity> targetNodeEntityList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entityCloneMap.entrySet()) {
            List<ProcessNodeEntity> tempEntityList = nodeIdEntityListMap.get(entry.getKey());
            if (!tempEntityList.isEmpty()) {
                tempEntityList.forEach(entity -> {
                    entity.setId(null);
                    entity.setNodeId(entry.getValue());
                });
                targetNodeEntityList.addAll(tempEntityList);
            }
        }

        if (!targetNodeEntityList.isEmpty()) {
            return this.saveBatch(targetNodeEntityList);
        }
        return false;
    }
}
