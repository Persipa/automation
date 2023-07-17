package site.persipa.automation.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.template.TemplateEntity;

import java.util.List;
import java.util.Map;

/**
 * @author persipa
 */
public interface TemplateEntityService extends IService<TemplateEntity> {

    /**
     * 根据根实例获取树形的所有实例
     *
     * @param entityId 根实例id
     * @return 模板实例数组
     */
    List<TemplateEntity> treeAsList(String entityId);

    /**
     * 查询叶子实例
     *
     * @param entityId 根实例id
     * @return 叶子实例数组
     */
    List<TemplateEntity> listLeafEntity(String entityId);

    ReflectEntityDto convertToReflectEntityDto(String entityId, Map<String, String> idValueMap);
}