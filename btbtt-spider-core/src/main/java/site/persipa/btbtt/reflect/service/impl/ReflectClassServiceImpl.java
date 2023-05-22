package site.persipa.btbtt.reflect.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.enums.reflect.BasicDataTypeEnum;
import site.persipa.btbtt.enums.reflect.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.exception.reflect.ReflectException;
import site.persipa.btbtt.reflect.mapper.ReflectClassMapper;
import site.persipa.btbtt.reflect.service.ReflectClassService;
import site.persipa.btbtt.pojo.reflect.ReflectClass;

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
    public BasicDataTypeEnum basicDataType(String classId) throws ReflectException {
        ReflectClass jsoupClass = this.getById(classId);
        String className = jsoupClass.getClassName();
        BasicDataTypeEnum result = BasicDataTypeEnum.parseByName(className);
        if (result == null) {
            throw ReflectException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
        }
        return result;
    }

    @Override
    public PackagingDataTypeEnum packagingDataType(String classId) throws ReflectException {
        ReflectClass jsoupClass = this.getById(classId);
        String classFullName = this.classFullName(jsoupClass);
        PackagingDataTypeEnum result = PackagingDataTypeEnum.parseByClassName(classFullName);
        if (result == null) {
            throw ReflectException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
        }
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
