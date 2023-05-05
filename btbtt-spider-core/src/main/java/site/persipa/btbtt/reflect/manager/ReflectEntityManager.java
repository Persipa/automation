package site.persipa.btbtt.reflect.manager;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.ReflectEntityConstructorType;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.exception.reflect.EntityConstructException;
import site.persipa.btbtt.exception.reflect.ProcessingException;
import site.persipa.btbtt.reflect.service.ReflectClassService;
import site.persipa.btbtt.reflect.service.ReflectEntityConstructorService;
import site.persipa.btbtt.reflect.service.ReflectEntityService;
import site.persipa.btbtt.pojo.reflect.ReflectEntityConstructor;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class ReflectEntityManager {

    @Autowired
    private ReflectClassService reflectClassService;
    @Autowired
    private ReflectEntityService reflectEntityService;
    @Autowired
    private ReflectEntityConstructorService reflectEntityConstructorService;

    public Object construct(String entityId) throws ProcessingException {
        ReflectEntity reflectEntity = reflectEntityService.getById(entityId);
        return this.construct(reflectEntity);
    }

    private Object construct(ReflectEntity reflectEntity) throws ProcessingException {
        Object result = null;
        ReflectEntityConstructor entityConstructor = reflectEntityConstructorService.getById(reflectEntity.getConstructorId());
        ReflectEntityConstructorType constructType = entityConstructor.getConstructType();
        if (constructType == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND);
        }
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

    private Object constructBasicData(ReflectEntity reflectEntity) throws ProcessingException {
        String classId = reflectEntity.getClassId();
        BasicDataTypeEnum dataType = reflectClassService.basicDataType(classId);
        if (dataType == null) {
            return null;
        }
        Class<?> packagingType = dataType.getPackagingType();
        Method method = ReflectUtil.getMethod(packagingType, "valueOf", String.class);
        if (method == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf");
        }
        return ReflectUtil.invokeStatic(method, reflectEntity.getEntityValue());
    }

    private Object constructPackagingData(ReflectEntity reflectEntity) throws ProcessingException {
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
        if (method == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf");
        }
        return ReflectUtil.invokeStatic(method, reflectEntity.getEntityValue());
    }

    private Object constructNormalData(ReflectEntity reflectEntity) throws ProcessingException {
        String constructorId = reflectEntity.getConstructorId();
        Class<?> entityClass = reflectClassService.getClazz(reflectEntity.getClassId());
        ReflectEntityConstructor entityConstructor = reflectEntityConstructorService.getById(constructorId);

        Integer argCount = entityConstructor.getArgCount();
        // 无参构造
        if (argCount.equals(0)) {
            Constructor<?> constructor = ReflectUtil.getConstructor(entityClass);
            if (constructor == null) {
                throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND);
            }
            try {
                return constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw EntityConstructException.expected(ProcessingExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION);
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
            Object arg =  this.construct(subEntity);
            argList.add(arg);
        }

        if (!argCount.equals(subEntityList.size()) || !argCount.equals(subEntityClassList.size())) {
            // 构造参数数量不对
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCT_ARGS_COUNT_INCORRECT);
        }

        Constructor<?> constructor = ReflectUtil.getConstructor(entityClass, subEntityClassList.toArray(new Class[0]));
        if (constructor == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND);
        }
        try {
            return constructor.newInstance(argList.toArray(new Object[0]));
        } catch (ReflectiveOperationException e) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.REFLECTIVE_OPERATION_EXCEPTION, e.getMessage());
        }
    }
}
