package site.persipa.btbtt.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.pojo.process.ProcessNode;

import java.util.List;

public interface ProcessNodeService extends IService<ProcessNode> {

    List<ProcessNode> listByConfigId(String configId, Boolean isAsc);

    boolean cloneNodeList(String sourceConfigId, String targetConfigId);

}
