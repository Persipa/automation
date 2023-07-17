package site.persipa.automation.dubbo.provider.template;

import site.persipa.automation.pojo.template.dto.TemplateConfigGenDto;
import site.persipa.automation.pojo.template.vo.TemplateConfigEntityVo;

/**
 * @author persipa
 */
public interface TemplateConfigApi {

    TemplateConfigEntityVo listRequiredEntities(String configId);

    String generate(TemplateConfigGenDto configGenDto);
}