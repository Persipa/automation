package site.persipa.automation.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.template.TemplateNode;

import java.util.List;

/**
 * @author persipa
 */
public interface TemplateNodeService extends IService<TemplateNode> {

    /**
     * 根据模板id 查询节点
     *
     * @param configId 模板id
     * @param sortAsc 按照sort 字段升序排序，null 为不排序
     * @return 模板节点数组
     */
    List<TemplateNode> listByConfigId(String configId, Boolean sortAsc);
}