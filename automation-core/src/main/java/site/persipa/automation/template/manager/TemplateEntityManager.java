package site.persipa.automation.template.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.persipa.automation.enums.exception.ReflectExceptionEnum;
import site.persipa.automation.enums.exception.TemplateExceptionEnum;
import site.persipa.automation.mapstruct.template.MapTemplateEntityMapper;
import site.persipa.automation.pojo.reflect.ReflectClass;
import site.persipa.automation.pojo.reflect.ReflectEntityConstructor;
import site.persipa.automation.pojo.template.TemplateEntity;
import site.persipa.automation.pojo.template.dto.TemplateEntityDto;
import site.persipa.automation.reflect.service.ReflectClassService;
import site.persipa.automation.reflect.service.ReflectEntityConstructorService;
import site.persipa.automation.template.service.TemplateEntityService;
import site.persipa.common.entity.exception.PersipaRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class TemplateEntityManager {

    private final ReflectClassService reflectClassService;

    private final ReflectEntityConstructorService reflectEntityConstructorService;

    private final TemplateEntityService templateEntityService;

    private final MapTemplateEntityMapper mapTemplateEntityMapper;

    /**
     * 添加实例模板
     *
     * @param entityDto 实例模板信息
     * @return 新实例模板的id
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(TemplateEntityDto entityDto) {
        List<TemplateEntity> entityList = this.parseEntityDto(entityDto, null, null);
        String entityId = entityList.stream().filter(entity -> entity.getParentId() == null)
                .map(TemplateEntity::getId)
                .findFirst()
                .orElseThrow(() -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_ENTITY_NOT_INDEPENDENT));
        Map.Entry<String, Integer> duplicateLabel = entityList.stream()
                .map(TemplateEntity::getLabel)
                .filter(StringUtils::hasLength)
                .collect(Collectors.toMap(Function.identity(), e -> 0, (k1, k2) -> 1))
                .entrySet().stream()
                .filter(entry -> entry.getValue().equals(1))
                .findAny().orElse(null);
        Assert.isNull(duplicateLabel, () -> new PersipaRuntimeException(TemplateExceptionEnum.TEMPLATE_ENTITY_NOT_EXIST));

        // 确定类存在
        Set<String> classIdSet = entityList.stream()
                .map(TemplateEntity::getClassId)
                .collect(Collectors.toSet());
        List<ReflectClass> reflectClassList = reflectClassService.listByIds(classIdSet);
        Assert.equals(reflectClassList.size(), classIdSet.size(), () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CLASS_NOT_FOUND));

        // 确定构造器存在
        Set<String> constructorIdSet = entityList.stream()
                .map(TemplateEntity::getConstructorId)
                .collect(Collectors.toSet());
        List<ReflectEntityConstructor> constructorList = reflectEntityConstructorService.listByIds(constructorIdSet);
        Assert.equals(constructorList.size(), constructorIdSet.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CONSTRUCTOR_NOT_FOUND));

        templateEntityService.saveOrUpdateBatch(entityList);

        return entityId;
    }

    public List<TemplateEntity> parseEntityDto(TemplateEntityDto dto, String parentId, Integer sort) {
        if (dto == null) return null;

        List<TemplateEntity> entityList = new ArrayList<>();
        TemplateEntity templateEntity = mapTemplateEntityMapper.fromDto(dto);
        if (StrUtil.isEmpty(templateEntity.getId())) {
            templateEntity.setId(IdUtil.fastSimpleUUID());
        }
        if (StrUtil.isNotEmpty(parentId) && sort != null) {
            templateEntity.setParentId(parentId);
            templateEntity.setSort(sort);
        }
        entityList.add(templateEntity);

        List<TemplateEntityDto> subDtoList = dto.getSubEntities();
        if (CollUtil.isNotEmpty(subDtoList)) {
            for (TemplateEntityDto subEntityDto : subDtoList) {
                List<TemplateEntity> tempList = this.parseEntityDto(subEntityDto, templateEntity.getId(), subEntityDto.getSort());
                if (CollUtil.isNotEmpty(tempList)) {
                    entityList.addAll(tempList);
                }
            }
        }
        return entityList;
    }
}