package site.persipa.automation.reflect.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.enums.exception.ReflectExceptionEnum;
import site.persipa.automation.enums.reflect.BasicDataTypeEnum;
import site.persipa.automation.enums.reflect.PackagingDataTypeEnum;
import site.persipa.automation.pojo.reflect.ReflectClass;
import site.persipa.automation.reflect.mapper.ReflectClassMapper;
import site.persipa.automation.reflect.service.ReflectClassService;
import site.persipa.cloud.exception.PersipaCustomException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author persipa
 */
@Service
public class ReflectClassServiceImpl extends ServiceImpl<ReflectClassMapper, ReflectClass> implements ReflectClassService {
    @Override
    public Class<?> getClazz(String classId) {
        ReflectClass jsoupClass = this.getById(classId);
        return this.getClazz(jsoupClass);
    }

    @Override
    public List<Class<?>> getClazz(List<String> classIdList) {
        List<Class<?>> classList = new ArrayList<>();
        List<ReflectClass> jsoupClassList = this.listByIds(classIdList);
        for (ReflectClass jsoupClass : jsoupClassList) {
            classList.add(this.getClazz(jsoupClass));
        }
        return classList;
    }

    @Override
    public BasicDataTypeEnum basicDataType(String classId) throws PersipaCustomException {
        ReflectClass jsoupClass = this.getById(classId);
        String className = jsoupClass.getClassName();
        BasicDataTypeEnum result = BasicDataTypeEnum.parseByName(className);
        Assert.notNull(result, () -> new PersipaCustomException(ReflectExceptionEnum.RELECT_CLASS_TYPE_NOT_MATCH));
        return result;
    }

    @Override
    public PackagingDataTypeEnum packagingDataType(String classId) throws PersipaCustomException {
        ReflectClass jsoupClass = this.getById(classId);
        String classFullName = this.classFullName(jsoupClass);
        PackagingDataTypeEnum result = PackagingDataTypeEnum.parseByClassName(classFullName);
        Assert.notNull(result, () -> new PersipaCustomException(ReflectExceptionEnum.RELECT_CLASS_TYPE_NOT_MATCH));
        return result;
    }

    private Class<?> getClazz(ReflectClass jsoupClass) {
        if (jsoupClass == null || StrUtil.isBlank(jsoupClass.getPackageName()) || StrUtil.isBlank(jsoupClass.getClassName())) {
            return null;
        }
        String classFullName = this.classFullName(jsoupClass);
        Class<?> clazz;
        try {
            clazz = Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return clazz;
    }

    private String classFullName(ReflectClass jsoupClass) {
        String packageName = jsoupClass.getPackageName();
        String className = jsoupClass.getClassName();
        return packageName + "." + className;
    }
}
