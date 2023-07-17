package site.persipa.automation.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.automation.pojo.template.TemplateNodeEntity;

import java.util.List;
import java.util.Map;

/**
 * @author persipa
 */
public interface TemplateNodeEntityService extends IService<TemplateNodeEntity> {

    /**
     * 查询节点和参数的关联关系
     *
     * @param nodeId 节点id
     * @param argOrderAsc 是否按照节点顺序正序排序，null 不排序
     * @return 关联关系数组
     */
    List<TemplateNodeEntity> listByNodeId(String nodeId, Boolean argOrderAsc);

    /**
     * 将模板实例关联关系转换为参数实例关联关系
     *
     * @param nodeId 节点id
     * @param entityIdMap 模板实例与反射实例的id 映射关系
     * @return 参数实例关联关系数组
     */
    List<ProcessNodeEntityDto> convertToProcessNodeEntityDto(String nodeId, Map<String, String> entityIdMap);
}