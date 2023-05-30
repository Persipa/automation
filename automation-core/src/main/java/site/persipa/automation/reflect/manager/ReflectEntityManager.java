package site.persipa.automation.reflect.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.enums.exception.ReflectExceptionEnum;
import site.persipa.automation.enums.reflect.BasicDataTypeEnum;
import site.persipa.automation.enums.reflect.PackagingDataTypeEnum;
import site.persipa.automation.enums.reflect.ReflectEntityConstructorType;
import site.persipa.automation.mapstruct.reflect.MapReflectEntityMapper;
import site.persipa.automation.pojo.process.ProcessNodeEntity;
import site.persipa.automation.pojo.reflect.ReflectClass;
import site.persipa.automation.pojo.reflect.ReflectEntity;
import site.persipa.automation.pojo.reflect.ReflectEntityConstructor;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.reflect.vo.ReflectEntityPreviewVo;
import site.persipa.automation.process.service.ProcessNodeEntityService;
import site.persipa.automation.reflect.service.ReflectClassService;
import site.persipa.automation.reflect.service.ReflectEntityConstructorService;
import site.persipa.automation.reflect.service.ReflectEntityService;
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

    private final ProcessNodeEntityService processNodeEntityService;

    private final MapReflectEntityMapper mapReflectEntityMapper;

    @Transactional(rollbackFor = Exception.class)
    public String addEntity(ReflectEntityDto entityDto, boolean force) {
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
        Assert.equals(reflectClassList.size(), classIdSet.size(), () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CLASS_NOT_FOUND));

        // 确定构造器存在
        Set<String> constructorIdSet = entityList.stream()
                .map(ReflectEntity::getConstructorId)
                .collect(Collectors.toSet());
        List<ReflectEntityConstructor> constructorList = reflectEntityConstructorService.listByIds(constructorIdSet);
        Assert.equals(constructorList.size(), constructorIdSet.size(),
                () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CONSTRUCTOR_NOT_FOUND));

        boolean success = reflectEntityService.saveOrUpdateBatch(entityList);
        // 非强制保存的情况，则尝试构造
        if (success && !force) {
            try {
                Object entity = this.construct(entityId);
                if (entity != null) {
                    reflectEntityService.update(Wrappers.lambdaUpdate(ReflectEntity.class)
                            .set(ReflectEntity::getPreviewValue, entity.toString())
                            .eq(ReflectEntity::getId, entityId));
                }
            } catch (PersipaCustomException e) {
                throw new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_CONSTRUCT_FAIL);
            }
        }

        return entityId;
    }

    public boolean remove(String entityId) {
        ReflectEntity reflectEntity = reflectEntityService.getById(entityId);
        Assert.notNull(reflectEntity, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_NOT_FOUND));

        List<ProcessNodeEntity> nodeEntityList = processNodeEntityService.listByEntityId(entityId);
        if (!nodeEntityList.isEmpty()) {
            throw new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_ENTITY_IN_USE);
        }

        List<ReflectEntity> deleteEntityList = reflectEntityService.allSubEntityList(entityId);
        deleteEntityList.add(reflectEntity);

        return reflectEntityService.removeBatchByIds(deleteEntityList);
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
        Assert.notNull(entityConstructor, () -> new PersipaCustomException(ReflectExceptionEnum.REFLECT_ENTITY_NOT_FOUND));
        ReflectEntityConstructorType constructType = entityConstructor.getConstructType();
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
        Assert.notNull(method, () -> new PersipaCustomException(ReflectExceptionEnum.NO_SUCH_METHOD_EXCEPTION, "valueOf"));
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
        Assert.notNull(method, () -> new PersipaCustomException(ReflectExceptionEnum.NO_SUCH_METHOD_EXCEPTION, "valueOf"));
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
            Assert.notNull(constructor, () -> new PersipaCustomException(ReflectExceptionEnum.NO_SUCH_METHOD_EXCEPTION, entityClass.getName()));
            try {
                return constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new PersipaCustomException(ReflectExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION);
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
            throw new PersipaCustomException(ReflectExceptionEnum.REFLECT_CONSTRUCTOR_ARGS_COUNT_INCORRECT);
        }

        Constructor<?> constructor = ReflectUtil.getConstructor(entityClass, subEntityClassList.toArray(new Class[0]));
        Assert.notNull(constructor, () -> new PersipaCustomException(ReflectExceptionEnum.NO_SUCH_METHOD_EXCEPTION, entityClass.getName()));
        try {
            return constructor.newInstance(argList.toArray(new Object[0]));
        } catch (ReflectiveOperationException e) {
            throw new PersipaCustomException(ReflectExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION, e.getMessage());
        }
    }
}
