package site.persipa.automation.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.process.ProcessNodeEntity;

import java.util.List;
import java.util.Map;

/**
 * @author persipa
 */
public interface ProcessNodeEntityService extends IService<ProcessNodeEntity> {

    List<ProcessNodeEntity> listByNodeId(String nodeId, Boolean argOrderAsc);

    List<ProcessNodeEntity> listByEntityId(String entityId);

    boolean cloneNodeEntityList(Map<String, String> entityCloneMap);
}
