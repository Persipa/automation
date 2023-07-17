package site.persipa.automation.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.reflect.ReflectEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author persipa
 */
public interface ReflectEntityService extends IService<ReflectEntity> {

    List<ReflectEntity> subEntityList(String entityId);

    List<ReflectEntity> allSubEntityList(String entityId);

    /**
     * todo 这个方法不严谨 需要改进 应该递归删除
     * @param entityIdSet
     * @return
     */
    Boolean removeBatchByEntityId(Collection<? extends String> entityIdSet);
}
