package site.persipa.btbtt.jsoup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.JsoupClassType;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
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
    public BasicDataTypeEnum basicDataType(String classId) {
        JsoupClass jsoupClass = this.getById(classId);
        if (JsoupClassType.BASIC_DATA_TYPE.equals(jsoupClass.getClassType())) {
            String className = jsoupClass.getClassName();
            return BasicDataTypeEnum.parseByName(className);
        }
        return null;
    }

    @Override
    public PackagingDataTypeEnum packagingDataType(String classId) {
        JsoupClass jsoupClass = this.getById(classId);
        if (JsoupClassType.PACKAGING_DATA_TYPE.equals(jsoupClass.getClassType())) {
            String classFullName = this.classFullName(jsoupClass);
            return PackagingDataTypeEnum.parseByClassName(classFullName);
        }
        return null;
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
