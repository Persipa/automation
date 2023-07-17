package site.persipa.automation.template.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.template.TemplateNode;
import site.persipa.automation.template.mapper.TemplateNodeMapper;
import site.persipa.automation.template.service.TemplateNodeService;

import java.util.List;

/**
 * @author persipa
 */
@Service
public class TemplateNodeServiceImpl extends ServiceImpl<TemplateNodeMapper, TemplateNode>
        implements TemplateNodeService {

    @Override
    public List<TemplateNode> listByConfigId(String configId, Boolean sortAsc){
        return this.list(Wrappers.lambdaQuery(TemplateNode.class)
                .eq(TemplateNode::getConfigId, configId)
                .orderBy(sortAsc != null, Boolean.TRUE.equals(sortAsc), TemplateNode::getSort));
    }

}