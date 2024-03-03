package site.persipa.automation.process.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.constant.RabbitConstant;
import site.persipa.automation.enums.exception.ProcessExceptionEnum;
import site.persipa.automation.enums.process.ProcessConfigStateEnum;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.mapstruct.process.MapProcessConfigMapper;
import site.persipa.automation.mapstruct.process.MapProcessNodeEntityMapper;
import site.persipa.automation.mapstruct.process.MapProcessNodeMapper;
import site.persipa.automation.mapstruct.process.MapProcessResultItemMapper;
import site.persipa.automation.pojo.process.*;
import site.persipa.automation.pojo.process.bo.ProcessExecuteBo;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigUpdateDto;
import site.persipa.automation.pojo.process.vo.ProcessConfigDetailVo;
import site.persipa.automation.pojo.process.vo.ProcessNodeEntityVo;
import site.persipa.automation.pojo.process.vo.ProcessNodeVo;
import site.persipa.automation.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.automation.pojo.reflect.ReflectEntity;
import site.persipa.automation.pojo.reflect.ReflectMethod;
import site.persipa.automation.process.service.*;
import site.persipa.automation.reflect.service.ReflectEntityService;
import site.persipa.automation.reflect.service.ReflectMethodService;
import site.persipa.common.entity.exception.PersipaRuntimeException;
import site.persipa.common.entity.pojo.page.dto.PageDto;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessConfigManager {

    private final ProcessConfigService processConfigService;
    private final ProcessNodeService processNodeService;
    private final ProcessNodeEntityService processNodeEntityService;
    private final ProcessExecutionLogService executionLogService;
    private final ReflectMethodService reflectMethodService;
    private final ReflectEntityService reflectEntityService;
    private final ProcessManager processManager;
    private final ProcessExecutionTicketService processExecutionTicketService;

    private final AmqpTemplate amqpTemplate;

    private final MapProcessConfigMapper mapProcessConfigMapper;
    private final MapProcessNodeMapper mapProcessNodeMapper;
    private final MapProcessNodeEntityMapper mapProcessNodeEntityMapper;
    private final MapProcessResultItemMapper mapProcessResultItemMapper;

    /**
     * 添加一个执行配置
     *
     * @param processConfigDto 添加的信息
     * @return 配置的id
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(ProcessConfigDto processConfigDto) {
        long count = processConfigService.count(Wrappers.lambdaQuery(ProcessConfig.class)
                .eq(ProcessConfig::getConfigCode, processConfigDto.getConfigName()));
        Assert.isTrue(count == 0, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NAME_DUPLICATE));

        ProcessConfig spiderConfig = mapProcessConfigMapper.dto2Pojo(processConfigDto);
        spiderConfig.setConfigState(ProcessConfigStateEnum.INIT);
        processConfigService.save(spiderConfig);
        return spiderConfig.getId();
    }

    /**
     * 更新执行的配置
     *
     * @param dto 更新的信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ProcessConfigUpdateDto dto) {
        ProcessConfig processConfig = processConfigService.getById(dto.getId());
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        if (StrUtil.isNotBlank(dto.getConfigName())) {
            long count = processConfigService.count(Wrappers.lambdaQuery(ProcessConfig.class)
                    .eq(ProcessConfig::getConfigName, dto.getConfigName())
                    .ne(ProcessConfig::getId, dto.getId()));
            Assert.isTrue(count == 0, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NAME_DUPLICATE));
            processConfig.setConfigName(dto.getConfigName());
        }
        processConfig.setRemark(dto.getRemark());
        return processConfigService.updateById(processConfig);
    }

    /**
     * 克隆一份可执行的配置
     *
     * @param cloneDto 克隆的信息
     * @return 新配置的id
     */
    @Transactional(rollbackFor = Exception.class)
    public String cloneConfig(ProcessConfigCloneDto cloneDto) {
        // 校验源配置：存在且可执行
        String sourceConfigId = cloneDto.getSourceConfigId();
        ProcessConfig processConfig = processConfigService.getById(sourceConfigId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));
        Assert.isTrue(processConfig.getConfigState().isExecutable(),
                () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NON_EXECUTABLE));

        // 复制配置
        ProcessConfigDto configDto = mapProcessConfigMapper.cloneDto2Dto(cloneDto);
        String configId = this.add(configDto);

        // 复制节点
        processNodeService.cloneNodeList(sourceConfigId, configId);

        // 复制节点执行参数
        List<ProcessNode> sourceNodeList = processNodeService.listByConfigId(sourceConfigId, null);
        Map<Long, String> sourceEntitySortIdMap = sourceNodeList.stream()
                .collect(Collectors.toMap(ProcessNode::getSort, ProcessNode::getId, (k1, k2) -> k1));
        List<ProcessNode> targetNodeList = processNodeService.listByConfigId(configId, null);
        Map<Long, String> targetEntitySortIdMap = targetNodeList.stream()
                .collect(Collectors.toMap(ProcessNode::getSort, ProcessNode::getId, (k1, k2) -> k1));
        Assert.equals(sourceEntitySortIdMap.size(), targetEntitySortIdMap.size(),
                () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_CLONE_FAILED, "节点数量不一致"));
        Map<String, String> entityCloneMap = sourceEntitySortIdMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> targetEntitySortIdMap.get(entry.getKey())));
        processNodeEntityService.cloneNodeEntityList(entityCloneMap);

        // 更新配置状态
        processConfigService.update(Wrappers.lambdaUpdate(ProcessConfig.class)
                .set(ProcessConfig::getConfigState, ProcessConfigStateEnum.SAVED)
                .eq(ProcessConfig::getId, configId));
        return configId;
    }

    /**
     * 预览执行的结果
     *
     * @param configId 配置的id
     * @return 预执行的结果
     */
    public ProcessResultPreviewVo previewResult(String configId) {
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        ProcessResultBo processResultBo = processManager.execute(processConfig, ProcessTypeEnum.PREVIEW);
        ProcessResultPreviewVo previewVo = mapProcessResultItemMapper.resultBoToPreviewVo(processResultBo);
        if (ProcessStatusEnum.SUCCESS.equals(processResultBo.getProcessStatus())) {
            Object processResult = previewVo.getResult();
            boolean canSerialize = true;
            if (processResult instanceof Iterable) {
                for (Object o : (Iterable<?>) processResult) {
                    if (!(o instanceof Serializable)) {
                        canSerialize = false;
                        break;
                    }
                }
            } else if (processResult != null && processResult.getClass().isArray()) {
                int length = Array.getLength(processResult);
                for (int i = 0; i < length; i++) {
                    if (!(Array.get(processResult, i) instanceof Serializable)) {
                        canSerialize = false;
                        break;
                    }
                }
            }
            if (!canSerialize) {
                previewVo.setResult(processResult.toString());
            }

            if (!ProcessConfigStateEnum.VERIFY_PASS.equals(processConfig.getConfigState())) {
                processConfig.setConfigState(ProcessConfigStateEnum.VERIFY_PASS);
                processConfigService.updateById(processConfig);
            }
        }
        return previewVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public String executeManually(String configId) {
        // 校验
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        // 记录
        ProcessExecutionLog processLog = new ProcessExecutionLog();
        processLog.setConfigId(configId);
        processLog.setConfigName(processConfig.getConfigName());
        processLog.setProcessType(ProcessTypeEnum.MANUAL);
        processLog.setCaller("local");
        processLog.setCallTime(LocalDateTime.now());
        executionLogService.save(processLog);

        ProcessExecutionTicket ticket = processExecutionTicketService.generateTicket();

        // 发送mq 执行
        amqpTemplate.convertAndSend(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_DIRECT_EXCHANGE, RabbitConstant.AUTOMATION_PROCESS_EXECUTE_ROUTING,
                new ProcessExecuteBo(configId, ProcessTypeEnum.MANUAL, ticket.getId()));

        // 返回结果
        return ticket.getId();
    }

    /**
     * 分页查询配置信息
     *
     * @param pageDto 查询条件
     * @return 查询结果
     */
    public Page<ProcessConfig> page(PageDto<ProcessConfigPageDto> pageDto) {
        ProcessConfigPageDto params = pageDto.getPayload();
        ProcessConfigStateEnum processStatus = ProcessConfigStateEnum.VALUE_HELPER.find(params.getProcessStatus(), null);
        Page<ProcessConfig> page = new Page<>(pageDto.getCurrent(), pageDto.getSize(), true);

        return processConfigService.page(page, Wrappers.lambdaQuery(ProcessConfig.class)
                .like(StrUtil.isNotBlank(params.getConfigName()), ProcessConfig::getConfigName, params.getConfigName())
                .eq(processStatus != null, ProcessConfig::getConfigState, processStatus));
    }

    public ProcessConfigDetailVo detail(String configId) {
        // 配置
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));
        // 节点
        List<ProcessNode> nodeList = processNodeService.listByConfigId(configId, true);
        // 执行的方法
        Set<String> methodIdSet = nodeList.stream()
                .map(ProcessNode::getMethodId)
                .collect(Collectors.toSet());
        List<ReflectMethod> methodList = reflectMethodService.listByIds(methodIdSet);
        Map<String, ReflectMethod> reflectMethodMap = methodList.stream()
                .collect(Collectors.toMap(ReflectMethod::getId, Function.identity(), (k1, k2) -> k1));
        // 方法执行的参数
        List<ProcessNodeVo> nodeVoList = new ArrayList<>();
        for (ProcessNode node : nodeList) {
            List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByNodeId(node.getId(), true);
            Set<String> entityIdSet = nodeEntityList.stream()
                    .map(ProcessNodeEntity::getEntityId)
                    .filter(StrUtil::isNotEmpty)
                    .collect(Collectors.toSet());

            // 参数实体
            Map<String, ReflectEntity> reflectEntityMap;
            if (!entityIdSet.isEmpty()) {
                List<ReflectEntity> reflectEntityList = reflectEntityService.listByIds(entityIdSet);
                reflectEntityMap = reflectEntityList.stream()
                        .collect(Collectors.toMap(ReflectEntity::getId, Function.identity(), (k1, k2) -> k1));
            } else {
                reflectEntityMap = Collections.emptyMap();
            }

            List<ProcessNodeEntityVo> nodeEntityVoList = nodeEntityList.stream()
                    .map(nodeEntity -> mapProcessNodeEntityMapper.toVo(nodeEntity, reflectEntityMap.get(nodeEntity.getEntityId())))
                    .collect(Collectors.toList());
            ProcessNodeVo vo = mapProcessNodeMapper.toVo(processConfig, node, reflectMethodMap.get(node.getMethodId()), nodeEntityVoList);
            nodeVoList.add(vo);
        }

        return mapProcessConfigMapper.toDetailVo(processConfig, nodeVoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(String configId) {
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        //查询节点
        List<ProcessNode> processNodeList = processNodeService.listByConfigId(configId, true);
        Set<String> removeEntityIdSet = new HashSet<>();
        if (!processNodeList.isEmpty()) {
            for (ProcessNode processNode : processNodeList) {
                // 节点关联的实例
                List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByNodeId(processNode.getId(), true);
                if (!nodeEntityList.isEmpty()) {
                    Set<String> entityIdSet = nodeEntityList.stream()
                            .map(ProcessNodeEntity::getEntityId)
                            .filter(StrUtil::isNotEmpty)
                            .collect(Collectors.toSet());
                    removeEntityIdSet.addAll(entityIdSet);
                    processNodeEntityService.removeBatchByIds(entityIdSet);
                }
            }
            List<String> processNodeIdList = processNodeList.stream().map(ProcessNode::getId).collect(Collectors.toList());
            List<ProcessNodeEntity> usingNodeEntityList = processNodeEntityService.list(Wrappers.lambdaQuery(ProcessNodeEntity.class)
                    .in(ProcessNodeEntity::getEntityId, removeEntityIdSet)
                    .notIn(ProcessNodeEntity::getNodeId, processNodeIdList));
            Set<String> usingEntityIdSet = usingNodeEntityList.stream().map(ProcessNodeEntity::getEntityId).collect(Collectors.toSet());
            removeEntityIdSet.removeIf(usingEntityIdSet::contains);

            processNodeService.removeBatchByIds(processNodeIdList);
        }

        processConfigService.removeById(processConfig.getId());

        if (!removeEntityIdSet.isEmpty()) {
            reflectEntityService.removeBatchByEntityId(removeEntityIdSet);
        }
        return true;
    }
}
