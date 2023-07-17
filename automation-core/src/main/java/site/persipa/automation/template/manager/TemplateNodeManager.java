package site.persipa.automation.template.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.enums.exception.ReflectExceptionEnum;
import site.persipa.automation.enums.exception.TemplateExceptionEnum;
import site.persipa.automation.mapstruct.template.MapTemplateNodeEntityMapper;
import site.persipa.automation.mapstruct.template.MapTemplateNodeMapper;
import site.persipa.automation.pojo.reflect.ReflectMethod;
import site.persipa.automation.pojo.template.TemplateConfig;
import site.persipa.automation.pojo.template.TemplateNode;
import site.persipa.automation.pojo.template.TemplateNodeEntity;
import site.persipa.automation.pojo.template.dto.TemplateNodeDto;
import site.persipa.automation.reflect.service.ReflectMethodService;
import site.persipa.automation.template.service.TemplateConfigService;
import site.persipa.automation.template.service.TemplateNodeEntityService;
import site.persipa.automation.template.service.TemplateNodeService;
import site.persipa.cloud.exception.PersipaRuntimeException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class TemplateNodeManager {

    private final TemplateConfigService templateConfigService;

    private final TemplateNodeService templateNodeService;

    private final TemplateNodeEntityService templateNodeEntityService;

    private final ReflectMethodService reflectMethodService;

    private final MapTemplateNodeMapper mapTemplateNodeMapper;

    private final MapTemplateNodeEntityMapper mapTemplateNodeEntityMapper;

    @Transactional(rollbackFor = Exception.class)
    public String upsert(TemplateNodeDto nodeDto) {
        // 校验配置存在
        String configId = nodeDto.getConfigId();
        TemplateConfig templateConfig = templateConfigService.getById(configId);
        Assert.notNull(templateConfig, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_NOT_EXIST));

        TemplateNode templateNode = mapTemplateNodeMapper.fromDto(nodeDto);

        // 节点sort
        if (templateNode.getSort() == null) {
            long nodeCount = templateNodeService.count(Wrappers.lambdaQuery(TemplateNode.class)
                    .eq(TemplateNode::getConfigId, configId)
                    .orderByDesc(TemplateNode::getSort));
            templateNode.setSort(++nodeCount);
        }

        // 执行的方法
        String methodId = templateNode.getMethodId();
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        Assert.notNull(reflectMethod, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));

        templateNodeService.saveOrUpdate(templateNode);
        String nodeId = templateNode.getId();

        // 执行方法的参数
        List<TemplateNodeEntity> existNodeEntityList = templateNodeEntityService.listByNodeId(nodeId, null);
        Map<Integer, String> argOrderIdMap;
        if (!existNodeEntityList.isEmpty()) {
            argOrderIdMap = existNodeEntityList.stream()
                    .collect(Collectors.toMap(TemplateNodeEntity::getArgOrder, TemplateNodeEntity::getId, (k1, k2) -> k1));
        } else {
            argOrderIdMap = Collections.emptyMap();
        }
        List<TemplateNodeEntity> nodeEntityList = nodeDto.getNodeEntities().stream()
                .map(entity -> mapTemplateNodeEntityMapper.fromDto(entity, nodeId))
                .collect(Collectors.toList());
        nodeEntityList.forEach(entity -> entity.setId(argOrderIdMap.get(entity.getArgOrder())));
        // 保存
        templateNodeEntityService.saveOrUpdateBatch(nodeEntityList);

        return nodeId;
    }
}