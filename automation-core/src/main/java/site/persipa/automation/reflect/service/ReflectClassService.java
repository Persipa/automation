package site.persipa.automation.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.enums.reflect.BasicDataTypeEnum;
import site.persipa.automation.enums.reflect.PackagingDataTypeEnum;
import site.persipa.automation.pojo.reflect.ReflectClass;
import site.persipa.common.entity.exception.PersipaCustomException;

import java.util.List;

/**
 * @author persipa
 */
public interface ReflectClassService extends IService<ReflectClass> {

    Class<?> getClazz(String classId);

    List<Class<?>> getClazz(List<String> classIdList);

    BasicDataTypeEnum basicDataType(String classId) throws PersipaCustomException;

    PackagingDataTypeEnum packagingDataType(String classId) throws PersipaCustomException;
}
