package site.persipa.automation.dubbo.provider.template.impl;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import site.persipa.automation.dubbo.provider.template.TemplateConfigApi;
import site.persipa.automation.pojo.template.dto.TemplateConfigGenDto;
import site.persipa.automation.pojo.template.vo.TemplateConfigEntityVo;
import site.persipa.automation.template.manager.TemplateConfigManager;

/**
 * @author persipa
 */
@DubboService(version = "1.0")
@RequiredArgsConstructor
public class TemplateConfigApiImpl implements TemplateConfigApi {

    private final TemplateConfigManager templateConfigManager;

    @Override
    public TemplateConfigEntityVo listRequiredEntities(String configId) {
        return templateConfigManager.findEntity(configId);
    }

    @Override
    public String generate(TemplateConfigGenDto configGenDto) {
        return templateConfigManager.generate(configGenDto);
    }
}