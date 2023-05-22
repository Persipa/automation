package site.persipa.btbtt.reflect.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.enums.exception.ReflectExceptionEnum;
import site.persipa.btbtt.enums.reflect.BasicDataTypeEnum;
import site.persipa.btbtt.enums.reflect.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.reflect.ReflectEntityConstructorType;
import site.persipa.btbtt.mapstruct.reflect.MapReflectEntityMapper;
import site.persipa.btbtt.pojo.reflect.ReflectClass;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;
import site.persipa.btbtt.pojo.reflect.ReflectEntityConstructor;
import site.persipa.btbtt.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.btbtt.pojo.reflect.vo.ReflectEntityPreviewVo;
import site.persipa.btbtt.reflect.service.ReflectClassService;
import site.persipa.btbtt.reflect.service.ReflectEntityConstructorService;
import site.persipa.btbtt.reflect.service.ReflectEntityService;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ReflectEntityManager {

    private final ReflectClassService reflectClassService;

    private final ReflectEntityService reflectEntityService;

    private final ReflectEntityConstructorService reflectEntityConstructorService;

    private final MapReflectEntityMapper mapReflectEntityMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean addEntity(ReflectEntityDto entityDto, boolean force) {
        // 树转数组
        List<ReflectEntity> entityList = this.parseEntityDto(entityDto, null, null);
        String entityId = entityList.stream().filter(entity -> entity.getParentId() == null)
                .map(ReflectEntity::getId)
                .findFirst()
                .orElseThrow(() -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_NOT_INDEPENDENT));

        // 确定类存在
        Set<String> classIdSet = entityList.stream()
                .map(ReflectEntity::getClassId)
                .collect(Collectors.toSet());
        List<ReflectClass> reflectClassList = reflectClassService.listByIds(classIdSet);
        Assert.equals(reflectClassList.size(), classIdSet.size(), () -> new PersipaRuntimeException(ReflectExceptionEnum.CLASS_NOT_FOUND));

        // 确定构造器存在
        Set<String> constructorIdSet = entityList.stream()
                .map(ReflectEntity::getConstructorId)
                .collect(Collectors.toSet());
        List<ReflectEntityConstructor> constructorList = reflectEntityConstructorService.listByIds(constructorIdSet);
        Assert.equals(constructorList.size(), constructorIdSet.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.CONSTRUCTOR_NOT_FOUND));

        boolean success = reflectEntityService.saveOrUpdateBatch(entityList);
        // 非强制保存的情况，则尝试构造
        if (success && !force) {
            try {
                this.construct(entityId);
            } catch (PersipaCustomException e) {
                throw new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_CONSTRUCT_FAIL);
            }
        }

        return success;
    }


    public ReflectEntityPreviewVo preview(String entityId) {
        // 实例信息
        ReflectEntity reflectEntity = reflectEntityService.getById(entityId);
        Assert.notNull(reflectEntity, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_NOT_FOUND));
        // 类信息
        ReflectClass reflectClass = reflectClassService.getById(reflectEntity.getClassId());
        // 构造器信息
        ReflectEntityConstructor entityConstructor = reflectEntityConstructorService.getById(reflectEntity.getConstructorId());

        ReflectEntityPreviewVo previewVo = mapReflectEntityMapper.toPreviewVo(reflectEntity, reflectClass, entityConstructor);

        try {
            Object entityValue = this.construct(entityId);
            if (entityValue instanceof Serializable) {
                previewVo.setEntityValue(entityValue);
            } else {
                previewVo.setEntityValue(entityValue.toString());
            }
            previewVo.setConstructSuccess(true);
        } catch (PersipaCustomException e) {
            previewVo.setConstructSuccess(false);
        }

        return previewVo;
    }

    public List<ReflectEntity> parseEntityDto(ReflectEntityDto dto, String parentId, Integer sort) {
        if (dto == null) return null;

        List<ReflectEntity> entityList = new ArrayList<>();
        ReflectEntity reflectEntity = mapReflectEntityMapper.fromDto(dto);
        if (StrUtil.isEmpty(reflectEntity.getId())) {
            reflectEntity.setId(IdUtil.fastSimpleUUID());
        }
        if (StrUtil.isNotEmpty(parentId) && sort != null) {
            reflectEntity.setParentId(parentId);
            reflectEntity.setSort(sort);
        }
        entityList.add(reflectEntity);

        List<ReflectEntityDto> subDtoList = dto.getSubEntities();
        if (CollUtil.isNotEmpty(subDtoList)) {
            for (ReflectEntityDto subEntityDto : subDtoList) {
                List<ReflectEntity> tempList = this.parseEntityDto(subEntityDto, reflectEntity.getId(), subEntityDto.getSort());
                if (CollUtil.isNotEmpty(tempList)) {
                    entityList.addAll(tempList);
                }
            }
        }
        return entityList;
    }

    public Object construct(String entityId) throws PersipaCustomException {
        ReflectEntity reflectEntity = reflectEntityService.getById(entityId);
        return this.construct(reflectEntity);
    }

    private Object construct(ReflectEntity reflectEntity) throws PersipaCustomException {
        Object result = null;
        ReflectEntityConstructor entityConstructor = reflectEntityConstructorService.getById(reflectEntity.getConstructorId());
        ReflectEntityConstructorType constructType = entityConstructor.getConstructType();
        Assert.notNull(constructType, () -> new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND));
        switch (constructType) {
            case BASIC_DATA_TYPE:
                result = this.constructBasicData(reflectEntity);
                break;
            case PACKAGING_DATA_TYPE:
                result = this.constructPackagingData(reflectEntity);
                break;
            case NORMAL_DATA_TYPE:
                result = this.constructNormalData(reflectEntity);
                break;
            default:
                break;
        }
        return result;
    }

    private Object constructBasicData(ReflectEntity reflectEntity) throws PersipaCustomException {
        String classId = reflectEntity.getClassId();
        BasicDataTypeEnum dataType = reflectClassService.basicDataType(classId);
        if (dataType == null) {
            return null;
        }
        Class<?> packagingType = dataType.getPackagingType();
        Method method = ReflectUtil.getMethod(packagingType, "valueOf", String.class);
        Assert.notNull(method, () -> new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf"));
        return ReflectUtil.invokeStatic(method, reflectEntity.getEntityValue());
    }

    private Object constructPackagingData(ReflectEntity reflectEntity) throws PersipaCustomException {
        String classId = reflectEntity.getClassId();
        PackagingDataTypeEnum dataType = reflectClassService.packagingDataType(classId);
        if (dataType == null) {
            return null;
        }
        Class<?> packagingType = dataType.getPackagingType();
        if (String.class.equals(packagingType)) {
            return reflectEntity.getEntityValue();
        }
        Method method = ReflectUtil.getMethod(packagingType, "valueOf", String.class);
        Assert.notNull(method, () -> new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf"));
        return ReflectUtil.invokeStatic(method, reflectEntity.getEntityValue());
    }

    private Object constructNormalData(ReflectEntity reflectEntity) throws PersipaCustomException {
        String constructorId = reflectEntity.getConstructorId();
        Class<?> entityClass = reflectClassService.getClazz(reflectEntity.getClassId());
        ReflectEntityConstructor entityConstructor = reflectEntityConstructorService.getById(constructorId);

        Integer argCount = entityConstructor.getArgCount();
        // 无参构造
        if (argCount.equals(0)) {
            Constructor<?> constructor = ReflectUtil.getConstructor(entityClass);
            Assert.notNull(constructor, () -> new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND));
            try {
                return constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new PersipaCustomException(ProcessingExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION);
            }
        }

        // 有参数构造
        List<ReflectEntity> subEntityList = reflectEntityService.subEntityList(reflectEntity.getId());
        List<String> subEntityClassIdList = subEntityList.stream()
                .map(ReflectEntity::getClassId)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        List<Class<?>> subEntityClassList = reflectClassService.getClazz(subEntityClassIdList);
        List<Object> argList = new ArrayList<>();
        for (ReflectEntity subEntity : subEntityList) {
            Object arg = this.construct(subEntity);
            argList.add(arg);
        }

        if (!argCount.equals(subEntityList.size()) || !argCount.equals(subEntityClassList.size())) {
            // 构造参数数量不对
            throw new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCT_ARGS_COUNT_INCORRECT);
        }

        Constructor<?> constructor = ReflectUtil.getConstructor(entityClass, subEntityClassList.toArray(new Class[0]));
        if (constructor == null) {
            throw new PersipaCustomException(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND);
        }
        try {
            return constructor.newInstance(argList.toArray(new Object[0]));
        } catch (ReflectiveOperationException e) {
            throw new PersipaCustomException(ProcessingExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION, e.getMessage());
        }
    }
}
