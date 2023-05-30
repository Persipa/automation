package site.persipa.btbtt.process.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.btbtt.enums.exception.ProcessExceptionEnum;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;
import site.persipa.btbtt.mapstruct.process.MapProcessConfigMapper;
import site.persipa.btbtt.mapstruct.process.MapProcessResultMapper;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.bo.ProcessExecuteResultBo;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigDto;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.btbtt.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeEntityService;
import site.persipa.btbtt.process.service.ProcessNodeService;
import site.persipa.cloud.exception.PersipaRuntimeException;
import site.persipa.cloud.pojo.page.dto.PageDto;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
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

    private final ProcessManager processManager;

    private final MapProcessConfigMapper mapProcessConfigMapper;

    private final MapProcessResultMapper mapProcessResultMapper;

    @Transactional(rollbackFor = Exception.class)
    public String addConfig(ProcessConfigDto processConfigDto) {
        String resourceName = processConfigDto.getResourceName();
        long count = processConfigService.count(Wrappers.lambdaQuery(ProcessConfig.class)
                .eq(ProcessConfig::getResourceName, resourceName));
        Assert.isTrue(count == 0, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NAME_DUPLICATE));

        ProcessConfig spiderConfig = mapProcessConfigMapper.dto2Pojo(processConfigDto);
        spiderConfig.setProcessStatus(ProcessConfigStatusEnum.INIT);
        processConfigService.save(spiderConfig);
        return spiderConfig.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public String cloneConfig(ProcessConfigCloneDto cloneDto) {
        // 校验源配置：存在且可执行
        String sourceConfigId = cloneDto.getSourceConfigId();
        ProcessConfig processConfig = processConfigService.getById(sourceConfigId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));
        Assert.isTrue(processConfig.getProcessStatus().isExecutable(),
                () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NON_EXECUTABLE));

        // 复制配置
        ProcessConfigDto configDto = mapProcessConfigMapper.cloneDto2Dto(cloneDto);
        String configId = this.addConfig(configDto);

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
                .set(ProcessConfig::getProcessStatus, ProcessConfigStatusEnum.SAVED)
                .eq(ProcessConfig::getId, configId));
        return configId;
    }

    public ProcessResultPreviewVo previewResult(String configId) {
        ProcessExecuteResultBo processResultBo = processManager.execute(configId, ProcessTypeEnum.PREVIEW);
        ProcessResultPreviewVo previewVo = mapProcessResultMapper.executeResultBoToPreviewVo(processResultBo);
        if (processResultBo.isExecuteSuccess()) {
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
        }
        return previewVo;
    }

    public Boolean execute(String configId) {
        ProcessExecuteResultBo executeResultBo = processManager.execute(configId, ProcessTypeEnum.MANUAL);
        return executeResultBo.isExecuteCompleted() && executeResultBo.isExecuteSuccess();
    }

    public Page<ProcessConfig> page(PageDto<ProcessConfigPageDto> pageDto) {
        ProcessConfigPageDto params = pageDto.getPayload();
        ProcessConfigStatusEnum processStatus = ProcessConfigStatusEnum.VALUE_HELPER.find(params.getProcessStatus(), null);
        Page<ProcessConfig> page = new Page<>(pageDto.getCurrent(), pageDto.getSize(), true);

        return processConfigService.page(page, Wrappers.lambdaQuery(ProcessConfig.class)
                .like(StrUtil.isNotBlank(params.getResourceName()), ProcessConfig::getResourceName, params.getResourceName())
                .eq(processStatus != null, ProcessConfig::getProcessStatus, processStatus));
    }

}
