package site.persipa.btbtt.jsoup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.enums.BasicDataTypeEnum;
import site.persipa.btbtt.enums.PackagingDataTypeEnum;
import site.persipa.btbtt.pojo.jsoup.JsoupClass;

import java.util.List;

/**
 * @author persipa
 */
public interface JsoupClassService extends IService<JsoupClass> {

    Class<?> getClazz(String classId);

    List<Class<?>> getClazz(List<String> classIdList);

    BasicDataTypeEnum basicDataType(String classId);

    PackagingDataTypeEnum packagingDataType(String classId);
}
