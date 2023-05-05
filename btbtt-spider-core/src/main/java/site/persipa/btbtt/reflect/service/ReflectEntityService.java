package site.persipa.btbtt.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;

import java.util.List;

/**
 * @author persipa
 */
public interface ReflectEntityService extends IService<ReflectEntity> {

    List<ReflectEntity> subEntityList(String entityId);
}
