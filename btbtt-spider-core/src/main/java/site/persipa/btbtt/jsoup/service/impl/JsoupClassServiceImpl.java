package site.persipa.btbtt.jsoup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.exception.jsoup.ProcessingException;
import site.persipa.btbtt.jsoup.mapper.JsoupClassMapper;
import site.persipa.btbtt.jsoup.service.JsoupClassService;
import site.persipa.btbtt.pojo.jsoup.JsoupClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author persipa
 */
@Service
public class JsoupClassServiceImpl extends ServiceImpl<JsoupClassMapper, JsoupClass> implements JsoupClassService {
    @Override
    public Class<?> getClazz(String classId) {
        JsoupClass jsoupClass = this.getById(classId);
        return this.getClazz(jsoupClass);
    }

    @Override
    public List<Class<?>> getClazz(List<String> classIdList) {
        List<Class<?>> classList = new ArrayList<>();
        List<JsoupClass> jsoupClassList = this.listByIds(classIdList);
        for (JsoupClass jsoupClass : jsoupClassList) {
            classList.add(this.getClazz(jsoupClass));
        }
        return classList;
    }

    @Override
    public BasicDataTypeEnum basicDataType(String classId) throws ProcessingException {
        JsoupClass jsoupClass = this.getById(classId);
        String className = jsoupClass.getClassName();
        BasicDataTypeEnum result = BasicDataTypeEnum.parseByName(className);
        if (result == null) {
            throw ProcessingException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
        }
        return result;
    }

    @Override
    public PackagingDataTypeEnum packagingDataType(String classId) throws ProcessingException {
        JsoupClass jsoupClass = this.getById(classId);
        String classFullName = this.classFullName(jsoupClass);
        PackagingDataTypeEnum result = PackagingDataTypeEnum.parseByClassName(classFullName);
        if (result == null) {
            throw ProcessingException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
        }
        return result;
    }

    private Class<?> getClazz(JsoupClass jsoupClass) {
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

    private String classFullName(JsoupClass jsoupClass) {
        String packageName = jsoupClass.getPackageName();
        String className = jsoupClass.getClassName();
        return packageName + "." + className;
    }
}
