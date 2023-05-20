package site.persipa.btbtt.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
import site.persipa.btbtt.exception.reflect.ReflectException;
import site.persipa.btbtt.pojo.reflect.ReflectClass;

import java.util.List;

/**
 * @author persipa
 */
public interface ReflectClassService extends IService<ReflectClass> {

    Class<?> getClazz(String classId);

    List<Class<?>> getClazz(List<String> classIdList);

    BasicDataTypeEnum basicDataType(String classId) throws ReflectException;

    PackagingDataTypeEnum packagingDataType(String classId) throws ReflectException;
}
