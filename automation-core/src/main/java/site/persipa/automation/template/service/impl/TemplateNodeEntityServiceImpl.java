package site.persipa.automation.template.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.persipa.automation.mapstruct.template.MapTemplateNodeEntityMapper;
import site.persipa.automation.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.automation.pojo.template.TemplateNodeEntity;
import site.persipa.automation.template.mapper.TemplateNodeEntityMapper;
import site.persipa.automation.template.service.TemplateNodeEntityService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
@RequiredArgsConstructor
public class TemplateNodeEntityServiceImpl extends ServiceImpl<TemplateNodeEntityMapper, TemplateNodeEntity>
        implements TemplateNodeEntityService {

    private final MapTemplateNodeEntityMapper mapTemplateNodeEntityMapper;

    @Override
    public List<TemplateNodeEntity> listByNodeId(String nodeId, Boolean argOrderAsc){
        return this.list(Wrappers.lambdaQuery(TemplateNodeEntity.class)
                .eq(TemplateNodeEntity::getNodeId, nodeId)
                .orderBy(argOrderAsc != null, Boolean.TRUE.equals(argOrderAsc), TemplateNodeEntity::getArgOrder));
    }

    @Override
    public List<ProcessNodeEntityDto> convertToProcessNodeEntityDto(String nodeId, Map<String, String> entityIdMap){
        List<TemplateNodeEntity> nodeEntityList = this.listByNodeId(nodeId, true);
        if (nodeEntityList.isEmpty() || entityIdMap == null) {
            return Collections.emptyList();
        }
        return nodeEntityList.stream()
                .map(template -> mapTemplateNodeEntityMapper.toProcessDto(template, entityIdMap.get(template.getEntityId())))
                .collect(Collectors.toList());
    }

}