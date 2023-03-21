package site.persipa.btbtt.jsoup.manager;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.JsoupConstructorType;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.exception.jsoup.EntityConstructException;
import site.persipa.btbtt.exception.jsoup.ProcessingException;
import site.persipa.btbtt.jsoup.service.JsoupClassService;
import site.persipa.btbtt.jsoup.service.JsoupConstructorService;
import site.persipa.btbtt.jsoup.service.JsoupEntityService;
import site.persipa.btbtt.pojo.jsoup.JsoupConstructor;
import site.persipa.btbtt.pojo.jsoup.JsoupEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class JsoupEntityManager {

    @Autowired
    private JsoupClassService jsoupClassService;
    @Autowired
    private JsoupEntityService jsoupEntityService;
    @Autowired
    private JsoupConstructorService jsoupConstructorService;

    public Object construct(String entityId) throws ProcessingException {
        JsoupEntity jsoupEntity = jsoupEntityService.getById(entityId);
        return this.construct(jsoupEntity);
    }

    private Object construct(JsoupEntity jsoupEntity) throws ProcessingException {
        Object result = null;
        JsoupConstructor jsoupConstructor = jsoupConstructorService.getById(jsoupEntity.getConstructorId());
        JsoupConstructorType constructType = jsoupConstructor.getConstructType();
        if (constructType == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND);
        }
        switch (constructType) {
            case BASIC_DATA_TYPE:
                result = this.constructBasicData(jsoupEntity);
                break;
            case PACKAGING_DATA_TYPE:
                result = this.constructPackagingData(jsoupEntity);
                break;
            case NORMAL_DATA_TYPE:
                result = this.constructNormalData(jsoupEntity);
                break;
            default:
                break;
        }
        return result;
    }

    private Object constructBasicData(JsoupEntity jsoupEntity) throws ProcessingException {
        String classId = jsoupEntity.getClassId();
        BasicDataTypeEnum dataType = jsoupClassService.basicDataType(classId);
        if (dataType == null) {
            return null;
        }
        Class<?> packagingType = dataType.getPackagingType();
        Method method = ReflectUtil.getMethod(packagingType, "valueOf", String.class);
        if (method == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf");
        }
        return ReflectUtil.invokeStatic(method, jsoupEntity.getEntityValue());
    }

    private Object constructPackagingData(JsoupEntity jsoupEntity) throws ProcessingException {
        String classId = jsoupEntity.getClassId();
        PackagingDataTypeEnum dataType = jsoupClassService.packagingDataType(classId);
        if (dataType == null) {
            return null;
        }
        Class<?> packagingType = dataType.getPackagingType();
        if (String.class.equals(packagingType)) {
            return jsoupEntity.getEntityValue();
        }
        Method method = ReflectUtil.getMethod(packagingType, "valueOf", String.class);
        if (method == null) {
            throw EntityConstructException.expected(ProcessingExceptionEnum.CONSTRUCTOR_NOT_FOUND, "valueOf");
        }
        return ReflectUtil.invokeStatic(method, jsoupEntity.getEntityValue());
    }

    private Object constructNormalData(JsoupEntity jsoupEntity) throws ProcessingException {
        String constructorId = jsoupEntity.getConstructorId();
        Class<?> entityClass = jsoupClassService.getClazz(jsoupEntity.getClassId());
        JsoupConstructor jsoupConstructor = jsoupConstructorService.getById(constructorId);

        Integer argCount = jsoupConstructor.getArgCount();
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
        List<JsoupEntity> subEntityList = jsoupEntityService.subEntityList(jsoupEntity.getId());
        List<String> subEntityClassIdList = subEntityList.stream()
                .map(JsoupEntity::getClassId)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        List<Class<?>> subEntityClassList = jsoupClassService.getClazz(subEntityClassIdList);
        List<Object> argList = new ArrayList<>();
        for (JsoupEntity subEntity : subEntityList) {
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
