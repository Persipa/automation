package site.persipa.automation.template.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.persipa.automation.enums.exception.TemplateExceptionEnum;
import site.persipa.automation.mapstruct.template.MapTemplateEntityMapper;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.template.TemplateEntity;
import site.persipa.automation.template.mapper.TemplateEntityMapper;
import site.persipa.automation.template.service.TemplateEntityService;
import site.persipa.common.entity.exception.PersipaRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
@RequiredArgsConstructor
public class TemplateEntityServiceImpl extends ServiceImpl<TemplateEntityMapper, TemplateEntity>
        implements TemplateEntityService {

    private final MapTemplateEntityMapper mapTemplateEntityMapper;

    @Override
    public List<TemplateEntity> treeAsList(String entityId) {
        TemplateEntity templateEntity = this.getById(entityId);
        if (templateEntity == null) {
            return Collections.emptyList();
        }
        List<TemplateEntity> resultList = new ArrayList<>();
        resultList.add(templateEntity);
        List<TemplateEntity> subEntityList = this.list(Wrappers.lambdaQuery(TemplateEntity.class)
                .eq(TemplateEntity::getParentId, templateEntity.getId()));
        for (TemplateEntity subEntity : subEntityList) {
            List<TemplateEntity> tempList = this.treeAsList(subEntity.getId());
            if (!tempList.isEmpty()) {
                resultList.addAll(tempList);
            }
        }

        return resultList;
    }

    @Override
    public List<TemplateEntity> listLeafEntity(String entityId) {
        TemplateEntity templateEntity = this.getById(entityId);
        Assert.notNull(templateEntity, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_ENTITY_NOT_EXIST));

        List<TemplateEntity> resultList = new ArrayList<>();
        List<TemplateEntity> subEntityList = this.list(Wrappers.lambdaQuery(TemplateEntity.class)
                .eq(TemplateEntity::getParentId, templateEntity.getId()));
        if (subEntityList.isEmpty()) {
            return Collections.singletonList(templateEntity);
        }

        for (TemplateEntity subEntity : subEntityList) {
            List<TemplateEntity> tempList = this.listLeafEntity(subEntity.getId());
            resultList.addAll(tempList);
        }

        return resultList;
    }

    @Override
    public ReflectEntityDto convertToReflectEntityDto(String entityId, Map<String, String> idValueMap) {
        TemplateEntity templateEntity = this.getById(entityId);
        ReflectEntityDto result = mapTemplateEntityMapper.toReflectEntityDto(templateEntity);
        if (idValueMap != null && idValueMap.containsKey(entityId)) {
            result.setEntityValue(idValueMap.get(entityId));
        }
        List<TemplateEntity> subEntityList = this.list(Wrappers.lambdaQuery(TemplateEntity.class)
                .eq(TemplateEntity::getParentId, entityId));
        if (!subEntityList.isEmpty()) {
            List<ReflectEntityDto> subEntities = subEntityList.stream()
                    .map(TemplateEntity::getId)
                    .map(subEntityId -> this.convertToReflectEntityDto(subEntityId, idValueMap))
                    .collect(Collectors.toList());
            result.setSubEntities(subEntities);
        }
        return result;
    }
}