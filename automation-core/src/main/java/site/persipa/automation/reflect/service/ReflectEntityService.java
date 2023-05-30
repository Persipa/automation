package site.persipa.automation.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.reflect.ReflectEntity;

import java.util.List;

/**
 * @author persipa
 */
public interface ReflectEntityService extends IService<ReflectEntity> {

    List<ReflectEntity> subEntityList(String entityId);

    List<ReflectEntity> allSubEntityList(String entityId);
}
