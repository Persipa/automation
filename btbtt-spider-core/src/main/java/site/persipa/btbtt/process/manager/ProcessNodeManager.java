package site.persipa.btbtt.process.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.btbtt.enums.exception.ProcessExceptionEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.enums.exception.ReflectExceptionEnum;
import site.persipa.btbtt.enums.process.NodeEntityGainTypeEnum;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.enums.process.ProcessNodeStatusEnum;
import site.persipa.btbtt.enums.process.ProcessNodeTypeEnum;
import site.persipa.btbtt.mapstruct.process.MapProcessNodeEntityMapper;
import site.persipa.btbtt.mapstruct.process.MapProcessNodeMapper;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.ProcessNodeEntity;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeDto;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;
import site.persipa.btbtt.pojo.reflect.ReflectMethod;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeEntityService;
import site.persipa.btbtt.process.service.ProcessNodeService;
import site.persipa.btbtt.reflect.manager.ReflectEntityManager;
import site.persipa.btbtt.reflect.manager.ReflectMethodManager;
import site.persipa.btbtt.reflect.service.ReflectEntityService;
import site.persipa.btbtt.reflect.service.ReflectMethodService;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessNodeManager {

    private final ProcessConfigService processConfigService;

    private final ProcessNodeService processNodeService;

    private final ProcessNodeEntityService processNodeEntityService;

    private final ReflectEntityManager reflectEntityManager;

    private final ReflectEntityService reflectEntityService;

    private final ReflectMethodManager reflectMethodManager;

    private final ReflectMethodService reflectMethodService;

    private final MapProcessNodeMapper mapProcessNodeMapper;

    private final MapProcessNodeEntityMapper mapProcessNodeEntityMapper;

    @Transactional(rollbackFor = Exception.class)
    public String add(ProcessNodeDto processNodeDto) {
        // 校验配置存在
        String configId = processNodeDto.getConfigId();
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        ProcessNode processNode = mapProcessNodeMapper.fromDto(processNodeDto);
        processNode.setNodeStatus(ProcessNodeStatusEnum.EDITING);

        // 节点sort
        if (processNode.getSort() == null) {
            long nodeCount = processNodeService.count(Wrappers.lambdaQuery(ProcessNode.class)
                    .eq(ProcessNode::getConfigId, configId));
            processNode.setSort(++nodeCount);
        }
        // 执行的方法
        String methodId = processNode.getMethodId();
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        Assert.notNull(reflectMethod, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));

        processNodeService.saveOrUpdate(processNode);
        String nodeId = processNode.getId();

        // 执行方法的参数
        List<ProcessNodeEntity> existNodeEntityList = processNodeEntityService.listByNodeId(nodeId, null);
        Map<Integer, String> argOrderIdMap;
        if (!existNodeEntityList.isEmpty()) {
            argOrderIdMap = existNodeEntityList.stream()
                    .collect(Collectors.toMap(ProcessNodeEntity::getArgOrder, ProcessNodeEntity::getId, (k1, k2) -> k1));
        } else {
            argOrderIdMap = Collections.emptyMap();
        }
        List<ProcessNodeEntity> nodeEntityList = processNodeDto.getNodeEntities().stream()
                .map(entity -> mapProcessNodeEntityMapper.fromDto(entity, nodeId))
                .collect(Collectors.toList());
        nodeEntityList.forEach(entity -> entity.setId(argOrderIdMap.get(entity.getArgOrder())));
        // 保存
        processNodeEntityService.saveOrUpdateBatch(nodeEntityList);

        // 更改配置状态为：编辑中
        processConfig.setProcessStatus(ProcessConfigStatusEnum.EDITING);
        processConfigService.updateById(processConfig);

        return nodeId;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean remove(String nodeId) {
        ProcessNode node = processNodeService.getById(nodeId);
        if (node == null) {
            return true;
        }
        List<ProcessNode> nodeList = processNodeService.listByConfigId(node.getConfigId(), false);
        // 如果删除的不是最后一个节点，则需要校验
        if (!nodeList.isEmpty() && !nodeList.get(0).equals(node)) {
            ProcessConfig processConfig = processConfigService.getById(node.getConfigId());
            processConfig.setProcessStatus(ProcessConfigStatusEnum.EDITING);
            processConfigService.updateById(processConfig);
        }
        List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByNodeId(nodeId, null);
        if (!nodeEntityList.isEmpty()) {
            processNodeEntityService.removeBatchByIds(nodeEntityList);
        }
        processNodeService.removeById(nodeId);

        return true;
    }

    private void verifyNodeEntity(ReflectMethod reflectMethod, List<ProcessNodeEntity> nodeEntityList) {
        Assert.notNull(reflectMethod, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));
        // 方法执行所需参数数量需一致
        Integer argCount = reflectMethod.getArgCount();
        if (Boolean.FALSE.equals(reflectMethod.getStaticMethod())) {
            argCount++;
        }
        Assert.equals(argCount, nodeEntityList.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_ARGS_COUNT_INCORRECT));
        // 参数位置不可重复
        Set<Integer> nodeEntityArgOrderSet = nodeEntityList.stream()
                .map(ProcessNodeEntity::getArgOrder)
                .collect(Collectors.toSet());
        Assert.equals(nodeEntityList.size(), nodeEntityArgOrderSet.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_ARGS_POSITION_INCORRECT));
        // 参数需存在
        List<String> entityIdList = nodeEntityList.stream()
                .filter(entity -> !NodeEntityGainTypeEnum.INPUT.equals(entity.getGainType()))
                .map(ProcessNodeEntity::getEntityId)
                .collect(Collectors.toList());
        List<ReflectEntity> reflectEntityList = reflectEntityService.listByIds(entityIdList);
        Assert.equals(entityIdList.size(), reflectEntityList.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_NOT_FOUND));
    }

    public List<ProcessNode> listByConfigId(String configId) {
        return processNodeService.listByConfigId(configId, true);
    }

    public Object execute(ProcessNode node, Object o) throws PersipaCustomException {
        ProcessNodeTypeEnum nodeType = node.getNodeType();
        switch (nodeType) {
            case SEQUENTIAL:
                o = this.executeProcessNode(node.getId(), o);
                break;
            case LOOP:
                Assert.isTrue(o instanceof Iterable, () -> new PersipaCustomException(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION));
                List<Object> list = new ArrayList<>();
                for (Object value : (Iterable<?>) o) {
                    Object singleResult = this.executeProcessNode(node.getId(), value);
                    list.add(singleResult);
                }
                o = list;
                break;
            case ARRAY:
                // 先校验是否是数组
                Assert.isTrue(o != null && o.getClass().isArray(),
                        () -> new PersipaCustomException(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION));
                int length = Array.getLength(o);
                Class<?> componentType = o.getClass().getComponentType();
                Object resultArray = Array.newInstance(componentType, length);
                for (int i = 0; i < length; i++) {
                    Object singleResult = this.executeProcessNode(node.getId(), Array.get(o, i));
                    Array.set(resultArray, i, singleResult);
                }
                o = resultArray;
                break;
            default:
                break;
        }
        return o;
    }

    private Object executeProcessNode(String nodeId, Object inputArg) throws PersipaCustomException {
        ProcessNode node = processNodeService.getById(nodeId);
        // 获取执行的方法
        String methodId = node.getMethodId();

        // 获取执行的参数
        List<ProcessNodeEntity> processingEntityList = processNodeEntityService.listByNodeId(nodeId, true);
        // 获取所有需要构造的参数
        List<String> constructEntityIdList = processingEntityList.stream()
                .filter(processingEntity -> NodeEntityGainTypeEnum.CONSTRUCT.equals(processingEntity.getGainType()))
                .map(ProcessNodeEntity::getEntityId)
                .collect(Collectors.toList());
        Map<String, Object> constructEntityMap = new HashMap<>();
        for (String entityId : constructEntityIdList) {
            Object tempArg = reflectEntityManager.construct(entityId);
            constructEntityMap.put(entityId, tempArg);
        }

        List<Object> argList = new ArrayList<>();
        for (ProcessNodeEntity processingEntity : processingEntityList) {
            Object tempArg = null;
            NodeEntityGainTypeEnum gainType = processingEntity.getGainType();
            switch (gainType) {
                case INPUT:
                    tempArg = inputArg;
                    break;
                case CONSTRUCT:
                    tempArg = constructEntityMap.get(processingEntity.getEntityId());
                    break;
                default:
                    break;
            }
            argList.add(tempArg);
        }
        return reflectMethodManager.invokeMethod(methodId, argList.toArray(new Object[0]));
    }

    /**
     * 校验节点的方法 方法的参数 是否合法
     *
     * @param nodeId 节点id
     * @return 校验的结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean verify(String nodeId) {
        // 校验方法
        ProcessNode node = processNodeService.getById(nodeId);
        String methodId = node.getMethodId();
        /// 校验方法存在
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        Assert.notNull(reflectMethod, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));


        // 校验方法的参数
        List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByNodeId(nodeId, null);
        this.verifyNodeEntity(reflectMethod, nodeEntityList);

        // 校验通过则 刷新 ProcessConfig 的状态
        node.setNodeStatus(ProcessNodeStatusEnum.SAVED);
        processNodeService.updateById(node);
        processConfigService.flushStatus(node.getConfigId());

        return true;
    }

    /**
     * 校验节点的方法 方法的参数 是否合法
     *
     * @param configId 配置id
     * @return 校验的结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer verifyBatch(String configId) {
        List<ProcessNode> nodeList = processNodeService.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .ne(ProcessNode::getNodeStatus, ProcessNodeStatusEnum.SAVED));
        if (nodeList.isEmpty()) {
            return 0;
        }
        // 校验方法
        Set<String> methodIdSet = nodeList.stream()
                .map(ProcessNode::getMethodId)
                .collect(Collectors.toSet());
        List<ReflectMethod> methodList = reflectMethodService.listByIds(methodIdSet);
        Map<String, ReflectMethod> reflectMethodMap = methodList.stream()
                .collect(Collectors.toMap(ReflectMethod::getId, Function.identity(), (k1, k2) -> k1));
        /// 校验方法存在
        if (methodList.size() != methodIdSet.size()) {
            nodeList.removeIf(node -> !reflectMethodMap.containsKey(node.getMethodId()));
        }
        Assert.notEmpty(nodeList, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));

        // 校验方法的参数
        for (ProcessNode node : nodeList) {
            List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByNodeId(node.getId(), null);
            this.verifyNodeEntity(reflectMethodMap.get(node.getMethodId()), nodeEntityList);
            node.setNodeStatus(ProcessNodeStatusEnum.SAVED);
        }

        // 校验通过则 刷新 ProcessConfig 的状态
        processNodeService.updateBatchById(nodeList);
        processConfigService.flushStatus(configId);

        return nodeList.size();
    }

}
