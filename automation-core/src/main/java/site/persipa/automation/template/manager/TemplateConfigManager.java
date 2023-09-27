package site.persipa.automation.template.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.enums.exception.TemplateExceptionEnum;
import site.persipa.automation.mapstruct.template.MapTemplateConfigMapper;
import site.persipa.automation.mapstruct.template.MapTemplateEntityMapper;
import site.persipa.automation.mapstruct.template.MapTemplateNodeEntityMapper;
import site.persipa.automation.mapstruct.template.MapTemplateNodeMapper;
import site.persipa.automation.pojo.process.dto.ProcessConfigDto;
import site.persipa.automation.pojo.process.dto.ProcessNodeDto;
import site.persipa.automation.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.template.TemplateConfig;
import site.persipa.automation.pojo.template.TemplateEntity;
import site.persipa.automation.pojo.template.TemplateNode;
import site.persipa.automation.pojo.template.TemplateNodeEntity;
import site.persipa.automation.pojo.template.dto.TemplateConfigDto;
import site.persipa.automation.pojo.template.dto.TemplateConfigGenDto;
import site.persipa.automation.pojo.template.dto.TemplateConfigPageDto;
import site.persipa.automation.pojo.template.dto.TemplateEntityGenDto;
import site.persipa.automation.pojo.template.vo.*;
import site.persipa.automation.process.manager.ProcessConfigManager;
import site.persipa.automation.process.manager.ProcessNodeManager;
import site.persipa.automation.reflect.manager.ReflectEntityManager;
import site.persipa.automation.template.service.TemplateConfigService;
import site.persipa.automation.template.service.TemplateEntityService;
import site.persipa.automation.template.service.TemplateNodeEntityService;
import site.persipa.automation.template.service.TemplateNodeService;
import site.persipa.cloud.exception.PersipaRuntimeException;
import site.persipa.cloud.pojo.page.dto.PageDto;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class TemplateConfigManager {

    private final TemplateConfigService templateConfigService;

    private final TemplateNodeService templateNodeService;

    private final TemplateNodeEntityService templateNodeEntityService;

    private final TemplateEntityService templateEntityService;

    private final ProcessConfigManager processConfigManager;

    private final ProcessNodeManager processNodeManager;

    private final ReflectEntityManager reflectEntityManager;

    private final MapTemplateConfigMapper mapTemplateConfigMapper;

    private final MapTemplateNodeMapper mapTemplateNodeMapper;

    private final MapTemplateNodeEntityMapper mapTemplateNodeEntityMapper;

    private final MapTemplateEntityMapper mapTemplateEntityMapper;

    /**
     * 新增执行模板
     *
     * @param configDto 模板基础信息
     * @return 新模板的id
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(TemplateConfigDto configDto) {
        String templateCode = configDto.getTemplateCode();
        TemplateConfig existConfig = templateConfigService.findOneByCode(templateCode);
        Assert.isNull(existConfig, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_CODE_HAS_BEAN_USED));

        TemplateConfig templateConfig = mapTemplateConfigMapper.fromDto(configDto);
        templateConfigService.save(templateConfig);

        return templateConfig.getId();
    }

    public Page<TemplateConfig> page(PageDto<TemplateConfigPageDto> pageDto) {
        TemplateConfigPageDto params = pageDto.getPayload();
        Page<TemplateConfig> page = new Page<>(pageDto.getCurrent(), pageDto.getSize(), true);

        return templateConfigService.page(page, Wrappers.lambdaQuery(TemplateConfig.class)
                .like(StrUtil.isNotBlank(params.getTemplateName()), TemplateConfig::getTemplateName, params.getTemplateName())
                .eq(StrUtil.isNotEmpty(params.getTemplateCode()), TemplateConfig::getTemplateCode, params.getTemplateCode()));
    }


    public TemplateConfigDetailVo detail(String configId) {
        // 模板信息
        TemplateConfig templateConfig = templateConfigService.getById(configId);
        Assert.notNull(templateConfig, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_NOT_EXIST));

        // 节点信息
        List<TemplateNode> nodeList = templateNodeService.listByConfigId(configId, true);
        List<TemplateNodeVo> nodes = new ArrayList<>(nodeList.size());
        if (!nodeList.isEmpty()) {
            for (TemplateNode templateNode : nodeList) {
                List<TemplateNodeEntity> nodeEntityList = templateNodeEntityService.listByNodeId(templateNode.getId(), true);
                Set<String> templateEntityIdSet = nodeEntityList.stream()
                        .map(TemplateNodeEntity::getId)
                        .collect(Collectors.toSet());

                List<TemplateEntity> templateEntityList = templateEntityService.listByIds(templateEntityIdSet);
                Map<String, TemplateEntity> templateEntityMap;
                if (!templateEntityIdSet.isEmpty()) {
                    templateEntityMap = templateEntityList.stream()
                            .collect(Collectors.toMap(TemplateEntity::getId, Function.identity(), (k1, k2) -> k1));
                } else {
                    templateEntityMap = Collections.emptyMap();
                }
                List<TemplateNodeEntityVo> entities = nodeEntityList.stream()
                        .map(nodeEntity -> mapTemplateNodeEntityMapper.toVo(nodeEntity, templateEntityMap.get(nodeEntity.getEntityId())))
                        .collect(Collectors.toList());

                TemplateNodeVo nodeVo = mapTemplateNodeMapper.toVo(templateNode, entities);
                nodes.add(nodeVo);
            }
        }

        return mapTemplateConfigMapper.toDetailVo(templateConfig, nodes);
    }

    public TemplateConfigEntityVo findEntity(String configId) {
        // 校验模板
        TemplateConfig templateConfig = templateConfigService.getById(configId);
        Assert.notNull(templateConfig, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_NOT_EXIST));

        TemplateConfigEntityVo result = mapTemplateConfigMapper.toEntityVo(templateConfig);

        // 模板所有节点对应的实例模板的叶子实例数组
        List<TemplateNode> nodeList = templateNodeService.listByConfigId(configId, true);
        List<TemplateEntityBriefVo> entities = nodeList.stream()
                // 节点模板对应的实例关系
                .map(TemplateNode::getId)
                .map(nodeId -> templateNodeEntityService.listByNodeId(nodeId, true))
                // 获取节点所有的根实例
                .flatMap(List::stream)
                // 过滤不需要实例化的
                .map(TemplateNodeEntity::getEntityId)
                .filter(StrUtil::isNotEmpty)
                .distinct()
                // 获取所有根实例的叶子实例
                .map(templateEntityService::listLeafEntity)
                .flatMap(List::stream)
                .map(mapTemplateEntityMapper::toBriefVo)
                .distinct()
                .collect(Collectors.toList());
        result.setEntities(entities);

        return result;
    }

    /**
     * 根据模板生成一个可执行的流程
     *
     * @param configGenDto 生成参数
     * @return 新流程id
     */
    @Transactional(rollbackFor = Exception.class)
    public String generate(TemplateConfigGenDto configGenDto) {
        // 校验模板
        String templateId = configGenDto.getId();
        TemplateConfig templateConfig = templateConfigService.getById(templateId);
        Assert.notNull(templateConfig, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_NOT_EXIST));
        // 模板节点
        List<TemplateNode> templateNodeList = templateNodeService.listByConfigId(templateId, true);

        // 生成流程所需参数
        List<TemplateNodeEntity> templateNodeEntityList = templateNodeList.stream()
                .map(TemplateNode::getId)
                .map(nodeId -> templateNodeEntityService.listByNodeId(nodeId, true))
                .flatMap(List::stream)
                .filter(nodeEntity -> StrUtil.isNotEmpty(nodeEntity.getEntityId()))
                .collect(Collectors.toList());
        Map<String, String> templateProcessEntityIdMap = new HashMap<>();
        List<TemplateEntityGenDto> genEntities = configGenDto.getGenEntities();
        Map<String, String> entityIdValueMap = genEntities.stream()
                .collect(Collectors.toMap(TemplateEntityGenDto::getId, TemplateEntityGenDto::getValue, (k1, k2) -> k1));
        for (TemplateNodeEntity templateNodeEntity : templateNodeEntityList) {
            ReflectEntityDto reflectEntityDto = templateEntityService.convertToReflectEntityDto(templateNodeEntity.getEntityId(), entityIdValueMap);
            String reflectEntityId = reflectEntityManager.addEntity(reflectEntityDto, false);
            templateProcessEntityIdMap.put(templateNodeEntity.getEntityId(), reflectEntityId);
        }

        // 生成流程
        ProcessConfigDto processConfigDto = new ProcessConfigDto();
        processConfigDto.setConfigName(configGenDto.getProcessConfigName());
        processConfigDto.setRemark(templateConfig.getTemplateCode());
        String processConfigId = processConfigManager.add(processConfigDto);

        // 生成节点并绑定节点参数
        for (TemplateNode templateNode : templateNodeList) {
            List<ProcessNodeEntityDto> nodeEntities = templateNodeEntityService.convertToProcessNodeEntityDto(templateNode.getId(), templateProcessEntityIdMap);
            ProcessNodeDto processNodeDto = mapTemplateNodeMapper.toProcessDto(processConfigId, templateNode, nodeEntities);
            processNodeManager.upsert(processNodeDto);
        }

        return processConfigId;
    }
}