package site.persipa.automation.template.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.pojo.template.TemplateConfig;
import site.persipa.automation.template.mapper.TemplateConfigMapper;
import site.persipa.automation.template.service.TemplateConfigService;

/**
 * @author persipa
 */
@Service
public class TemplateConfigServiceImpl extends ServiceImpl<TemplateConfigMapper, TemplateConfig>
        implements TemplateConfigService {

    @Override
    public TemplateConfig findOneByCode(String code) {
        return this.getOne(Wrappers.lambdaQuery(TemplateConfig.class)
                .eq(TemplateConfig::getTemplateCode, code), false);
    }
}